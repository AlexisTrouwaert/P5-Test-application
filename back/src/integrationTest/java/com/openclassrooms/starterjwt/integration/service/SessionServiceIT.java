package com.openclassrooms.starterjwt.integration.service;

import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
public class SessionServiceIT {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);

        session = new Session();
        session.setName("Test Session");
        session.setDate(new Date());
        session.setDescription("A test session");
        session.setTeacher(null);
        session = sessionRepository.save(session);
    }

    @Test
    void testCreateSession() {
        Session newSession = new Session();
        newSession.setName("New Session");
        newSession.setDescription("Desc");
        newSession.setDate(new Date());
        newSession.setTeacher(null);

        Session saved = sessionService.create(newSession);

        assertNotNull(saved.getId());
        assertEquals("New Session", saved.getName());
    }

    @Test
    void testDeleteSession() {
        sessionService.delete(session.getId());
        assertFalse(sessionRepository.findById(session.getId()).isPresent());
    }

    @Test
    void testFindAllSessions() {
        List<Session> sessions = sessionService.findAll();
        assertTrue(sessions.size() >= 1);
    }

    @Test
    void testGetById() {
        Session found = sessionService.getById(session.getId());
        assertNotNull(found);
        assertEquals(session.getId(), found.getId());
    }

    @Test
    void testUpdateSession() {
        session.setDescription("Updated");
        Session updated = sessionService.update(session.getId(), session);
        assertEquals("Updated", updated.getDescription());
    }

    @Test
    void testParticipate() {
        sessionService.participate(session.getId(), user.getId());

        Session updated = sessionRepository.findById(session.getId()).orElseThrow();
        assertTrue(updated.getUsers().stream().anyMatch(u -> u.getId().equals(user.getId())));
    }

    @Test
    void testParticipate_InvalidSessionOrUser_Throws() {
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(999L, user.getId());
        });

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), 999L);
        });
    }

    @Test
    void testNoLongerParticipate_SessionNotFound_Throws() {
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(999L, user.getId());
        });
    }

}
