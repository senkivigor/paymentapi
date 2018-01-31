package com.senkiv.interview.assignment.domain.mapper.impl;

import java.math.BigDecimal;

import com.senkiv.interview.assignment.domain.dto.TransferTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.domain.mapper.EntityMapper;

public class TransferTransactionDTOToTransactionMapper implements EntityMapper<Transaction, TransferTransactionRequestDTO> {
	@Override
	public Transaction mapToEntity(TransferTransactionRequestDTO dto) {
		Transaction transaction = new Transaction();
		transaction.setTxId(dto.getTxID());
		transaction.setTxAmount(new BigDecimal(dto.getTxAmount()).setScale(2, BigDecimal.ROUND_UP));
		transaction.setTxAmountCy(dto.getTxAmountCy());
		transaction.setFee(new BigDecimal(dto.getFee()).setScale(2, BigDecimal.ROUND_UP));
		transaction.setFeeCy(dto.getFeeCy());
		transaction.setFeeMode(Transaction.TransactionFeeMode.valueOf(dto.getFeeMode()));
		transaction.setAuthCode(dto.getAuthCode());
		return transaction;
	}
}
