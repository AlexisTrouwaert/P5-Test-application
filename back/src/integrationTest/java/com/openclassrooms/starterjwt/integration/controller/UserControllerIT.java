package com.openclassrooms.starterjwt.integration.controller;

import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");
        testUser.setPassword("password");
        testUser.setFirstName("Test");
        testUser.setLastName("Test");

        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@test.com");
        testUserDto.setFirstName("Test");
        testUserDto.setLastName("Test");
    }

    private String getJwtToken() throws Exception {
        String loginJson = "{\n" +
                "  \"email\": \"yoga@studio.com\",\n" +
                "  \"password\": \"test!1234\"\n" +
                "}";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.read(response, "$.token");
    }

    @Test
    void findById_ShouldReturnUserDto_WhenUserExists() throws Exception {
        when(userService.findById(1L)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        String token = getJwtToken();

        mockMvc.perform(get("/api/user/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserDto.getId()))
                .andExpect(jsonPath("$.email").value(testUserDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testUserDto.getFirstName()));
    }

    @Test
    void findById_ShouldReturn404_WhenUserNotFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);

        String token = getJwtToken();

        mockMvc.perform(get("/api/user/999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_ShouldReturn400_WhenIdIsInvalid() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(get("/api/user/invalid-id")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_ShouldReturnUnauthorized_WhenUserDeletesAnotherUser() throws Exception {
        when(userService.findById(1L)).thenReturn(testUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("other@example.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList())
        );

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).delete(anyLong());
    }

    @Test
    void deleteUser_ShouldReturn404_WhenUserNotFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);

        String token = getJwtToken();

        mockMvc.perform(delete("/api/user/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(userService, never()).delete(anyLong());
    }

    @Test
    void deleteUser_ShouldReturn400_WhenIdIsInvalid() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(delete("/api/user/invalid-id")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());

        verify(userService, never()).delete(anyLong());
    }
}
