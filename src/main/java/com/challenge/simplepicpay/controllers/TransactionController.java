package com.challenge.simplepicpay.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.challenge.simplepicpay.dto.handler.HandlerDTO;
import com.challenge.simplepicpay.dto.transaction.TransactionDTO;
import com.challenge.simplepicpay.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Create a transaction",
            description = "Create a transaction and return it info"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created a transaction",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TransactionDTO.class)) }),

            @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = HandlerDTO.class))),

            @ApiResponse(responseCode = "403", description = "Transaction not authorized",
            content = @Content(schema = @Schema(implementation = HandlerDTO.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO request) {

        TransactionDTO created = this.transactionService.createTransaction(request);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(
            summary = "List all transactions",
            description = "List all transactions and return a list of transactions data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all transactions",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class))) })
    })
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDTO>> listAllTransactions() {

        List<TransactionDTO> transactions = this.transactionService.findAllTransactions();

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
