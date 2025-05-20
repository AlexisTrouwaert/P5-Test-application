package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import static org.junit.jupiter.api.Assertions.*;

public class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_SessionFound() {
        Long id = 1L;
        Session session = new Session();
        SessionDto dto = new SessionDto();

        when(sessionService.getById(id)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        ResponseEntity<?> response = sessionController.findById(String.valueOf(id));

        assertEquals(OK, response.getStatusCode());
        assertEquals(dto, response.getBody());

        verify(sessionService).getById(id);
        verify(sessionMapper).toDto(session);
    }

    @Test
    public void testFindById_SessionNotFound() {
        Long id = 1L;

        when(sessionService.getById(id)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById(String.valueOf(id));

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(sessionService).getById(id);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    public void testFindById_InvalidIdFormat() {
        String invalidId = "abc";

        ResponseEntity<?> response = sessionController.findById(invalidId);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verifyNoInteractions(sessionService);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    public void testFindAll_ReturnsSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());

        List<SessionDto> sessionDtos = new ArrayList<>();
        sessionDtos.add(new SessionDto());
        sessionDtos.add(new SessionDto());

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());

        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
    }

    @Test
    public void testCreate_Success() {
        LocalDateTime now = LocalDateTime.now();

        SessionDto inputDto = new SessionDto();
        inputDto.setId(1L);
        inputDto.setCreatedAt(now);
        inputDto.setUpdatedAt(now);
        inputDto.setDate(new Date());
        inputDto.setName("Test");
        inputDto.setDescription("Test");
        inputDto.setTeacher_id(1L);
        inputDto.setUsers(List.of(1L, 2L));

        Session entity = new Session();
        entity.setId(1L);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setDate(new Date());
        entity.setName("Test");
        entity.setDescription("Test");

        Session savedEntity = new Session();
        savedEntity.setId(1L);
        savedEntity.setCreatedAt(now);
        savedEntity.setUpdatedAt(now);
        savedEntity.setDate(new Date());
        savedEntity.setName("Test");
        savedEntity.setDescription("Test");

        SessionDto outputDto = new SessionDto();
        outputDto.setId(1L);
        outputDto.setCreatedAt(now);
        outputDto.setUpdatedAt(now);
        outputDto.setDate(new Date());
        outputDto.setName("Test");
        outputDto.setDescription("Test");
        outputDto.setTeacher_id(1L);
        outputDto.setUsers(List.of(1L, 2L));

        when(sessionMapper.toEntity(inputDto)).thenReturn(entity);
        when(sessionService.create(entity)).thenReturn(savedEntity);
        when(sessionMapper.toDto(savedEntity)).thenReturn(outputDto);

        ResponseEntity<?> response = sessionController.create(inputDto);

        assertEquals(OK, response.getStatusCode());
        assertEquals(outputDto, response.getBody());

        verify(sessionMapper).toEntity(inputDto);
        verify(sessionService).create(entity);
        verify(sessionMapper).toDto(savedEntity);
    }

    @Test
    public void testUpdate_Success() {
        String id = "1";
        Long idLong = 1L;

        SessionDto inputDto = new SessionDto();
        inputDto.setName("Updated Title");

        Session entity = new Session();
        entity.setName("Updated Title");

        Session updatedEntity = new Session();
        updatedEntity.setId(idLong);
        updatedEntity.setName("Updated Title");

        SessionDto outputDto = new SessionDto();
        outputDto.setId(idLong);
        outputDto.setName("Updated Title");

        when(sessionMapper.toEntity(inputDto)).thenReturn(entity);
        when(sessionService.update(idLong, entity)).thenReturn(updatedEntity);
        when(sessionMapper.toDto(updatedEntity)).thenReturn(outputDto);

        ResponseEntity<?> response = sessionController.update(id, inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(outputDto, response.getBody());

        verify(sessionMapper).toEntity(inputDto);
        verify(sessionService).update(idLong, entity);
        verify(sessionMapper).toDto(updatedEntity);
    }

    @Test
    public void testUpdate_InvalidIdId() {
        String invalidId = "test";
        SessionDto inputDto = new SessionDto();

        ResponseEntity<?> response = sessionController.update(invalidId, inputDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verifyNoInteractions(sessionService);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    public void testDelete_Success() {
        String id = "1";
        Long idLong = 1L;

        Session session = new Session();
        session.setId(idLong);

        when(sessionService.getById(idLong)).thenReturn(session);
        doNothing().when(sessionService).delete(idLong);

        ResponseEntity<?> response = sessionController.save(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).getById(idLong);
        verify(sessionService).delete(idLong);
    }

    @Test
    public void testDelete_NotFound() {
        String id = "1";
        Long idLong = 1L;

        when(sessionService.getById(idLong)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService).getById(idLong);
        verify(sessionService, never()).delete(any());
    }

    @Test
    public void testDelete_InvalidId() {
        String invalidId = "abc";

        ResponseEntity<?> response = sessionController.save(invalidId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }

    @Test
    public void testParticipate_Success() {
        String sessionId = "1";
        String userId = "2";

        doNothing().when(sessionService).participate(1L, 2L);

        ResponseEntity<?> response = sessionController.participate(sessionId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).participate(1L, 2L);
    }

    @Test
    public void testParticipate_InvalidId() {
        String invalidSessionId = "abc";
        String validUserId = "2";

        ResponseEntity<?> response = sessionController.participate(invalidSessionId, validUserId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }

    @Test
    public void testParticipate_InvalidUserId() {
        String validSessionId = "1";
        String invalidUserId = "xyz";

        ResponseEntity<?> response = sessionController.participate(validSessionId, invalidUserId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }

    @Test
    public void testNoLongerParticipate_Success() {
        String sessionId = "1";
        String userId = "2";

        doNothing().when(sessionService).noLongerParticipate(1L, 2L);

        ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).noLongerParticipate(1L, 2L);
    }

    @Test
    public void testNoLongerParticipate_InvalidSessionId() {
        String invalidSessionId = "abc";
        String userId = "2";

        ResponseEntity<?> response = sessionController.noLongerParticipate(invalidSessionId, userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }

    @Test
    public void testNoLongerParticipate_InvalidUserId() {
        String sessionId = "1";
        String invalidUserId = "xyz";

        ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId, invalidUserId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }

}
