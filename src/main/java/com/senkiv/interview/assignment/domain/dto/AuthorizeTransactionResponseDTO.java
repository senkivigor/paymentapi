package com.senkiv.interview.assignment.domain.dto;

public class AuthorizeTransactionResponseDTO {
	private String userId;
	private Boolean success;
	private String txId;
	private String merchantTxId;
	private String authCode;
	private Integer errCode;
	private String errMsg;

	private AuthorizeTransactionResponseDTO(AuthorizeTransactionResponseDTOBuilder builder) {
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

	public static AuthorizeTransactionResponseDTOBuilder authorizeTransactionResponseDTO() {
		return new AuthorizeTransactionResponseDTOBuilder();
	}

	@Override
	public String toString() {
		return "AuthorizeTransactionResponseDTO{" + "userId='" + userId + '\'' + ", success=" + success + ", txId='" + txId + '\''
				+ ", merchantTxId='" + merchantTxId + '\'' + ", authCode='" + authCode + '\'' + ", errCode=" + errCode + ", errMsg='" + errMsg
				+ '\'' + '}';
	}

	public static class AuthorizeTransactionResponseDTOBuilder {
		private String userId;
		private Boolean success;
		private String txId;
		private String merchantTxId;
		private String authCode;
		private Integer errCode;
		private String errMsg;

		private AuthorizeTransactionResponseDTOBuilder() {
		}

		public AuthorizeTransactionResponseDTOBuilder withUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public AuthorizeTransactionResponseDTOBuilder withTxId(String txId) {
			this.txId = txId;
			return this;
		}

		public AuthorizeTransactionResponseDTOBuilder withMerchantTxId(String merchantTxId) {
			this.merchantTxId = merchantTxId;
			return this;
		}

		public AuthorizeTransactionResponseDTOBuilder withAuthCode(String authCode) {
			this.authCode = authCode;
			return this;
		}

		public AuthorizeTransactionResponseDTOBuilder withSuccess(Boolean success) {
			this.success = success;
			return this;
		}

		public AuthorizeTransactionResponseDTOBuilder withErrCode(Integer errCode) {
			this.errCode = errCode;
			return this;
		}

		public AuthorizeTransactionResponseDTOBuilder withErrMsg(String errMsg) {
			this.errMsg = errMsg;
			return this;
		}

		public AuthorizeTransactionResponseDTO build() {
			return new AuthorizeTransactionResponseDTO(this);
		}

	}
}
