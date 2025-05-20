package com.openclassrooms.starterjwt.security.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserDetailsImplTest {

    @Test
    public void testBuilderAndGetters() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("Test")
                .lastName("test")
                .admin(true)
                .password("secret")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("Test", user.getFirstName());
        assertEquals("test", user.getLastName());
        assertTrue(user.getAdmin());
        assertEquals("secret", user.getPassword());
    }

    @Test
    public void testEquals_sameObject_returnsTrue() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test")
                .firstName("test")
                .lastName("test")
                .admin(true)
                .password("test")
                .build();

        assertEquals(user, user);
    }

    @Test
    public void testAuthoritiesIsEmpty() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    public void testAccountStatusMethodsReturnTrue() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testEquals() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(null, user1);
        assertNotEquals(new Object(), user1);
    }
}
