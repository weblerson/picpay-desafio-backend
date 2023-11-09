package com.challenge.simplepicpay.transaction;

import com.challenge.simplepicpay.dto.transaction.TransactionDTO;
import com.challenge.simplepicpay.entities.transaction.Transaction;
import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.entities.user.UserType;
import com.challenge.simplepicpay.repositories.TransactionRepository;
import com.challenge.simplepicpay.repositories.UserRepository;
import com.challenge.simplepicpay.services.TransactionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionTests {

    private static User sender, receiver;

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final MockMvc mockMvc;

    @Autowired
    public TransactionTests(
            TransactionRepository transactionRepository,
            UserRepository userRepository,
            TransactionService transactionService,
            MockMvc mockMvc
    ) {

        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public static void setUpTestData() {

        sender = new User();
        receiver = new User();

        sender.setFirstName("test");
        sender.setLastName("sender");
        sender.setDocument("12345678930");
        sender.setEmail("sender@test.com");
        sender.setPassword("sendertestpassword");
        sender.setBalance(BigDecimal.valueOf(100));
        sender.setUserType(UserType.COMMON);

        receiver.setFirstName("test");
        receiver.setLastName("receiver");
        receiver.setDocument("12345678931");
        receiver.setEmail("receiver@test.com");
        receiver.setPassword("receivertestpassword");
        receiver.setBalance(BigDecimal.valueOf(100));
        receiver.setUserType(UserType.MERCHANT);
    }

    public void saveInstances(User... users) {

        this.userRepository.saveAll(Arrays.asList(users));
    }

    public Transaction createTransacton(User sender, User receiver) {

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(BigDecimal.valueOf(10));

        return this.transactionRepository.save(transaction);
    }

    @Test
    public void testTransactionCreation() {

        this.saveInstances(sender, receiver);
        Transaction transaction = this.createTransacton(sender, receiver);

        Transaction created = this.transactionRepository.save(transaction);

        assertEquals(created, transaction);
    }

    @Test
    public void testIfFoundTransactionEqualsCreatedTransaction() {

        this.saveInstances(sender, receiver);

        Transaction created = this.transactionRepository.save(this.createTransacton(sender, receiver));
        Optional<Transaction> found = this.transactionRepository.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals(created, found.get());
    }

    @Test
    public void testIfTransactionServiceCreateTransactionCreatesATransaction() {

        this.saveInstances(sender, receiver);
        Transaction transaction = this.createTransacton(sender, receiver);

        TransactionDTO transactionDTO = new TransactionDTO(this.transactionRepository.save(transaction));
        TransactionDTO created = this.transactionService.createTransaction(transactionDTO);

        assertNotNull(created);
    }

    @Test
    public void testIfTransactionServiceFindAllTransactionsReturnsAllTransactions() {

        int size = 5;

        this.saveInstances(sender, receiver);
        Transaction transaction = this.createTransacton(sender, receiver);

        for (int i = 0; i < size; i++) {

            this.transactionRepository.save(transaction);
        }

        List<TransactionDTO> transactions = this.transactionService.findAllTransactions();

        assertNotNull(transactions);
    }

    @Test
    public void testIfTransactionControllerCreateTransactionReturnsHttpCreated() throws Exception {

        User createdSender = this.userRepository.save(sender);
        User createdReceiver = this.userRepository.save(receiver);
        TransactionDTO body =
                new TransactionDTO(BigDecimal.valueOf(10), createdSender.getId(), createdReceiver.getId());

        this.mockMvc.perform(
                post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testIfTransactionControllerCreateTransactionCreatesATransaction() throws Exception {

        User createdSender = this.userRepository.save(sender);
        User createdReceiver = this.userRepository.save(receiver);
        TransactionDTO body =
                new TransactionDTO(BigDecimal.valueOf(10), createdSender.getId(), createdReceiver.getId());

        MvcResult result = this.mockMvc.perform(
                        post("/transactions")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        TransactionDTO responseDto = mapper.readValue(json, TransactionDTO.class);

        assertEquals(responseDto.value(), body.value());
        assertEquals(responseDto.senderId(), body.senderId());
        assertEquals(responseDto.receiverId(), body.receiverId());
    }

    @Test
    public void testIfTransactionControllerListAllTransactionsReturnsHttpOk() throws Exception {

        this.mockMvc.perform(
                get("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testIfTransactionControllerListAllTransactionsReturnsATransactionsList() throws Exception {

        MvcResult result = this.mockMvc.perform(
                        get("/transactions")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        List<TransactionDTO> response = mapper.readValue(json, new TypeReference<>() {
        });

        assertNotNull(response);
    }
}
