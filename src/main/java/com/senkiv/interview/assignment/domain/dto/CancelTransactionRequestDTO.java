package com.senkiv.interview.assignment.domain.dto;

import java.util.UUID;

public class CancelTransactionRequestDTO {

	private String userId;
	private String authCode;
	private String txAmount;
	private String txAmountCy;
	private String txId;
	private Integer txTypeId;
	private String txName;
	private String provider;
	private String originTxId;
	private UUID accountId;
	private String maskedAccount;
	private String statusCode;
	private String pspStatusCode;
	private String pspFee;
	private String pspFeeCy;
	private String pspFeeBase;
	private String pspFeeBaseCy;
	private String pspRefId;
	private String pspStatusMessage;
	private Object attributes;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getTxAmountCy() {
		return txAmountCy;
	}

	public void setTxAmountCy(String txAmountCy) {
		this.txAmountCy = txAmountCy;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public Integer getTxTypeId() {
		return txTypeId;
	}

	public void setTxTypeId(Integer txTypeId) {
		this.txTypeId = txTypeId;
	}

	public String getTxName() {
		return txName;
	}

	public void setTxName(String txName) {
		this.txName = txName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOriginTxId() {
		return originTxId;
	}

	public void setOriginTxId(String originTxId) {
		this.originTxId = originTxId;
	}

	public UUID getAccountId() {
		return accountId;
	}

	public void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}

	public String getMaskedAccount() {
		return maskedAccount;
	}

	public void setMaskedAccount(String maskedAccount) {
		this.maskedAccount = maskedAccount;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getPspStatusCode() {
		return pspStatusCode;
	}

	public void setPspStatusCode(String pspStatusCode) {
		this.pspStatusCode = pspStatusCode;
	}

	public String getPspFee() {
		return pspFee;
	}

	public void setPspFee(String pspFee) {
		this.pspFee = pspFee;
	}

	public String getPspFeeCy() {
		return pspFeeCy;
	}

	public void setPspFeeCy(String pspFeeCy) {
		this.pspFeeCy = pspFeeCy;
	}

	public String getPspFeeBase() {
		return pspFeeBase;
	}

	public void setPspFeeBase(String pspFeeBase) {
		this.pspFeeBase = pspFeeBase;
	}

	public String getPspFeeBaseCy() {
		return pspFeeBaseCy;
	}

	public void setPspFeeBaseCy(String pspFeeBaseCy) {
		this.pspFeeBaseCy = pspFeeBaseCy;
	}

	public String getPspRefId() {
		return pspRefId;
	}

	public void setPspRefId(String pspRefId) {
		this.pspRefId = pspRefId;
	}

	public String getPspStatusMessage() {
		return pspStatusMessage;
	}

	public void setPspStatusMessage(String pspStatusMessage) {
		this.pspStatusMessage = pspStatusMessage;
	}

	public Object getAttributes() {
		return attributes;
	}

	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

}
