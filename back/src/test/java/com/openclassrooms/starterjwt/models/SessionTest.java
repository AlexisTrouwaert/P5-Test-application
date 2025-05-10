package com.openclassrooms.starterjwt.models;

import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    private Validator validator;

    public SessionTest() {
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void SessionGetterSetter() {
        LocalDateTime now = LocalDateTime.now();

        User user1 = new User("test1@test.fr", "test1", "test1", "test1", true);
        User user2 = new User("test2@test.fr", "test2", "test2", "test2", true);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Teacher teacher = new Teacher();
        teacher.setLastName("test");
        teacher.setFirstName("test");

        Session session = new Session();
        session.setId(1L);
        session.setDate(new Date());
        session.setName("test");
        session.setDescription("test");
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        session.setTeacher(teacher);
        session.setUsers(users);

        assertEquals(1L, session.getId());
        assertEquals("test", session.getName());
        assertNotNull(session.getDate());
        assertEquals("test", session.getDescription());
        assertEquals(now, session.getUpdatedAt());
        assertEquals(now, session.getCreatedAt());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
    }

    @Test
    public void SessionValid_valid() {
        LocalDateTime now = LocalDateTime.now();

        User user1 = new User("test1@test.fr", "test1", "test1", "test1", true);
        User user2 = new User("test2@test.fr", "test2", "test2", "test2", true);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Teacher teacher = new Teacher();
        teacher.setLastName("test");
        teacher.setFirstName("test");

        Session session = new Session();
        session.setId(1L);
        session.setDate(new Date());
        session.setName("test");
        session.setDescription("test");
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        session.setTeacher(teacher);
        session.setUsers(users);

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void SessionValid_invalidName() {
        LocalDateTime now = LocalDateTime.now();

        User user1 = new User("test1@test.fr", "test1", "test1", "test1", true);
        User user2 = new User("test2@test.fr", "test2", "test2", "test2", true);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Teacher teacher = new Teacher();
        teacher.setLastName("test");
        teacher.setFirstName("test");

        Session session = new Session();
        session.setId(1L);
        session.setDate(new Date());
        session.setName("");
        session.setDescription("test");
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        session.setTeacher(teacher);
        session.setUsers(users);

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void SessionValid_invalidDescription() {
        LocalDateTime now = LocalDateTime.now();

        User user1 = new User("test1@test.fr", "test1", "test1", "test1", true);
        User user2 = new User("test2@test.fr", "test2", "test2", "test2", true);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Teacher teacher = new Teacher();
        teacher.setLastName("test");
        teacher.setFirstName("test");

        Session session = new Session();
        session.setId(1L);
        session.setDate(new Date());
        session.setName("test");
        session.setDescription(null);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        session.setTeacher(teacher);
        session.setUsers(users);

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testSessionBuilder() {
        Date sessionDate = new Date();
        Session session = Session.builder()
                .name("test")
                .date(sessionDate)
                .description("test")
                .teacher(null)
                .users(new ArrayList<>())
                .build();

        assertNotNull(session);
        assertEquals("test", session.getName());
        assertEquals(sessionDate, session.getDate());
        assertEquals("test", session.getDescription());
    }

    @Test
    public void testSessionToString() {
        Date sessionDate = new Date();
        Session session = Session.builder()
                .name("test")
                .date(sessionDate)
                .description("test")
                .teacher(null)
                .users(new ArrayList<>())
                .build();
        session.setId(1L);

        String expectedToString = "Session(id=1, name=test, date=" + sessionDate + ", description=test, teacher=null, users=[], createdAt=null, updatedAt=null)";
        assertEquals(expectedToString, session.toString());
    }

    @Test
    public void testSessionEqualsHashCode() {
        Date sessionDate = new Date();
        Session session1 = Session.builder()
                .name("test")
                .date(sessionDate)
                .description("test")
                .teacher(null)
                .users(new ArrayList<>())
                .build();
        session1.setId(1L);

        Session session2 = Session.builder()
                .name("test")
                .date(sessionDate)
                .description("test")
                .teacher(null)
                .users(new ArrayList<>())
                .build();
        session2.setId(1L);

        Session session3 = Session.builder()
                .name("test")
                .date(sessionDate)
                .description("test")
                .teacher(null)
                .users(new ArrayList<>())
                .build();
        session3.setId(2L);

        Session sessionNullId1 = new Session();
        Session sessionNullId2 = new Session();

        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());

        assertNotEquals(session1, session3);
        assertNotEquals(session1.hashCode(), session3.hashCode());

        assertNotEquals(null, session1);

        assertNotEquals(new Object(), session1);

        assertNotEquals(session1, sessionNullId1);
        assertNotEquals(sessionNullId1, session1);

        assertEquals(sessionNullId1, sessionNullId2);
        assertEquals(sessionNullId1.hashCode(), sessionNullId2.hashCode());
    }

    @Test
    void testSessionValidation_NameExcededLength() {
        Session session = Session.builder()
                .name("a".repeat(51))
                .description("Valid description")
                .date(new Date())
                .build();

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSessionValidation_DescriptionExcededLength() {
        Session session = Session.builder()
                .name("Valid Name")
                .description("a".repeat(2501))
                .date(new Date())
                .build();

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertFalse(violations.isEmpty());
    }

}
