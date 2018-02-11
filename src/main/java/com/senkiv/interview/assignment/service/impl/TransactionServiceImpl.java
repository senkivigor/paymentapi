package com.senkiv.interview.assignment.service.impl;

import com.senkiv.interview.assignment.domain.dto.TransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.TransactionResponseDTO;
import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.domain.entity.User;
import com.senkiv.interview.assignment.domain.entity.converter.EntityConverter;
import com.senkiv.interview.assignment.repository.TransactionRepository;
import com.senkiv.interview.assignment.repository.UserRepository;
import com.senkiv.interview.assignment.service.TransactionService;
import com.senkiv.interview.assignment.service.UserService;
import com.senkiv.interview.assignment.service.impl.helper.CurrencyConverterHelper;
import com.senkiv.interview.assignment.service.validation.ErrorCode;
import com.senkiv.interview.assignment.service.validation.ValidationRule;
import com.senkiv.interview.assignment.service.validation.ValidatorBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public TransactionResponseDTO authorizeTransaction(final TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = EntityConverter.DTO_TO_TRANSACTION.apply(transactionRequestDTO);
        transaction.setAuthCode(UUID.randomUUID().toString());
        User user = userRepository.findOne(transactionRequestDTO.getUserId());
        Optional<ErrorCode> errorCode = Optional.ofNullable(user == null ? ErrorCode.USER_NOT_FOUND : null);
        if (!errorCode.isPresent()) {
            transaction.setUser(user);
            errorCode = validateAuthorizeTransaction(transaction);
        }
        if (!errorCode.isPresent()) {
            transaction.setTransactionStatus(Transaction.TransactionStatus.PENDING);
            transactionRepository.save(transaction);
            logger.debug("transaction with txId " + transaction.getTxId() + " has been authorized");
            return EntityConverter.TRANSACTION_TO_DTO.apply(transaction);
        }
        transaction.setTransactionStatus(Transaction.TransactionStatus.FAILED);
        transactionRepository.save(transaction);
        logger.debug("transaction with txId " + transaction.getTxId() + " has not been authorized: " + errorCode.get().toString());
        return EntityConverter.failedTransactionResponse(transactionRequestDTO, transaction, errorCode.get());
    }

    @Override
    @Transactional
    public TransactionResponseDTO cancelTransaction(final TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = transactionRepository.findByAuthCode(transactionRequestDTO.getAuthCode());
        Optional<ErrorCode> errorCode = validateCancelTransaction(transaction);
        if (!errorCode.isPresent()) {
            transaction.setTransactionStatus(Transaction.TransactionStatus.CANCELED);
            logger.debug("transaction with txId " + transaction.getTxId() + " has been canceled");
            return EntityConverter.TRANSACTION_TO_DTO.apply(transaction);
        }
        return EntityConverter.failedTransactionResponse(transactionRequestDTO, null, errorCode.get());
    }

    @Override
    @Transactional
    public TransactionResponseDTO transferTransaction(final TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = prepareTransactionToTransfer(transactionRequestDTO);
        Optional<ErrorCode> errorCode = validateTransferTransaction(transaction);
        if (!errorCode.isPresent()) {
            transaction.setTransactionStatus(Transaction.TransactionStatus.COMPLETED);
            userService.changeUserBalance(transaction.getUser(), calculateUserBalanceChange(transaction));
            transactionRepository.save(transaction);
            logger.debug("transaction with txId " + transaction.getTxId() + " has been transferred");
            return EntityConverter.TRANSACTION_TO_DTO.apply(transaction);
        }
        transaction.setTransactionStatus(Transaction.TransactionStatus.FAILED);
        transactionRepository.save(transaction);
        logger.debug("transaction with txId " + transaction.getTxId() + " has not been transferred: " + errorCode.get().toString());
        return EntityConverter.failedTransactionResponse(transactionRequestDTO, null, errorCode.get());
    }

    @Override public Transaction getTransctionByTxId(String txId) {
        return transactionRepository.findByTxId(txId);
    }

    @Override public Set<Transaction> getTransactionsByUserIdAndAmountCy(String userId, String txAmountCy) {
        Set<Transaction> transactions;
        if (StringUtils.isBlank(userId)) {
            transactions = new HashSet<>(transactionRepository.findAll());
        } else {
            transactions = userRepository.findOne(userId).getTransactions();
        }
        if (StringUtils.isNotBlank(txAmountCy)) {
            transactions = transactions.stream().filter(transaction ->
                    txAmountCy.equals(transaction.getTxAmountCy())).collect(Collectors.toSet());
        }
        return transactions;
    }

    /**
     * check if transaction has been authorized before and create new one if not
     *
     * @param transactionRequestDTO transactionRequestDTO
     * @return transaction to transfer
     */
    private Transaction prepareTransactionToTransfer(TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction;
        Optional<Transaction> authorizedTransaction = findTransactionByAuthCode(transactionRequestDTO.getAuthCode());
        if (authorizedTransaction.isPresent()) {
            transaction = authorizedTransaction.get();
            transaction.setTxAmount(new BigDecimal(transactionRequestDTO.getTxAmount()).setScale(2, RoundingMode.UP));
            transaction.setTxAmountCy(transactionRequestDTO.getTxAmountCy());
            transaction.setFee(new BigDecimal(transactionRequestDTO.getFee()).setScale(2, RoundingMode.UP));
            transaction.setFeeCy(transactionRequestDTO.getFeeCy());
            transaction.setFeeMode(Transaction.TransactionFeeMode.valueOf(transactionRequestDTO.getFeeMode()));
        } else {
            transaction = EntityConverter.DTO_TO_TRANSACTION.apply(transactionRequestDTO);
            transaction.setUser(userRepository.findOne(transactionRequestDTO.getUserId()));
        }
        return transaction;
    }

    private Optional<ErrorCode> validateAuthorizeTransaction(Transaction transactionToAuthorize) {
        // check if transaction with txId has been already authorized or processed
        ValidationRule transactionExist = transaction -> Optional
                .ofNullable(!transactionRepository.existsByTxId(transaction.getTxId()) ? null : ErrorCode.TRANSACTION_IS_NOT_VALID);
        // check if currency is supported by application
        ValidationRule currencySupported = transaction -> Optional.ofNullable(
                CurrencyConverterHelper.isCurrencySupported(transaction.getTxAmountCy()) ? null : ErrorCode.CURRENCY_IS_NOT_SUPPORTED);
        // check if user won't become over credited
        ValidationRule userBecomeOverCredit = transaction -> {
            if (transaction.getTxAmount().compareTo(BigDecimal.ZERO) >= 0) {
                return Optional.empty();
            }
            BigDecimal lockedAmount = transactionRepository
                    .findByUserAndTransactionStatusAndTxAmountLessThan(transaction.getUser(), Transaction.TransactionStatus.PENDING,
                            BigDecimal.ZERO).stream().map(pendingTransaction -> CurrencyConverterHelper
                            .convert(pendingTransaction.getTxAmountCy(), pendingTransaction.getUser().getBalanceCy(),
                                    pendingTransaction.getTxAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal availableAmount = lockedAmount.add(transaction.getUser().getBalance());
            BigDecimal transactionAmountInUserCurrency = CurrencyConverterHelper
                    .convert(transaction.getTxAmountCy(), transaction.getUser().getBalanceCy(), transaction.getTxAmount());
            return Optional.ofNullable(
                    availableAmount.compareTo(transactionAmountInUserCurrency.negate()) >= 0 ? null : ErrorCode.NOT_ENOUGH_MONEY);
        };

        return ValidatorBuilder.validatorBuilder()
                .first(currencySupported)
                .successor(transactionExist)
                .successor(userBecomeOverCredit)
                .build()
                .validate(transactionToAuthorize);
    }

    private Optional<ErrorCode> validateTransferTransaction(Transaction transactionToTransfer) {
        // check if transaction valid and hasn't benn already processed
        //if transaction is authorized it should exist with status "pending" or does not exist at all
        ValidationRule transactionExist = transaction -> {
            boolean valid;
            if (StringUtils.isNoneEmpty(transaction.getAuthCode())) {
                valid = Transaction.TransactionStatus.PENDING.equals(transaction.getTransactionStatus());
            } else {
                valid = !transactionRepository.existsByTxId(transaction.getTxId());
            }
            return Optional.ofNullable(valid ? null : ErrorCode.TRANSACTION_IS_NOT_VALID);
        };
        // check if transaction user exist
        ValidationRule userExist = transaction -> Optional.ofNullable(transaction.getUser() == null ? ErrorCode.USER_NOT_FOUND : null);
        // check if currency is supported by application
        ValidationRule currencySupported = transaction -> Optional.ofNullable(
                CurrencyConverterHelper.isCurrencySupported(transaction.getTxAmountCy()) && CurrencyConverterHelper
                        .isCurrencySupported(transaction.getFeeCy()) ? null : ErrorCode.CURRENCY_IS_NOT_SUPPORTED);

        return ValidatorBuilder.validatorBuilder()
                .first(userExist)
                .successor(currencySupported)
                .successor(transactionExist)
                .build()
                .validate(transactionToTransfer);
    }

    private Optional<ErrorCode> validateCancelTransaction(Transaction transactionToCancel) {
        // check if transaction valid and hasn't been already processed
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

    private Optional<Transaction> findTransactionByAuthCode(String authCode) {
        if (StringUtils.isEmpty(authCode)) {
            return Optional.empty();
        }
        return Optional.ofNullable(transactionRepository.findByAuthCode(authCode));
    }
}
