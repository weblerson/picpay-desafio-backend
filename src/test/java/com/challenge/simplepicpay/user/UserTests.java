package com.challenge.simplepicpay.user;

import com.challenge.simplepicpay.dto.user.UserRequestDTO;
import com.challenge.simplepicpay.dto.user.UserResponseDTO;
import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.entities.user.UserType;
import com.challenge.simplepicpay.exceptions.DBException;
import com.challenge.simplepicpay.exceptions.InsufficientBalanceTransactionException;
import com.challenge.simplepicpay.exceptions.TransactionNotAllowedException;
import com.challenge.simplepicpay.exceptions.UserNotFoundException;
import com.challenge.simplepicpay.repositories.UserRepository;
import com.challenge.simplepicpay.services.UserService;
import com.challenge.simplepicpay.utils.RandomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {

    private static User commonTestUser, merchantTestUser;
    private static UserRequestDTO commonRequestDto, merchantRequestDto;

    private final UserRepository userRepository;
    private final UserService userService;
    private final MockMvc mockMvc;

    @Autowired
    public UserTests(UserRepository userRepository, UserService userService, MockMvc mockMvc) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public static void setUpTestData() {

        commonTestUser = new User();
        commonTestUser.setFirstName("test");
        commonTestUser.setLastName("user");
        commonTestUser.setDocument("12345678910");
        commonTestUser.setEmail("test@test.com");
        commonTestUser.setPassword("testpassword");
        commonTestUser.setBalance(BigDecimal.valueOf(100));
        commonTestUser.setUserType(UserType.COMMON);

        merchantTestUser = new User();
        merchantTestUser.setFirstName("test");
        merchantTestUser.setLastName("user");
        merchantTestUser.setDocument("12345678911");
        merchantTestUser.setEmail("testm@test.com");
        merchantTestUser.setPassword("testpassword");
        merchantTestUser.setBalance(BigDecimal.valueOf(100));
        merchantTestUser.setUserType(UserType.MERCHANT);

        commonRequestDto = new UserRequestDTO(
                "test",
                "user",
                "12345678920",
                "test2@test.com",
                "testpassword",
                BigDecimal.valueOf(100),
                UserType.COMMON
        );

        merchantRequestDto = new UserRequestDTO(
                "test",
                "user",
                "12345678921",
                "test21@test.com",
                "testpassword",
                BigDecimal.valueOf(100),
                UserType.MERCHANT
        );
    }

    @Test
    public void testUserCreation() {

        User createdCommon = this.userRepository.save(commonTestUser);
        User createdMerchant = this.userRepository.save(merchantTestUser);

        assertEquals(createdCommon, commonTestUser);
        assertEquals(createdMerchant, merchantTestUser);
    }

    @Test
    public void testIfFoundUserEqualsCreatedUser() {

        User createdCommon = this.userRepository.save(commonTestUser);
        User createdMerchant = this.userRepository.save(merchantTestUser);

        Optional<User> foundCommon = this.userRepository.findById(createdCommon.getId());
        Optional<User> foundMerchant = this.userRepository.findById(createdMerchant.getId());

        assertTrue(foundCommon.isPresent());
        assertTrue(foundMerchant.isPresent());

        assertEquals(createdCommon, foundCommon.get());
        assertEquals(createdMerchant, foundMerchant.get());
    }

    @Test
    public void testIfUserServiceFindByIdIsReturningAnUser() {

        User createdCommon = this.userRepository.save(commonTestUser);
        User createdMerchant = this.userRepository.save(merchantTestUser);

        User foundCommon = this.userService.findById(createdCommon.getId());
        User foundMerchant = this.userService.findById(createdMerchant.getId());

        assertNotNull(foundCommon);
        assertNotNull(foundMerchant);

        assertEquals(createdCommon, foundCommon);
        assertEquals(createdMerchant, foundMerchant);
    }

    @Test
    public void testIfUserServiceFindByIdThrowsUserNotFoundException() {

        assertThrows(UserNotFoundException.class, () -> this.userService.findById(RandomUtil.getRandomLong()));
    }

    @Test
    public void testIfUserServiceSaveSaveUser() {

        UserResponseDTO createdCommonDto = this.userService.save(commonTestUser);
        UserResponseDTO createdMerchantDto = this.userService.save(merchantTestUser);

        User createdCommon = this.userService.findById(createdCommonDto.id());
        User createdMerchant = this.userService.findById(createdMerchantDto.id());

        assertEquals(createdCommon, commonTestUser);
        assertEquals(createdMerchant, merchantTestUser);
    }

    @Test
    public void testIfUserServiceSaveThrowsDBException() {

        assertThrows(DBException.class, () -> this.userService.save(null));
    }

    @Test
    public void testIfUserServiceValidateTransactionThrowsTransactionNotAllowedException() {

        User createdMerchant = this.userRepository.save(merchantTestUser);

        assertThrows(
                TransactionNotAllowedException.class,
                () -> this.userService.validateTransaction(createdMerchant, BigDecimal.valueOf(10))
        );
    }

    @Test
    public void testIfUserServiceValidateTransactionThrowsInsufficientBalanceTransactionException() {

        User createdCommon = this.userRepository.save(commonTestUser);

        assertThrows(
                InsufficientBalanceTransactionException.class,
                () -> this.userService.validateTransaction(
                        createdCommon, BigDecimal.valueOf(1000)
                )
        );
    }

    @Test
    public void testIfUserServiceFindAllUsersReturnsAllUsers() {

        UserResponseDTO createdCommonDto = this.userService.save(commonTestUser);
        UserResponseDTO createdMerchantDto = this.userService.save(merchantTestUser);

        List<UserResponseDTO> createdUsers = List.of(createdCommonDto, createdMerchantDto);
        List<UserResponseDTO> foundUsers = this.userService.findAllUsers();

        assertEquals(createdUsers.get(0).id(), foundUsers.get(0).id());
        assertEquals(createdUsers.get(1).id(), foundUsers.get(1).id());
    }

    @Test
    public void testIfUserControllerCreateUserReturnsHttpCreated() throws Exception {

        this.mockMvc.perform(
                post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(commonRequestDto)))
                .andExpect(status().isCreated());

        this.mockMvc.perform(
                post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(merchantRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testIfUserControllerListAllUsersReturnsHttpOk() throws Exception {

        this.mockMvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
