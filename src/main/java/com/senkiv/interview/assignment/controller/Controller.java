package com.senkiv.interview.assignment.controller;

import com.senkiv.interview.assignment.domain.dto.TransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.TransactionResponseDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserRequestDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserResponseDTO;
import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.service.TransactionService;
import com.senkiv.interview.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    public TransactionResponseDTO authorizeTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        return transactionService.authorizeTransaction(transactionRequestDTO);
    }

    @PostMapping("/cancel")
    public TransactionResponseDTO cancelTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        return transactionService.cancelTransaction(transactionRequestDTO);
    }

    @PostMapping("/transfer")
    public TransactionResponseDTO transferTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        return transactionService.transferTransaction(transactionRequestDTO);
    }

    @RequestMapping("/transactions")
    public Set<Transaction> getTransactions(@RequestParam(value = "userId", required = false) String userId,
                                            @RequestParam(value = "txAmountCy", required = false) String txAmountCy) {
        return transactionService.getTransactionsByUserIdAndAmountCy(userId, txAmountCy);
    }

    @RequestMapping("/transactions/{txId}")
    public Transaction getTransactionByTxId(@PathVariable(value = "txId") String txId) {
        return transactionService.getTransctionByTxId(txId);
    }
}
