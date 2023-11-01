package com.challenge.simplepicpay.services;

import com.challenge.simplepicpay.dto.transaction.TransactionDTO;
import com.challenge.simplepicpay.entities.transaction.Transaction;
import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.exceptions.TransactionException;
import com.challenge.simplepicpay.impl.authorization.ChallengeAuthorizationService;
import com.challenge.simplepicpay.impl.authorization.MyAuthorizationService;
import com.challenge.simplepicpay.interfaces.AuthorizationService;
import com.challenge.simplepicpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class TransactionService {

    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService challengeAuthorizationService;
    private final MyAuthorizationService myAuthorizationService;
    private final NotificationService notificationService;
    private final CreateTransactionService createTransactionService;

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            UserService userService,
            AuthorizationService challengeAuthorizationService,
            MyAuthorizationService myAuthorizationService,
            NotificationService notificationService,
            CreateTransactionService createTransactionService
    ) {

        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.challengeAuthorizationService = challengeAuthorizationService;
        this.myAuthorizationService = myAuthorizationService;
        this.notificationService = notificationService;
        this.createTransactionService = createTransactionService;
    }

    public TransactionDTO createTransaction(TransactionDTO transaction) {

        CompletableFuture<User> senderFuture = CompletableFuture.supplyAsync(
                () -> this.userService.findById(transaction.senderId())
        );
        CompletableFuture<User> receiverFuture = CompletableFuture.supplyAsync(
                () -> this.userService.findById(transaction.receiverId())
        );

        try {

            User sender = senderFuture.get();
            User receiver = receiverFuture.get();

            this.userService.validateTransaction(sender, transaction.value());

//            O mock do desafio não está mais disponível
//            if (! this.challengeAuthorizationService.authorize(sender, transaction.value()))
//                throw new TransactionException("Transação não autorizada!");

            if (! this.myAuthorizationService.authorize(sender, transaction.value()))
                throw new TransactionException("Transação não autorizada!");

            this.createTransactionService.doTransaction(transaction, sender, receiver);

            this.notificationService.notificate(sender, "Valor enviado para o destinatário.");
            this.notificationService.notificate(receiver, "Valor recebido pelo remetente.");

            return transaction;
        }
        catch (InterruptedException | ExecutionException e) {

            throw new TransactionException(e.getMessage());
        }
    }

    public List<TransactionDTO> findAllTransactions() {

        List<Transaction> transactions = this.transactionRepository.findAll();

        return transactions.stream().map(TransactionDTO::new).toList();
    }
}
