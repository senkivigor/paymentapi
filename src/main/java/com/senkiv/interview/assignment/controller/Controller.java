package com.senkiv.interview.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senkiv.interview.assignment.domain.dto.AuthorizeTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.AuthorizeTransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.CancelTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.CancelTransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.TransferTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.TransferTransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserRequestDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserResponseDTO;
import com.senkiv.interview.assignment.service.TransactionService;
import com.senkiv.interview.assignment.service.UserService;

@RestController
@RequestMapping("/api")
public class Controller {
	private final TransactionService transactionService;
	private final UserService userService;

	@Autowired
	public Controller(TransactionService transactionService, UserService userService) {
		this.transactionService = transactionService;
		this.userService = userService;
	}

	@PostMapping("/verifyuser")
	public VerifyUserResponseDTO verifyUser(@RequestBody VerifyUserRequestDTO verifyUserRequestDTO) {
		return userService.verifyUser(verifyUserRequestDTO);

	}

	@PostMapping("/authorize")
	public AuthorizeTransactionResponseDTO authorizeTransaction(@RequestBody AuthorizeTransactionRequestDTO authorizeTransactionRequestDTO) {
		return transactionService.authorizeTransaction(authorizeTransactionRequestDTO);
	}

	@PostMapping("/cancel")
	public CancelTransactionResponseDTO cancelTransaction(@RequestBody CancelTransactionRequestDTO cancelTransactionRequestDTO) {
		return transactionService.cancelTransaction(cancelTransactionRequestDTO);
	}

	@PostMapping("/transfer")
	public TransferTransactionResponseDTO transferTransaction(@RequestBody TransferTransactionRequestDTO transferTransactionRequestDTO) {
		return transactionService.transferTransaction(transferTransactionRequestDTO);
	}
}
