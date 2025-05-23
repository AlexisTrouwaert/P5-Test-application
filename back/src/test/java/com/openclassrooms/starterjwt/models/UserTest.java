package com.openclassrooms.starterjwt.models;

import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private Validator validator;

    public UserTest() {
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void UserSetter() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.fr");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("@Test1234");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@test.fr", user.getEmail());
        assertEquals("Test", user.getFirstName());
        assertEquals("Test", user.getLastName());
        assertEquals("@Test1234", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    public void testUserBuilder(){
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test1@test.fr")
                .lastName("test1")
                .firstName("test2")
                .password("@test1234")
                .createdAt(now)
                .updatedAt(now)
                .admin(true)
                .build();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertEquals("test1@test.fr", user.getEmail());
        assertEquals("test1", user.getLastName());
        assertEquals("test2", user.getFirstName());
        assertEquals("@test1234", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @Test
    public void testUserValidation_Valid(){

        User user = new User("test@test.fr", "test", "test", "@test1234", true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserBuilderMinimal() {
        User user = User.builder()
                .email("test@test.fr")
                .firstName("test")
                .lastName("test")
                .password("@test1234")
                .build();

        assertEquals("test@test.fr", user.getEmail());
        assertEquals("test", user.getFirstName());
        assertEquals("test", user.getLastName());
        assertEquals("@test1234", user.getPassword());
    }

    @Test
    public void testUserValidation_InvalidMail(){
        User user = new User("invalid-mail", "test", "test", "@test1234", true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserValidation_ExcedeMaxSize(){
        User user = new User("t".repeat(51), "test".repeat(51), "test".repeat(51), "@test1234".repeat(151), true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserEqualsHashCode() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "test@test.fr", "test", "test", "@test1234", true, now, now);
        User user2 = new User(1L, "test@test.fr", "test", "test", "@test1234", true, now, now);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void UserToString() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@test.fr", "test", "test", "@test1234", true, now ,now);

        String expectedToString = "User(id=1, email=test@test.fr, lastName=test, firstName=test, password=@test1234, admin=true, createdAt=" + now +", updatedAt=" + now + ")";
        assertEquals(expectedToString, user.toString());
    }

    @Test
    public void UserToStringBuilder() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test1@test.fr")
                .lastName("test1")
                .firstName("test2")
                .password("@test1234")
                .createdAt(now)
                .updatedAt(now)
                .admin(true)
                .build();

        String UserToString = user.toString();

        String expectedToString = "User(id=1, email=test1@test.fr, lastName=test1, firstName=test2, password=@test1234, admin=true, createdAt=" + now + ", updatedAt=" + now + ")";
        assertNotNull(user.toString());
        assertEquals(expectedToString, UserToString);
    }

    @Test
    void testEquals() {
        User user1 = new User().setId(1L);
        User user2 = new User().setId(1L);
        User user3 = new User().setId(2L);
        User userNullId = new User();

        assertEquals(user1, user1);
        assertEquals(user1, user2); // même id
        assertNotEquals(user1, user3); // id différent
        assertNotEquals(null, user1); // null
        assertNotEquals(new Object(), user1); // autre type
        assertNotEquals(user1, userNullId);
        assertNotEquals(userNullId, user1);

        User userNullId2 = new User();
        assertEquals(userNullId, userNullId2);
    }
}
