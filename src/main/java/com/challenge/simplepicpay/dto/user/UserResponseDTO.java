package com.challenge.simplepicpay.dto.user;

import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.entities.user.UserType;

import java.math.BigDecimal;

public record UserResponseDTO(
        Long id, String firstName, String lastName, String email, BigDecimal balance, UserType userType
) {

    public UserResponseDTO(User user) {

        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBalance(),
                user.getUserType()
        );
    }
}
