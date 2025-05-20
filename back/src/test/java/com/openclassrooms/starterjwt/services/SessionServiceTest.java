package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindAll() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();

        Teacher teacher = Teacher.builder().id(1L).firstName("John").lastName("Doe").createdAt(now).updatedAt(now).build();
        List<User> users = List.of(
                User.builder()
                        .id(1L)
                        .email("test@test.fr")
                        .firstName("test")
                        .lastName("test")
                        .password("test1234")
                        .createdAt(now)
                        .updatedAt(now)
                        .admin(false)
                        .build()
        );

        List<Session> sessions = List.of(
                new Session(1L, "test1", date, "test1", teacher, users, now, now),
                new Session(2L, "test2", date, "test2", teacher, users, now, now)
        );

        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act
        List<Session> result = sessionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).getName());
        assertEquals("test2", result.get(1).getName());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteSessionById() {
        Long sessionId = 1L;

        sessionService.delete(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    public void testFindById() {
        Long sessionId = 1L;

        sessionService.getById(sessionId);

        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    public void testCreateSession() {
        LocalDateTime now = LocalDateTime.now();
        Session session = Session.builder()
                .id(1L)
                .name("test")
                .description("test")
                .date(new Date())
                .users(List.of())
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals("test", result.getName());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testUpdateSession() {

        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();

        Session Update = Session.builder()
                .name("Updated Session")
                .description("Updated description")
                .date(date)
                .users(List.of())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Long sessionId = 1L;

        Session savedSession = Session.builder()
                .id(sessionId)
                .name("Updated Session")
                .description("Updated description")
                .date(date)
                .users(List.of())
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(sessionRepository.save(any(Session.class))).thenReturn(savedSession);

        // Act
        Session result = sessionService.update(sessionId, Update);

        // Assert
        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        assertEquals("Updated Session", result.getName());
        verify(sessionRepository, times(1)).save(Update);
    }

    @Test
    public void testParticipateSessionNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;

        User mockUser = new User();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
    }

    @Test
    public void testParticipateUserNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;

        Session mockSession = new Session();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
    }

    @Test
    public void testAlreadyParticipate() {
        Long sessionId = 1L;
        Long userId = 2L;

        User existingUser = new User();
        existingUser.setId(userId);

        Session session = new Session();
        session.setUsers(new ArrayList<>(List.of(existingUser)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
    }

    @Test
    void testParticipateSuccess() {
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Session session = new Session();
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.participate(sessionId, userId);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void testNoLongerParticipateSessionNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });

        verify(sessionRepository).findById(sessionId);
        verifyNoMoreInteractions(sessionRepository, userRepository);
    }

    @Test
    void testNoLongerParticipateUserNotInSession() {
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(List.of());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });

        verify(sessionRepository).findById(sessionId);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testNoLongerParticipateSuccess() {
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>(List.of(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.noLongerParticipate(sessionId, userId);

        assertTrue(session.getUsers().isEmpty());

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(session);
    }
}
