package com.challenge.simplepicpay.exceptions;

public class TransactionNotAllowedException extends RuntimeException {

    public TransactionNotAllowedException(String msg) {

        super(msg);
    }
}
