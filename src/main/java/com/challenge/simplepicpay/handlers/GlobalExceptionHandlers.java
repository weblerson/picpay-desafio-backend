package com.challenge.simplepicpay.handlers;

import com.challenge.simplepicpay.dto.handler.HandlerDTO;
import com.challenge.simplepicpay.exceptions.*;
import com.challenge.simplepicpay.utils.TimeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlers {

    @ExceptionHandler({ DBException.class })
    public ResponseEntity<HandlerDTO> handleDBException() {

        return new ResponseEntity<>(
                new HandlerDTO("Erro interno do sistema.", TimeUtil.getCurrentTimeMillis()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<HandlerDTO> handleUserNotFoundException() {

        return new ResponseEntity<>(
                new HandlerDTO("Credenciais inválidas!", TimeUtil.getCurrentTimeMillis()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({ TransactionNotAllowedException.class })
    public ResponseEntity<HandlerDTO> handleTransactionNotAllowedException() {

        return new ResponseEntity<>(
                new HandlerDTO("Transação não autorizada!", TimeUtil.getCurrentTimeMillis()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler({ InsufficientBalanceTransactionException.class })
    public ResponseEntity<HandlerDTO> handleInsufficientBalanceTransactionException() {

        return new ResponseEntity<>(
                new HandlerDTO("Saldo insuficiente.", TimeUtil.getCurrentTimeMillis()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler({ TransactionException.class })
    public ResponseEntity<HandlerDTO> handleTransactionException() {

        return new ResponseEntity<>(
                new HandlerDTO("Ocorreu um erro ao realizar a transação.", TimeUtil.getCurrentTimeMillis()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
