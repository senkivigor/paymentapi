package com.senkiv.interview.assignment.service;

import java.math.BigDecimal;

import com.senkiv.interview.assignment.domain.dto.VerifyUserRequestDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserResponseDTO;
import com.senkiv.interview.assignment.domain.entity.User;

public interface UserService {
	/**
	 * verify that a user is properly authenticated and retrieve user data like name, address,
	 * birth-date etc.
	 *
	 * @param verifyUserRequestDTO verify user request object
	 * @return verify user response object
	 */
	VerifyUserResponseDTO verifyUser(VerifyUserRequestDTO verifyUserRequestDTO);

	/**
	 * change user balance: credit (increase) or debit
	 * (decrease) a user account.
	 *
	 * @param user              user who's amount will be changed
	 * @param userBalanceChange amount to change balance
	 */
	void changeUserBalance(User user, BigDecimal userBalanceChange);
}
