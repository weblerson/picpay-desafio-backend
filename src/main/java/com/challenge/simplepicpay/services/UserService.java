package com.challenge.simplepicpay.services;

import com.challenge.simplepicpay.dto.user.UserRequestDTO;
import com.challenge.simplepicpay.dto.user.UserResponseDTO;
import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.entities.user.UserType;
import com.challenge.simplepicpay.exceptions.DBException;
import com.challenge.simplepicpay.exceptions.TransactionException;
import com.challenge.simplepicpay.repositories.UserRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public User findById(Long id) {

        Optional<User> opt = this.userRepository.findById(id);

        return opt.orElseThrow(() -> new DBException("Não existe um usuário com esse Id."));
    }

    public UserResponseDTO save(User user) {

        try {

            User created = this.userRepository.save(user);

            return new UserResponseDTO(created);
        }
        catch (PersistenceException e) {

            throw new DBException(e.getMessage());
        }
    }

    public void validateTransaction(User sender, BigDecimal amount) {

        if (sender.getUserType() == UserType.MERCHANT)
            throw new TransactionException("Usuário não autorizado para efetuar transação.");

        if (sender.getBalance().compareTo(amount) < 0)
            throw new TransactionException("Usuário sem saldo suficiente para realizar a transação.");
    }
}
