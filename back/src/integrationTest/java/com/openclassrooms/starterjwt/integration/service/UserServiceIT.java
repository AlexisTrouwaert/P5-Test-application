package com.openclassrooms.starterjwt.integration.service;


import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("test");
        user.setLastName("test");
        user.setPassword("password123");
        user.setAdmin(false);

        savedUser = userRepository.save(user);
    }

    @Test
    void testFindById_existingUser() {
        User user = userService.findById(savedUser.getId());
        assertNotNull(user);
        assertEquals("test@test.com", user.getEmail());
    }

    @Test
    void testFindById_nonExistingUser() {
        User user = userService.findById(999L);
        assertNull(user);
    }

    @Test
    void testDelete_existingUser() {
        userService.delete(savedUser.getId());
        assertFalse(userRepository.findById(savedUser.getId()).isPresent());
    }
}
