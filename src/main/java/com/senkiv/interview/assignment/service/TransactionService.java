package com.senkiv.interview.assignment.service;

import com.senkiv.interview.assignment.domain.dto.AuthorizeTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.AuthorizeTransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.CancelTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.CancelTransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.TransferTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.TransferTransactionResponseDTO;

public interface TransactionService {
	/**
	 * Verify that the user is allowed to
	 * process and also reserve amount for future debit and check that
	 * the user account will not be over debited.
	 *
	 * @param authorizeTransactionRequestDTO authorized transaction request object
	 * @return authorization authorized transaction response object
	 */
	AuthorizeTransactionResponseDTO authorizeTransaction(AuthorizeTransactionRequestDTO authorizeTransactionRequestDTO);

	/**
	 * Cancel the previous authorize request, i.e. Reserved money will be released from user's account
	 *
	 * @param cancelTransactionRequestDTO cancel transaction request object
	 * @return cancel transaction response object
	 */
	CancelTransactionResponseDTO cancelTransaction(CancelTransactionRequestDTO cancelTransactionRequestDTO);

	/**
	 * Processed transaction to credit (increase) or debit (decrease) a
	 * user's account balance.
	 *
	 * @param transferTransactionRequestDTO transfer transaction request object
	 * @return transfer transaction response object
	 */
	TransferTransactionResponseDTO transferTransaction(TransferTransactionRequestDTO transferTransactionRequestDTO);
}
