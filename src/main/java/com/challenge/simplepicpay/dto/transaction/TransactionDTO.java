package com.challenge.simplepicpay.dto.transaction;

import com.challenge.simplepicpay.entities.transaction.Transaction;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value, Long senderId, Long receiverId) {

    public TransactionDTO(Transaction transaction) {
        this(transaction.getAmount(), transaction.getSender().getId(), transaction.getReceiver().getId());
    }
}
