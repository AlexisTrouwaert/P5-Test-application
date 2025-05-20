package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtResponseTest {

    @Test
    public void testJwtResponseConstructorAndGetters() {
        String token = "abc123token";
        Long id = 42L;
        String username = "test";
        String firstName = "test";
        String lastName = "test";
        Boolean admin = true;

        JwtResponse response = new JwtResponse(token, id, username, firstName, lastName, admin);

        assertEquals(token, response.getToken());
        assertEquals("Bearer", response.getType()); // valeur par défaut
        assertEquals(id, response.getId());
        assertEquals(username, response.getUsername());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
        assertEquals(admin, response.getAdmin());
    }

    @Test
    public void testSettersAndGetters() {
        JwtResponse response = new JwtResponse("token", 1L, "test1", "test1", "test1", false);

        response.setToken("testToken");
        response.setType("testToken");
        response.setId(2L);
        response.setUsername("test");
        response.setFirstName("test");
        response.setLastName("test");
        response.setAdmin(true);

        assertEquals("testToken", response.getToken());
        assertEquals("testToken", response.getType());
        assertEquals(2L, response.getId());
        assertEquals("test", response.getUsername());
        assertEquals("test", response.getFirstName());
        assertEquals("test", response.getLastName());
        assertTrue(response.getAdmin());
    }
}
