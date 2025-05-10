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
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.fr");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("@Test1234");
        user.setAdmin(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, user.getId());
        assertEquals("test@test.fr", user.getEmail());
    }

    @Test
    public void testUserBuilder(){
        User user = User.builder()
                .email("test1@test.fr")
                .lastName("test1")
                .firstName("test2")
                .password("@test1234")
                .admin(true)
                .build();

        assertNotNull(user);
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
    public void testUserValidation_InvalidMail(){
        User user = new User("invalid-mail", "test", "test", "@test1234", true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserValidation_ExcedeMaxSize(){
        User user = new User("t".repeat(51), "test", "test", "@test1234", true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserEqualsHashCode() {
        User user1 = new User("test@test.fr", "test", "test", "@test1234", true);
        User user2 = new User("test@test.fr", "test", "test", "@test1234", true);
        user1.setId(1L);
        user2.setId(1L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void UserToString() {
        User user = new User("test@test.fr", "test", "test", "@test1234", true);
        user.setId(1L);

        String expectedToString = "User(id=1, email=test@test.fr, lastName=test, firstName=test, password=@test1234, admin=true, createdAt=null, updatedAt=null)";
        assertEquals(expectedToString, user.toString());
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
