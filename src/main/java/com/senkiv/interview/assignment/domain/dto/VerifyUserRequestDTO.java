package com.senkiv.interview.assignment.domain.dto;

public class VerifyUserRequestDTO {
	private String sessionId;
	private String userId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
