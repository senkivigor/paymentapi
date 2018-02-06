package com.senkiv.interview.assignment.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponseDTO {
    private String userId;
    private Boolean success;
    private String txId;
    private String merchantTxId;
    private String authCode;
    private Integer errCode;
    private String errMsg;

    private TransactionResponseDTO(TransactionResponseDTOBuilder builder) {
        userId = builder.userId;
        success = builder.success;
        txId = builder.txId;
        merchantTxId = builder.merchantTxId;
        authCode = builder.authCode;
        errCode = builder.errCode;
        errMsg = builder.errMsg;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getTxId() {
        return txId;
    }

    public String getMerchantTxId() {
        return merchantTxId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public static TransactionResponseDTOBuilder transactionResponseDTO() {
        return new TransactionResponseDTOBuilder();
    }

    @Override
    public String toString() {
        return "TransactionResponseDTO{" + "userId='" + userId + '\'' + ", success=" + success + ", txId='" + txId + '\''
                + ", merchantTxId='" + merchantTxId + '\'' + ", authCode='" + authCode + '\'' + ", errCode=" + errCode + ", errMsg='" + errMsg
                + '\'' + '}';
    }

    public static class TransactionResponseDTOBuilder {
        private String userId;
        private Boolean success;
        private String txId;
        private String merchantTxId;
        private String authCode;
        private Integer errCode;
        private String errMsg;

        private TransactionResponseDTOBuilder() {
        }

        public TransactionResponseDTOBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public TransactionResponseDTOBuilder withTxId(String txId) {
            this.txId = txId;
            return this;
        }

        public TransactionResponseDTOBuilder withMerchantTxId(String merchantTxId) {
            this.merchantTxId = merchantTxId;
            return this;
        }

        public TransactionResponseDTOBuilder withAuthCode(String authCode) {
            this.authCode = authCode;
            return this;
        }

        public TransactionResponseDTOBuilder withSuccess(Boolean success) {
            this.success = success;
            return this;
        }

        public TransactionResponseDTOBuilder withErrCode(Integer errCode) {
            this.errCode = errCode;
            return this;
        }

        public TransactionResponseDTOBuilder withErrMsg(String errMsg) {
            this.errMsg = errMsg;
            return this;
        }

        public TransactionResponseDTO build() {
            return new TransactionResponseDTO(this);
        }

    }
}
