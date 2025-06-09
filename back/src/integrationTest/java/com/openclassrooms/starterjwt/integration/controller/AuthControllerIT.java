package com.openclassrooms.starterjwt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_PASSWORD = "password";

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_shouldSucceed() throws Exception {
        SignupRequest signup = new SignupRequest();
        signup.setEmail(TEST_EMAIL);
        signup.setPassword(TEST_PASSWORD);
        signup.setFirstName("Test");
        signup.setLastName("Test");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void registerUser_shouldFailIfEmailExists() throws Exception {
        User existing = new User(TEST_EMAIL, "Last", "First", passwordEncoder.encode(TEST_PASSWORD), false);
        userRepository.save(existing);

        SignupRequest signup = new SignupRequest();
        signup.setEmail(TEST_EMAIL);
        signup.setPassword(TEST_PASSWORD);
        signup.setFirstName("Test");
        signup.setLastName("User");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }

    @Test
    void authenticateUser_shouldSucceed() throws Exception {
        User user = new User(TEST_EMAIL, "Last", "First", passwordEncoder.encode(TEST_PASSWORD), false);
        userRepository.save(user);

        LoginRequest login = new LoginRequest();
        login.setEmail(TEST_EMAIL);
        login.setPassword(TEST_PASSWORD);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(TEST_EMAIL));
    }

    @Test
    void authenticateUser_shouldFailWithWrongPassword() throws Exception {
        User user = new User(TEST_EMAIL, "Last", "First", passwordEncoder.encode(TEST_PASSWORD), false);
        userRepository.save(user);

        LoginRequest login = new LoginRequest();
        login.setEmail(TEST_EMAIL);
        login.setPassword("wrongPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateUser_shouldFailIfUserDoesNotExist() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("notfound@example.com");
        loginRequest.setPassword("invalidpass");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateUser_shouldReturnIsAdminTrue() throws Exception {
        User admin = new User();
        admin.setEmail("admin@example.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setAdmin(true);
        userRepository.save(admin);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@example.com");
        loginRequest.setPassword("adminpass");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.id").value(admin.getId()))
                .andExpect(jsonPath("$.username").value("admin@example.com"))
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.admin").value(true));
    }
}
