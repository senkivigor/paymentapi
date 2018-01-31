package com.senkiv.interview.assignment.service.validation;

public enum ErrorCode {
	USER_NOT_FOUND(1, "Unknown userId"),
	NOT_ENOUGH_MONEY(2, "Insufficient funds on user's account"),
	TRANSACTION_IS_NOT_VALID(3, "Transaction is not valid or has been already processed"),
	CURRENCY_IS_NOT_SUPPORTED(4, "Currency is not supported");

	private final int code;
	private final String message;

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ErrorCode{" + "code=" + code + ", message='" + message + '\'' + '}';
	}
}
