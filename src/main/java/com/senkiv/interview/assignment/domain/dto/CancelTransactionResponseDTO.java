package com.senkiv.interview.assignment.domain.dto;

import com.senkiv.interview.assignment.service.validation.ErrorCode;

public class CancelTransactionResponseDTO {

	private String userId;
	private Boolean success;
	private Integer errCode;
	private String errMsg;

	private CancelTransactionResponseDTO(CancelTransactionResponseDTOBuilder builder) {
		userId = builder.userId;
		success = builder.success;
		errCode = builder.errCode;
		errMsg = builder.errMsg;
	}

	public String getUserId() {
		return userId;
	}

	public Boolean getSuccess() {
		return success;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	@Override
	public String toString() {
		return "CancelTransactionResponseDTO{" + "userId='" + userId + '\'' + ", success=" + success + ", errCode=" + errCode + ", errMsg='"
				+ errMsg + '\'' + '}';
	}

	public static CancelTransactionResponseDTOBuilder cancelTransactionResponseDTO() {
		return new CancelTransactionResponseDTOBuilder();
	}

	public static class CancelTransactionResponseDTOBuilder {
		private String userId;
		private Boolean success;
		private Integer errCode;
		private String errMsg;

		private CancelTransactionResponseDTOBuilder() {
		}

		public CancelTransactionResponseDTOBuilder withUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public CancelTransactionResponseDTOBuilder withError(ErrorCode errorCode) {
			if (errorCode == null) {
				this.success = true;
			} else {
				this.success = false;
				this.errCode = errorCode.getCode();
				this.errMsg = errorCode.getMessage();
			}

			return this;
		}

		public CancelTransactionResponseDTO build() {
			return new CancelTransactionResponseDTO(this);
		}
	}
}
