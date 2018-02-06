package com.senkiv.interview.assignment.domain.dto;

import java.util.UUID;

public class TransactionRequestDTO {
    private String userId;
    private String txAmount;
    private String txAmountCy;
    private String txId;
    private Integer txTypeId;
    private String txName;
    private String authCode;
    private String fee;
    private String feeCy;
    private String feeMode;
    private String txRefId;
    private String provider;
    private String statusCode;
    private String pspStatusCode;
    private String pspRefId;
    private String pspStatusMessage;
    private String pspService;
    private String txPspAmount;
    private String txPspAmountCy;
    private String originTxId;
    private UUID accountId;
    private String accountHolder;
    private String maskedAccount;
    private String pspFee;
    private String pspFeeCy;
    private String pspFeeBase;
    private String pspFeeBaseCy;
    private Object attributes;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFeeCy() {
        return feeCy;
    }

    public void setFeeCy(String feeCy) {
        this.feeCy = feeCy;
    }

    public String getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(String feeMode) {
        this.feeMode = feeMode;
    }

    public String getTxRefId() {
        return txRefId;
    }

    public void setTxRefId(String txRefId) {
        this.txRefId = txRefId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public String getPspService() {
        return pspService;
    }

    public void setPspService(String pspService) {
        this.pspService = pspService;
    }

    public String getTxPspAmount() {
        return txPspAmount;
    }

    public void setTxPspAmount(String txPspAmount) {
        this.txPspAmount = txPspAmount;
    }

    public String getTxPspAmountCy() {
        return txPspAmountCy;
    }

    public void setTxPspAmountCy(String txPspAmountCy) {
        this.txPspAmountCy = txPspAmountCy;
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

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getMaskedAccount() {
        return maskedAccount;
    }

    public void setMaskedAccount(String maskedAccount) {
        this.maskedAccount = maskedAccount;
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

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }
}
