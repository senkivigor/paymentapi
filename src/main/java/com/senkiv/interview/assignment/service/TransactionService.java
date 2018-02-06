package com.senkiv.interview.assignment.service;

import com.senkiv.interview.assignment.domain.dto.TransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.TransactionResponseDTO;

public interface TransactionService {
    /**
     * Verify that the user is allowed to
     * process and also reserve amount for future debit and check that
     * the user account will not be over debited.
     *
     * @param transactionRequestDTO authorized transaction request object
     * @return authorization authorized transaction response object
     */
    TransactionResponseDTO authorizeTransaction(TransactionRequestDTO transactionRequestDTO);

    /**
     * Cancel the previous authorize request, i.e. Reserved money will be released from user's account
     *
     * @param transactionRequestDTO cancel transaction request object
     * @return cancel transaction response object
     */
    TransactionResponseDTO cancelTransaction(TransactionRequestDTO transactionRequestDTO);

    /**
     * Processed transaction to credit (increase) or debit (decrease) a
     * user's account balance.
     *
     * @param transactionRequestDTO transfer transaction request object
     * @return transfer transaction response object
     */
    TransactionResponseDTO transferTransaction(TransactionRequestDTO transactionRequestDTO);
}
