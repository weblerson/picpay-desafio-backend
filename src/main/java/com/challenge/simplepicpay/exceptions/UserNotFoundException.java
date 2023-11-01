package com.challenge.simplepicpay.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String msg) {

        super(msg);
    }
}
