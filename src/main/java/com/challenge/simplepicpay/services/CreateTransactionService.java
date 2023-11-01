package com.challenge.simplepicpay.services;

import com.challenge.simplepicpay.dto.transaction.TransactionDTO;
import com.challenge.simplepicpay.entities.transaction.Transaction;
import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public CreateTransactionService(TransactionRepository transactionRepository, UserService userService) {

        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public void doTransaction(TransactionDTO transaction, User sender, User receiver) {

        Transaction createdTransaction = new Transaction();
        createdTransaction.setAmount(transaction.value());
        createdTransaction.setSender(sender);
        createdTransaction.setReceiver(receiver);

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(createdTransaction);
        this.userService.save(sender);
        this.userService.save(receiver);
    }
}
