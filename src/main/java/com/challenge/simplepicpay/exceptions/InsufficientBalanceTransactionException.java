package com.challenge.simplepicpay.exceptions;

public class InsufficientBalanceTransactionException extends RuntimeException {

    public InsufficientBalanceTransactionException(String msg) {

        super(msg);
    }
}
