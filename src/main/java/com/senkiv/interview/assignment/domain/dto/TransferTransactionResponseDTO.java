package com.senkiv.interview.assignment.domain.dto;

public class TransferTransactionResponseDTO {
	private String userId;
	private Boolean success;
	private String txId;
	private String merchantTxId;
	private Integer errCode;
	private String errMsg;

	private TransferTransactionResponseDTO(TransferTransactionResponseDTOBuilder builder) {
		userId = builder.userId;
		success = builder.success;
		txId = builder.txId;
		merchantTxId = builder.merchantTxId;
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

	public Integer getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	@Override
	public String toString() {
		return "TransferTransactionResponseDTO{" + "userId='" + userId + '\'' + ", success=" + success + ", txId='" + txId + '\''
				+ ", merchantTxId='" + merchantTxId + '\'' + ", errCode=" + errCode + ", errMsg='" + errMsg + '\'' + '}';
	}

	public static TransferTransactionResponseDTOBuilder authorizeTransactionResponseDTO() {
		return new TransferTransactionResponseDTOBuilder();
	}

	public static class TransferTransactionResponseDTOBuilder {
		private String userId;
		private Boolean success;
		private String txId;
		private String merchantTxId;
		private Integer errCode;
		private String errMsg;

		private TransferTransactionResponseDTOBuilder() {
		}

		public TransferTransactionResponseDTOBuilder withUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public TransferTransactionResponseDTOBuilder withTxId(String txId) {
			this.txId = txId;
			return this;
		}

		public TransferTransactionResponseDTOBuilder withMerchantTxId(String merchantTxId) {
			this.merchantTxId = merchantTxId;
			return this;
		}

		public TransferTransactionResponseDTOBuilder withSuccess(Boolean success) {
			this.success = success;
			return this;
		}

		public TransferTransactionResponseDTOBuilder withErrCode(Integer errCode) {
			this.errCode = errCode;
			return this;
		}

		public TransferTransactionResponseDTOBuilder withErrMsg(String errMsg) {
			this.errMsg = errMsg;
			return this;
		}

		public TransferTransactionResponseDTO build() {
			return new TransferTransactionResponseDTO(this);
		}
	}
}
