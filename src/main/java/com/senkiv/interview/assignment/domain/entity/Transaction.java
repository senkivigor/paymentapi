package com.senkiv.interview.assignment.domain.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "id", "txId" }) })
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String txId;
	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;
	private BigDecimal txAmount;
	private String txAmountCy;
	private BigDecimal fee;
	private String feeCy;
	@Enumerated(EnumType.STRING)
	private TransactionFeeMode feeMode;
	private String authCode;
	@Enumerated(EnumType.STRING)
	private TransactionStatus transactionStatus;
	private Integer errorCode;
	private String errorMessage;

	public enum TransactionFeeMode {
		A,
		D,;
	}

	public enum TransactionStatus {
		COMPLETED,
		PENDING,
		CANCELED,
		FAILED,;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(BigDecimal txAmount) {
		this.txAmount = txAmount;
	}

	public String getTxAmountCy() {
		return txAmountCy;
	}

	public void setTxAmountCy(String txAmountCy) {
		this.txAmountCy = txAmountCy;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getFeeCy() {
		return feeCy;
	}

	public void setFeeCy(String feeCy) {
		this.feeCy = feeCy;
	}

	public TransactionFeeMode getFeeMode() {
		return feeMode;
	}

	public void setFeeMode(TransactionFeeMode feeMode) {
		this.feeMode = feeMode;
	}

	public String getAuthCode() {
		return authCode;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
