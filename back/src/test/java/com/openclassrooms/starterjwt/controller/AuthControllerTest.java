package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("test123");

        User user = new User();
        user.setEmail("test@test.com");
        user.setAdmin(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(42L);
        when(userDetails.getUsername()).thenReturn("test@test.com");
        when(userDetails.getFirstName()).thenReturn("test");
        when(userDetails.getLastName()).thenReturn("test");
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("testtoken");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("testtoken", jwtResponse.getToken());
        assertEquals(42L, jwtResponse.getId());
        assertEquals("test@test.com", jwtResponse.getUsername());
        assertTrue(jwtResponse.getAdmin());

    }

    @Test
    void testAuthenticateUser_UserIsNull() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("test123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(42L);
        when(userDetails.getUsername()).thenReturn("test@test.com");
        when(userDetails.getFirstName()).thenReturn("test");
        when(userDetails.getLastName()).thenReturn("test");
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("testtoken");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        assertEquals("testtoken", jwtResponse.getToken());
        assertEquals(42L, jwtResponse.getId());
        assertEquals("test@test.com", jwtResponse.getUsername());
        assertEquals("test", jwtResponse.getFirstName());
        assertEquals("test", jwtResponse.getLastName());

        assertFalse(jwtResponse.getAdmin());
    }

    @Test
    void testRegisterUser_Success() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("newuser@test.com");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");
        signUpRequest.setPassword("password123");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse message = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", message.getMessage());

        verify(userRepository).existsByEmail(signUpRequest.getEmail());
        verify(passwordEncoder).encode(signUpRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyTaken() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("existinguser@test.com");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse message = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already taken!", message.getMessage());

        verify(userRepository).existsByEmail(signUpRequest.getEmail());
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }
}
