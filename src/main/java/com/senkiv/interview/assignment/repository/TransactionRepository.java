package com.senkiv.interview.assignment.repository;

import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByUserAndTransactionStatusAndTxAmountLessThan(User user, Transaction.TransactionStatus transactionStatus, BigDecimal lessThan);

    Transaction findByTxId(String txId);

    boolean existsByTxId(String txId);

    Transaction findByAuthCode(String authCode);
}
