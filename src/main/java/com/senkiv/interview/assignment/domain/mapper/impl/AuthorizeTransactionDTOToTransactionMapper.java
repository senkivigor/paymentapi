package com.senkiv.interview.assignment.domain.mapper.impl;

import java.math.BigDecimal;

import com.senkiv.interview.assignment.domain.dto.AuthorizeTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.domain.mapper.EntityMapper;

public class AuthorizeTransactionDTOToTransactionMapper implements EntityMapper<Transaction, AuthorizeTransactionRequestDTO> {
	@Override
	public Transaction mapToEntity(AuthorizeTransactionRequestDTO dto) {
		Transaction transaction = new Transaction();
		transaction.setTxId(dto.getTxId());
		transaction.setTxAmount(new BigDecimal(dto.getTxAmount()).setScale(2, BigDecimal.ROUND_UP));
		transaction.setTxAmountCy(dto.getTxAmountCy());
		return transaction;
	}
}
