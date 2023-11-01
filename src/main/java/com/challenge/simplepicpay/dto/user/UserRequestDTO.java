package com.challenge.simplepicpay.dto.user;

import com.challenge.simplepicpay.entities.user.UserType;

import java.math.BigDecimal;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String document,
        String email,
        String password,
        BigDecimal balance,
        UserType userType
) {
}
