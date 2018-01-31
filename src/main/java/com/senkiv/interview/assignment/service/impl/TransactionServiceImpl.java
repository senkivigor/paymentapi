package com.senkiv.interview.assignment.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senkiv.interview.assignment.domain.dto.AuthorizeTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.AuthorizeTransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.CancelTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.CancelTransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.TransferTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.TransferTransactionResponseDTO;
import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.domain.entity.User;
import com.senkiv.interview.assignment.domain.mapper.impl.AuthorizeTransactionDTOToTransactionMapper;
import com.senkiv.interview.assignment.domain.mapper.impl.TransferTransactionDTOToTransactionMapper;
import com.senkiv.interview.assignment.repository.TransactionRepository;
import com.senkiv.interview.assignment.repository.UserRepository;
import com.senkiv.interview.assignment.service.TransactionService;
import com.senkiv.interview.assignment.service.UserService;
import com.senkiv.interview.assignment.service.validation.ErrorCode;
import com.senkiv.interview.assignment.service.validation.ValidationRule;
import com.senkiv.interview.assignment.service.validation.ValidatorBuilder;

@Service
public class TransactionServiceImpl implements TransactionService {

	private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	private final UserService userService;
	private final UserRepository userRepository;
	private final TransactionRepository transactionRepository;

	@Autowired
	public TransactionServiceImpl(UserService userService, UserRepository userRepository, TransactionRepository transactionRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	@Transactional
	public AuthorizeTransactionResponseDTO authorizeTransaction(final AuthorizeTransactionRequestDTO authorizeTransactionRequestDTO) {
		Transaction transaction = new AuthorizeTransactionDTOToTransactionMapper().mapToEntity(authorizeTransactionRequestDTO);
		transaction.setAuthCode(UUID.randomUUID().toString());
		User user = userRepository.findOne(authorizeTransactionRequestDTO.getUserId());
		transaction.setUser(user);
		Optional<ErrorCode> errorCode = validateAuthorizeTransaction(transaction);
		if (errorCode.isPresent()) {
			transaction.setTransactionStatus(Transaction.TransactionStatus.FAILED);
			transaction.setErrorCode(errorCode.get().getCode());
			transaction.setErrorMessage(errorCode.get().getMessage());
			logger.debug("transaction with txId " + transaction.getTxId() + " has not been authorized: " + errorCode.get().toString());

		} else {
			transaction.setTransactionStatus(Transaction.TransactionStatus.PENDING);
			logger.debug("transaction with txId " + transaction.getTxId() + " has been authorized");
		}
		transactionRepository.save(transaction);
		return buildAuthorizeResponse(authorizeTransactionRequestDTO, transaction);
	}

	@Override
	@Transactional
	public CancelTransactionResponseDTO cancelTransaction(final CancelTransactionRequestDTO cancelTransactionRequestDTO) {
		Transaction transaction = transactionRepository.findByAuthCode(cancelTransactionRequestDTO.getAuthCode());
		Optional<ErrorCode> errorCode = validateCancelTransaction(transaction);
		if (!errorCode.isPresent()) {
			transaction.setTransactionStatus(Transaction.TransactionStatus.CANCELED);
			logger.debug("transaction with txId " + transaction.getTxId() + " has been canceled");
		}
		return buildCancelTransactionResponseDTO(cancelTransactionRequestDTO, errorCode.orElse(null));
	}

	@Override
	@Transactional
	public TransferTransactionResponseDTO transferTransaction(final TransferTransactionRequestDTO transferTransactionRequestDTO) {
		Transaction transaction = prepareTransactionToTransfer(transferTransactionRequestDTO);
		Optional<ErrorCode> errorCode = validateTransferTransaction(transaction);
		if (errorCode.isPresent()) {
			transaction.setTransactionStatus(Transaction.TransactionStatus.FAILED);
			transaction.setErrorCode(errorCode.get().getCode());
			transaction.setErrorMessage(errorCode.get().getMessage());
			logger.debug("transaction with txId " + transaction.getTxId() + " has not been transferred: " + errorCode.get().toString());
		} else {
			transaction.setTransactionStatus(Transaction.TransactionStatus.COMPLETED);
			userService.changeUserBalance(transaction.getUser(), calculateUserBalanceChange(transaction));
			logger.debug("transaction with txId " + transaction.getTxId() + " has been transferred");
		}
		transactionRepository.save(transaction);
		return buildTransferTransactionResponseDTO(transferTransactionRequestDTO, transaction);
	}

	/**
	 * check if transaction has been authorized before and create new one if not
	 *
	 * @param transferTransactionRequestDTO transferTransactionRequestDTO
	 * @return transaction to transfer
	 */
	private Transaction prepareTransactionToTransfer(TransferTransactionRequestDTO transferTransactionRequestDTO) {
		Transaction transaction;
		if (transferTransactionRequestDTO.getAuthCode() != null && !transferTransactionRequestDTO.getAuthCode().isEmpty()
				&& transactionRepository.existsByAuthCode(transferTransactionRequestDTO.getAuthCode())) {
			transaction = transactionRepository.findByAuthCode(transferTransactionRequestDTO.getAuthCode());
			transaction.setTxAmount(new BigDecimal(transferTransactionRequestDTO.getTxAmount()).setScale(2, RoundingMode.UP));
			transaction.setTxAmountCy(transferTransactionRequestDTO.getTxAmountCy());
			transaction.setFee(new BigDecimal(transferTransactionRequestDTO.getFee()).setScale(2, RoundingMode.UP));
			transaction.setFeeCy(transferTransactionRequestDTO.getFeeCy());
			transaction.setFeeMode(Transaction.TransactionFeeMode.valueOf(transferTransactionRequestDTO.getFeeMode()));
		} else {
			transaction = new TransferTransactionDTOToTransactionMapper().mapToEntity(transferTransactionRequestDTO);
			transaction.setUser(userRepository.findOne(transferTransactionRequestDTO.getUserId()));
		}
		return transaction;
	}

	private Optional<ErrorCode> validateAuthorizeTransaction(Transaction transactionToAuthorize) {
		// check if transaction user exist
		ValidationRule userExist = transaction -> Optional.ofNullable(transaction.getUser() != null ? null : ErrorCode.USER_NOT_FOUND);

		// check if transaction with txId has been already authorized or processed
		ValidationRule transactionExist = transaction -> Optional
				.ofNullable(!transactionRepository.existsByTxId(transaction.getTxId()) ? null : ErrorCode.TRANSACTION_IS_NOT_VALID);
		// check if currency is supported by application
		ValidationRule currencySupported = transaction -> Optional.ofNullable(
				CurrencyConverterHelper.isCurrencySupported(transaction.getTxAmountCy()) ? null : ErrorCode.CURRENCY_IS_NOT_SUPPORTED);

		// check if user be over credited after success authorize transaction
		ValidationRule userBecomeOverCredit = transaction -> {
			BigDecimal availableAmount = transactionRepository
					.findByUserAndTransactionStatusAndTxAmountLessThan(transaction.getUser(), Transaction.TransactionStatus.PENDING,
							BigDecimal.ZERO).stream().map(pendingTransaction -> CurrencyConverterHelper
							.convert(pendingTransaction.getTxAmountCy(), pendingTransaction.getUser().getBalanceCy(),
									pendingTransaction.getTxAmount())).reduce(transaction.getUser().getBalance(), BigDecimal::add);
			BigDecimal transactionAmountUserCurrency = CurrencyConverterHelper
					.convert(transaction.getTxAmountCy(), transaction.getUser().getBalanceCy(), transaction.getTxAmount());
			return Optional.ofNullable(
					availableAmount.add(transactionAmountUserCurrency).compareTo(BigDecimal.ZERO) >= 0 ? null : ErrorCode.NOT_ENOUGH_MONEY);
		};

		return ValidatorBuilder.validatorBuilder().first(userExist).successor(transactionExist).successor(currencySupported)
				.successor(userBecomeOverCredit).build().validate(transactionToAuthorize);
	}

	private Optional<ErrorCode> validateTransferTransaction(Transaction transactionToTransfer) {
		// check if transaction valid and hasn't benn already processed
		//if transaction is authorized it should  be with status pending or do not exist at all
		ValidationRule transactionExist = transaction -> {
			boolean valid;
			if (transaction.getAuthCode() != null && !transaction.getAuthCode().isEmpty()) {
				valid = Transaction.TransactionStatus.PENDING.equals(transaction.getTransactionStatus());
			} else {
				valid = !transactionRepository.existsByTxId(transaction.getTxId());
			}
			return Optional.ofNullable(valid ? null : ErrorCode.TRANSACTION_IS_NOT_VALID);
		};
		// check if transaction user exist
		ValidationRule userExist = transaction -> Optional.ofNullable(transaction.getUser() != null ? null : ErrorCode.USER_NOT_FOUND);
		// check if currency is supported by application
		ValidationRule currencySupported = transaction -> Optional.ofNullable(
				CurrencyConverterHelper.isCurrencySupported(transaction.getTxAmountCy()) && CurrencyConverterHelper
						.isCurrencySupported(transaction.getFeeCy()) ? null : ErrorCode.CURRENCY_IS_NOT_SUPPORTED);

		return ValidatorBuilder.validatorBuilder()
				.first(transactionExist)
				.successor(userExist)
				.successor(currencySupported)
				.build()
				.validate(transactionToTransfer);
	}

	private Optional<ErrorCode> validateCancelTransaction(Transaction transactionToCancel) {
		// check if transaction valid and hasn't benn already processed
		ValidationRule validateTransactionExist = transaction -> Optional
				.ofNullable(transaction != null ? null : ErrorCode.TRANSACTION_IS_NOT_VALID);
		return validateTransactionExist.validate(transactionToCancel);
	}

	private BigDecimal calculateUserBalanceChange(Transaction transaction) {
		BigDecimal userBalanceChange = CurrencyConverterHelper
				.convert(transaction.getTxAmountCy(), transaction.getUser().getBalanceCy(), transaction.getTxAmount());
		if (transaction.getFeeMode().equals(Transaction.TransactionFeeMode.D)) {
			userBalanceChange = userBalanceChange.subtract(
					CurrencyConverterHelper.convert(transaction.getFeeCy(), transaction.getUser().getBalanceCy(), transaction.getFee()));
		}
		return userBalanceChange;
	}

	private AuthorizeTransactionResponseDTO buildAuthorizeResponse(AuthorizeTransactionRequestDTO authorizeTransactionRequestDTO,
			Transaction transaction) {
		return AuthorizeTransactionResponseDTO.authorizeTransactionResponseDTO()
				.withUserId(authorizeTransactionRequestDTO.getUserId())
				.withTxId(authorizeTransactionRequestDTO.getTxId())
				.withMerchantTxId(transaction.getId().toString())
				.withAuthCode(transaction.getAuthCode())
				.withSuccess(transaction.getTransactionStatus().equals(Transaction.TransactionStatus.PENDING))
				.withErrCode(transaction.getErrorCode())
				.withErrMsg(transaction.getErrorMessage())
				.build();
	}

	private CancelTransactionResponseDTO buildCancelTransactionResponseDTO(CancelTransactionRequestDTO cancelTransactionRequestDTO,
			ErrorCode validationFailureResult) {
		return CancelTransactionResponseDTO.cancelTransactionResponseDTO()
				.withUserId(cancelTransactionRequestDTO.getUserId())
				.withError(validationFailureResult).build();

	}

	private TransferTransactionResponseDTO buildTransferTransactionResponseDTO(TransferTransactionRequestDTO transferTransactionRequestDTO,
			Transaction transaction) {
		return TransferTransactionResponseDTO.authorizeTransactionResponseDTO()
				.withTxId(transaction.getTxId())
				.withMerchantTxId(transaction.getId().toString())
				.withUserId(transferTransactionRequestDTO.getUserId())
				.withSuccess(transaction.getTransactionStatus().equals(Transaction.TransactionStatus.COMPLETED))
				.withErrCode(transaction.getErrorCode())
				.withErrMsg(transaction.getErrorMessage())
				.build();

	}
}
