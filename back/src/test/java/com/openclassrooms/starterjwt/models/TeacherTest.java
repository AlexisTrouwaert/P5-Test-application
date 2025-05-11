package com.openclassrooms.starterjwt.models;

import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherTest {

    private Validator validator;

    public TeacherTest() {
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void TeacherGetterSetter() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = new Teacher();
        teacher.setFirstName("test1");
        teacher.setLastName("test2");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);
        teacher.setId(1L);

        assertEquals(1L, teacher.getId());
        assertEquals("test1", teacher.getFirstName());
        assertEquals("test2", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    public void testTeacherBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertNotNull(teacher);
        assertEquals("test", teacher.getFirstName());
        assertEquals("test", teacher.getLastName());
        assertEquals(1L, teacher.getId());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    public void testTeacherToString() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .createdAt(now)
                .updatedAt(now)
                .build();

        String expectedToString = "Teacher(id=1, lastName=test, firstName=test, createdAt=" + now + ", updatedAt=" + now + ")";
        assertEquals(expectedToString, teacher.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Teacher teacher3 = Teacher.builder()
                .id(2L)
                .firstName("test")
                .lastName("test")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Teacher teacherNullId = new Teacher();
        Teacher teacherNullId2 = new Teacher();

        assertEquals(teacher, teacher2);
        assertEquals(teacher.hashCode(), teacher2.hashCode());

        assertNotEquals(teacher, teacher3);
        assertNotEquals(teacher, teacherNullId);
        assertNotEquals(teacher.hashCode(), teacherNullId.hashCode());
        assertNotEquals(teacher.hashCode(), teacher3.hashCode());

        assertNotEquals(null, teacher);

        assertNotEquals(new Object(), teacher);

        assertEquals(teacherNullId2, teacherNullId);
        assertEquals(teacherNullId2.hashCode(), teacherNullId.hashCode());
    }

}
