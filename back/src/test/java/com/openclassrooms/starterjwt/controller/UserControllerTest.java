package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testFindById_UserFound() {
        Long id = 1L;
        String stringId = "1";

        User user = new User();
        user.setId(id);
        user.setFirstName("test");

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setFirstName("test");

        when(userService.findById(id)).thenReturn(user);
        when(userMapper.toDto((User) any())).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById(stringId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());

        verify(userService).findById(id);
        verify(userMapper).toDto(user);
    }

    @Test
    public void testFindById_UserNotFound() {
        Long id = 1L;
        String stringId = "1";

        when(userService.findById(id)).thenReturn(null);

        ResponseEntity<?> response = userController.findById(stringId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userService).findById(id);
        verifyNoInteractions(userMapper); // toDto ne doit pas être appelé si user == null
    }

    @Test
    public void testFindById_InvalidId() {
        String invalidId = "abc";

        ResponseEntity<?> response = userController.findById(invalidId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verifyNoInteractions(userService);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testDelete_Success() {
        Long id = 1L;
        String stringId = "1";

        User user = new User();
        user.setId(id);
        user.setEmail("user@example.com");

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        when(userService.findById(id)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<?> response = userController.save(stringId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).delete(id);
    }

    @Test
    void testDelete_UserNotFound() {
        Long id = 1L;
        String stringId = "1";

        when(userService.findById(id)).thenReturn(null);

        ResponseEntity<?> response = userController.save(stringId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).delete(any());
    }

    @Test
    void testDelete_Unauthorized() {
        Long id = 1L;
        String stringId = "1";

        User user = new User();
        user.setId(id);
        user.setEmail("test@test.com");

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test2@test.com");

        when(userService.findById(id)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<?> response = userController.save(stringId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).delete(any());
    }

    @Test
    void testDelete_InvalidId() {
        String invalidId = "test";

        ResponseEntity<?> response = userController.save(invalidId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).findById(any());
        verify(userService, never()).delete(any());
    }
}
