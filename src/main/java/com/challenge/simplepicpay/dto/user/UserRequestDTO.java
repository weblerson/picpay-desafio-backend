package com.challenge.simplepicpay.dto.user;

import com.challenge.simplepicpay.entities.user.UserType;

public record UserRequestDTO(String firstName, String lastName, String document, String email, String password, UserType userType) {
}
