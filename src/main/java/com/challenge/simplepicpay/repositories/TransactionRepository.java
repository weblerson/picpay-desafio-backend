package com.challenge.simplepicpay.repositories;

import com.challenge.simplepicpay.entities.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
