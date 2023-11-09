package com.challenge.simplepicpay.controllers;

import com.challenge.simplepicpay.dto.user.UserRequestDTO;
import com.challenge.simplepicpay.dto.user.UserResponseDTO;
import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @Operation(
            summary = "Create an user",
            description = "Create an user by providing a first name, last name, document, email, password, initial balance and an user type (COMMON|MERCHANT)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created an user",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = UserResponseDTO.class)) }),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> createUser(
            @Parameter(description = "User request info", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @RequestBody UserRequestDTO request) {

        UserResponseDTO created = this.userService.save(new User(request));

        return new ResponseEntity<>(
                created, HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "List all users",
            description = "List all users and return a list of users"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all users",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))) })
    })
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDTO>> listAllUsers() {

        List<UserResponseDTO> users = this.userService.findAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
