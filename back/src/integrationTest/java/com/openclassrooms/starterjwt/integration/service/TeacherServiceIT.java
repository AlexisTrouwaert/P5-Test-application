package com.openclassrooms.starterjwt.integration.service;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherServiceIT {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test");
        teacher = teacherRepository.save(teacher);
    }

    @Test
    void testFindAll() {
        List<Teacher> teachers = teacherService.findAll();
        assertNotNull(teachers);
        assertEquals(3, teachers.size());
        assertEquals("test", teachers.get(2).getFirstName());
    }

    @Test
    void testFindById_existingId() {
        Teacher found = teacherService.findById(teacher.getId());
        assertNotNull(found);
        assertEquals("test", found.getFirstName());
    }

    @Test
    void testFindById_nonExistingId() {
        Teacher found = teacherService.findById(999L);
        assertNull(found);
    }
}
