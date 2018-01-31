package com.senkiv.interview.assignment.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.domain.entity.User;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
	List<Transaction> findByUserAndTransactionStatusAndTxAmountLessThan(User user, Transaction.TransactionStatus transactionStatus, BigDecimal lessThan);

	Transaction findByTxId(String txId);

	boolean existsByTxId(String txId);

	Transaction findByAuthCode(String authCode);

	boolean existsByAuthCode(String authCode);

}
