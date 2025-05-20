package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherControllerTest {

    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_Success() {
        String id = "1";
        Long idLong = 1L;

        Teacher teacher = new Teacher();
        teacher.setId(idLong);
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(idLong);

        when(teacherService.findById(idLong)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto, response.getBody());
        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    public void testFindById_NotFound() {
        String id = "1";
        Long idLong = 1L;

        when(teacherService.findById(idLong)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(teacherService).findById(idLong);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    public void testFindById_InvalidId() {
        String invalidId = "test";

        ResponseEntity<?> response = teacherController.findById(invalidId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(teacherService);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    public void testFindAll_Success() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("test");
        teacher.setFirstName("test");

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("test");
        teacherDto.setFirstName("test");

        List<Teacher> teachers = List.of(teacher);
        List<TeacherDto> teacherDtos = List.of(teacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto((List<Teacher>) any())).thenReturn(teacherDtos); // désambiguïsation ici

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDtos, response.getBody());

        verify(teacherService).findAll();
        verify(teacherMapper).toDto((List<Teacher>) any());
    }
}
