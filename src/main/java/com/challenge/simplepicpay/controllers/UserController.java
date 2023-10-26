package com.challenge.simplepicpay.controllers;

import com.challenge.simplepicpay.dto.user.UserRequestDTO;
import com.challenge.simplepicpay.dto.user.UserResponseDTO;
import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {

        UserResponseDTO created = this.userService.save(new User(request));

        return ResponseEntity.ok(created);
    }
}
