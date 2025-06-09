package com.openclassrooms.starterjwt.integration.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthTokenFilterIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void givenNoJwt_whenRequest_thenNoAuthenticationSet() throws Exception {
        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenInvalidJwt_whenRequest_thenNoAuthenticationSet() throws Exception {
        String token = "invalidtoken";

        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        mockMvc.perform(get("/api/teacher/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenExceptionInFilter_thenLogsErrorAndContinues() throws Exception {
        String token = "errorjwt";

        when(jwtUtils.validateJwtToken(token)).thenThrow(new RuntimeException("Error in validation"));

        mockMvc.perform(get("/api/teacher/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }
}
