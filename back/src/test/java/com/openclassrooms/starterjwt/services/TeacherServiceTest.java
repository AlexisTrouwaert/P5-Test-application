package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    public void testFindAll() {

        LocalDateTime now = LocalDateTime.now();
        Teacher teacher1 = Teacher.builder().id(1L).firstName("test1").lastName("test1").createdAt(now).updatedAt(now).build();
        Teacher teacher2 = Teacher.builder().id(2L).firstName("test2").lastName("test2").createdAt(now).updatedAt(now).build();
        List<Teacher> mockTeachers = Arrays.asList(teacher1, teacher2);

        when(teacherRepository.findAll()).thenReturn(mockTeachers);

        List<Teacher> result = teacherService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).getFirstName());
        assertEquals("test2", result.get(1).getFirstName());
        verify(teacherRepository, times(1)).findAll();
    }
}
