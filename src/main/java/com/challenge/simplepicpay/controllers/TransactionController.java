package com.challenge.simplepicpay.controllers;

import com.challenge.simplepicpay.dto.transaction.TransactionDTO;
import com.challenge.simplepicpay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO request) {

        TransactionDTO created = this.transactionService.createTransaction(request);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDTO>> listAllTransactions() {

        List<TransactionDTO> transactions = this.transactionService.findAllTransactions();

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
