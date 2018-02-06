package com.senkiv.interview.assignment.domain.entity.converter;

import com.senkiv.interview.assignment.domain.dto.TransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.TransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserResponseDTO;
import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.domain.entity.User;
import com.senkiv.interview.assignment.service.validation.ErrorCode;

import java.math.BigDecimal;
import java.util.function.Function;

public class EntityConverter {

    public static final Function<User, VerifyUserResponseDTO> USER_TO_DTO = user ->
            VerifyUserResponseDTO.verifyUserResponseDTO()
                    .withUserId(user.getUserId())
                    .withSuccess(true)
                    .withUserCat(user.getUserCat())
                    .withKycStatus(user.getKycStatus())
                    .withSex(user.getSex())
                    .withFirstName(user.getFirstName())
                    .withLastName(user.getLastName())
                    .withStreet(user.getStreet())
                    .withCity(user.getCity())
                    .withZip(user.getZip())
                    .withCountry(user.getCountry())
                    .withEmail(user.getEmail())
                    .withDob(user.getDob())
                    .withMobile(user.getMobile())
                    .withBalance(user.getBalance())
                    .withBalanceCy(user.getBalanceCy())
                    .withLocale(user.getLocale())
                    .build();

    public static final Function<String, VerifyUserResponseDTO> USER_NOT_FOUND_DTO = userId ->
            VerifyUserResponseDTO.verifyUserResponseDTO()
                    .withUserId(userId)
                    .withSuccess(false)
                    .withErrCode(ErrorCode.USER_NOT_FOUND.getCode())
                    .withErrMsg(ErrorCode.USER_NOT_FOUND.getMessage())
                    .build();

    public static final Function<Transaction, TransactionResponseDTO> TRANSACTION_TO_DTO = transaction ->
            TransactionResponseDTO.transactionResponseDTO()
                    .withUserId(transaction.getUser().getUserId())
                    .withTxId(transaction.getTxId())
                    .withMerchantTxId(transaction.getId().toString())
                    .withAuthCode(transaction.getAuthCode())
                    .withSuccess(true)
                    .build();


    public static final Function<TransactionRequestDTO, Transaction> DTO_TO_TRANSACTION = transactionRequestDTO -> {
        Transaction transaction = new Transaction();
        transaction.setTxId(transactionRequestDTO.getTxId());
        if (transactionRequestDTO.getTxAmount() != null) {
            transaction.setTxAmount(new BigDecimal(transactionRequestDTO.getTxAmount()).setScale(2, BigDecimal.ROUND_UP));
        }
        transaction.setTxAmountCy(transactionRequestDTO.getTxAmountCy());
        if (transactionRequestDTO.getFee() != null) {
            transaction.setFee(new BigDecimal(transactionRequestDTO.getFee()).setScale(2, BigDecimal.ROUND_UP));
        }
        transaction.setFeeCy(transactionRequestDTO.getFeeCy());
        if (transactionRequestDTO.getFeeMode() != null) {
            transaction.setFeeMode(Transaction.TransactionFeeMode.valueOf(transactionRequestDTO.getFeeMode()));
        }
        transaction.setAuthCode(transactionRequestDTO.getAuthCode());

        return transaction;
    };

    public static TransactionResponseDTO failedTransactionResponse(TransactionRequestDTO transactionRequestDTO, Transaction transaction,
                                                                   ErrorCode errorCode) {
        return TransactionResponseDTO.transactionResponseDTO()
                .withUserId(transactionRequestDTO.getUserId())
                .withTxId(transactionRequestDTO.getTxId())
                .withAuthCode(transaction != null ? transaction.getAuthCode() : transactionRequestDTO.getAuthCode())
                .withMerchantTxId(transaction != null ? transaction.getId().toString() : null)
                .withSuccess(false)
                .withErrCode(errorCode.getCode())
                .withErrMsg(errorCode.getMessage())
                .build();
    }
}
