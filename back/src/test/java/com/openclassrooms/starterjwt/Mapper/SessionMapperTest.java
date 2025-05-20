package com.openclassrooms.starterjwt.Mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;


    @Test
    public void testSessionToDto() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .lastName("test")
                .firstName("test")
                .email("test@test.fr")
                .password("test1234")
                .createdAt(now)
                .updatedAt(now)
                .admin(true)
                .build();

        List<User> users = Collections.singletonList(user);

        Teacher teacher = Teacher.builder()
                .id(1L)
                .updatedAt(now)
                .createdAt(now)
                .lastName("test")
                .firstName("test")
                .build();

        Session session = new Session(1L, "test", new Date(), "test", teacher, users, now, now);

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getCreatedAt(), sessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDto.getUpdatedAt());
        assertEquals(session.getDate(), sessionDto.getDate());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(1, sessionDto.getUsers().size());
        assertEquals(user.getId(), sessionDto.getUsers().get(0));
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
    }

    @Test
    public void testSessionWithNull(){
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        assertNull(sessionDto);
    }

    @Test
    public void testSessionToEntity() {
        LocalDateTime now = LocalDateTime.now();

        Long teacherId = 1L;
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .lastName("test")
                .firstName("test")
                .email("test@test.fr")
                .password("test1234")
                .createdAt(now)
                .updatedAt(now)
                .admin(true)
                .build();

        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .updatedAt(now)
                .createdAt(now)
                .lastName("teacher")
                .firstName("teacher")
                .build();

        List<Long> users = Collections.singletonList(user.getId());

        List<SessionDto> sessions = Arrays.asList(
                new SessionDto(1L, "test", new Date(), teacherId, "test", users, now, now),
                new SessionDto(2L, "test2", new Date(), 1L, "test", users, now, now)
        );

        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(userService.findById(userId)).thenReturn(user);

        List<Session> sessionList = sessionMapper.toEntity(sessions);

        assertNotNull(sessionList);
        assertEquals(2, sessionList.size());
        assertEquals(sessions.get(0).getId(), sessionList.get(0).getId());
        assertEquals("test", sessionList.get(0).getName());
        assertEquals("test2", sessionList.get(1).getName());
        assertEquals(teacher, sessionList.get(0).getTeacher());
        assertEquals(user, sessionList.get(0).getUsers().get(0));
    }

    @Test
    public void testToEntityWithNullDtoList(){
        List<Session> sessionList = sessionMapper.toEntity((List<SessionDto>) null);

        assertNull(sessionList);
    }

    @Test
    public void testSessionIsNull() {
        Session session = sessionMapper.toEntity((SessionDto) null);

        assertNull(session);
    }

    @Test
    public void testToDtoWithNullList() {
        List<SessionDto> listDto = sessionMapper.toDto((List<Session>) null);
        assertNull(listDto);
    }

    @Test
    public void testToDtoWithValidList() {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .lastName("test")
                .firstName("test")
                .email("test@test.fr")
                .password("test1234")
                .createdAt(now)
                .updatedAt(now)
                .admin(true)
                .build();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .updatedAt(now)
                .createdAt(now)
                .lastName("test")
                .firstName("test")
                .build();

        Session session = new Session(1L, "test", new Date(), "test", teacher, Collections.singletonList(user), now, now);
        List<Session> sessionList = Collections.singletonList(session);

        List<SessionDto> listSession = sessionMapper.toDto(sessionList);

        assertNotNull(listSession);
        assertEquals(sessionList.size(), listSession.size());
        assertEquals(session.getId(), listSession.get(0).getId());
        assertEquals(session.getName(), listSession.get(0).getName());
        assertEquals(session.getTeacher().getId(), listSession.get(0).getTeacher_id());
        assertEquals(session.getUsers().get(0).getId(), listSession.get(0).getUsers().get(0));
    }

    @Test
    public void testSetTeacherWithNullTeacherId() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setTeacher_id(null);

        Session session = sessionMapper.toEntity(sessionDto);

        assertNull(session.getTeacher());
    }

    @Test
    public void testSessionMapperWithNullUsers() {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setUsers(null);

        Session session = sessionMapper.toEntity(sessionDto);

        assertTrue(session.getUsers().isEmpty());
    }
}
