package com.openclassrooms.starterjwt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class SessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

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
    void getSessionById_ShouldReturnOk_WhenSessionExists() throws Exception {
        Session session = new Session();
        session.setName("Test session");
        session.setId(0L);
        session.setDescription("Test description");
        session.setTeacher(null);
        session.setDate(new Date());
        session = sessionRepository.save(session);

        String token = getJwtToken();

        mockMvc.perform(get("/api/session/" + session.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test session"));
    }

    @Test
    void getSessionById_ShouldReturn404_WhenNotFound() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(get("/api/session/99999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSessionById_ShouldReturn400_WhenInvalidId() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(get("/api/session/abc")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSessions_ShouldReturnOk_WithList() throws Exception {
        sessionRepository.deleteAll();
        Session session1 = new Session();
        session1.setName("Session 1");
        session1.setId(0L);
        session1.setDescription("Test description");
        session1.setTeacher(null);
        session1.setDate(new Date());
        sessionRepository.save(session1);

        Session session2 = new Session();
        session2.setName("Session 2");
        session2.setId(1L);
        session2.setDescription("Test description2");
        session2.setTeacher(null);
        session2.setDate(new Date());
        sessionRepository.save(session2);

        String token = getJwtToken();

        mockMvc.perform(get("/api/session")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", not(emptyOrNullString())));
    }

    @Test
    void createSession_ShouldReturnOk_WhenValid() throws Exception {
        SessionDto dto = new SessionDto();
        dto.setName("New session");
        dto.setId(1L);
        dto.setDescription("Test description2");
        dto.setTeacher_id(1L);
        dto.setDate(new Date());

        String token = getJwtToken();

        mockMvc.perform(post("/api/session")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New session"));
    }

    @Test
    void createSession_ShouldReturn400_WhenInvalid() throws Exception {
        SessionDto dto = new SessionDto();

        String token = getJwtToken();

        mockMvc.perform(post("/api/session")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSession_ShouldReturnOk_WhenValid() throws Exception {
        Session session = new Session();
        session.setName("Old name");
        session.setId(1L);
        session.setDescription("Test description2");
        session.setTeacher(null);
        session.setDate(new Date());
        session = sessionRepository.save(session);

        SessionDto dto = new SessionDto();
        dto.setName("Updated name");
        dto.setDate(new Date());
        dto.setDescription("Test");
        dto.setTeacher_id(1L);

        String token = getJwtToken();

        mockMvc.perform(put("/api/session/" + session.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated name"));
    }

    @Test
    void updateSession_ShouldReturn400_WhenInvalidId() throws Exception {
        SessionDto dto = new SessionDto();
        dto.setName("Updated name");

        String token = getJwtToken();

        mockMvc.perform(put("/api/session/abc")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSession_ShouldReturnOk_WhenExists() throws Exception {
        Session session = new Session();
        session.setName("To delete");
        session.setId(1L);
        session.setDescription("Test description2");
        session.setTeacher(null);
        session.setDate(new Date());
        session = sessionRepository.save(session);

        String token = getJwtToken();

        mockMvc.perform(delete("/api/session/" + session.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSession_ShouldReturnNotFound_WhenNotExists() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(delete("/api/session/999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSession_ShouldReturn400_WhenInvalidId() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(delete("/api/session/abc")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_ShouldReturn400_WhenInvalidId() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(post("/api/session/abc/participate/123")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/session/1/participate/abc")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}
