package com.challenge.simplepicpay.dto.user;

import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.entities.user.UserType;

public record UserResponseDTO(Long id, String firstName, String lastName, String email, UserType userType) {

    public UserResponseDTO(User user) {

        this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserType());
    }
}
