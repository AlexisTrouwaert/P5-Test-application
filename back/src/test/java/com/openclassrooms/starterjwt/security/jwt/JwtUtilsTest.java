package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import javax.crypto.spec.SecretKeySpec;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUtilsTest {

    private void injectSecret(JwtUtils jwtUtils, String secret) throws Exception {
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, secret);
    }

    @Test
    public void testGenerateJwtToken() throws Exception {

        String testSecret = "secret-key";
        int testExpiration = 3600000;

        JwtUtils jwtUtils = new JwtUtils();

        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, testSecret);

        Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtils, testExpiration);

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = Jwts.parser()
                .setSigningKey(testSecret)
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testuser", claims.getSubject());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    public void testGetUserNameFromJwtToken() throws Exception {

        String testSecret = "secret-key";
        int expirationMs = 3600000;

        String expectedUsername = "testuser";
        String token = Jwts.builder()
                .setSubject(expectedUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, testSecret)
                .compact();

        JwtUtils jwtUtils = new JwtUtils();

        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, testSecret);

        String actualUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testValidateJwtToken_validToken() throws Exception {
        String secret = "secret-key";
        JwtUtils jwtUtils = new JwtUtils();
        injectSecret(jwtUtils, secret);

        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_invalidSignature() throws Exception {
        String secret = "secret-key";
        String wrongSecret = "wrong-secret-key";
        JwtUtils jwtUtils = new JwtUtils();
        injectSecret(jwtUtils, secret);

        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512, wrongSecret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_expiredToken() throws Exception {
        String secret = "secret-key";
        JwtUtils jwtUtils = new JwtUtils();
        injectSecret(jwtUtils, secret);

        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // déjà expiré
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_malformedToken() throws Exception {
        String secret = "secret-key";
        JwtUtils jwtUtils = new JwtUtils();
        injectSecret(jwtUtils, secret);

        String malformedToken = "not-a-valid-jwt.token.structure";

        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    public void testValidateJwtToken_unsupportedToken() throws Exception {
        String secret = "secret-key";
        JwtUtils jwtUtils = new JwtUtils();
        injectSecret(jwtUtils, secret);

        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"none\"}".getBytes());
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"sub\":\"testuser\"}".getBytes());
        String unsupportedToken = header + "." + payload + ".";

        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }

    @Test
    public void testValidateJwtToken_illegalArgument() throws Exception {
        String secret = "secret-key";
        JwtUtils jwtUtils = new JwtUtils();
        injectSecret(jwtUtils, secret);

        String emptyToken = "";

        assertFalse(jwtUtils.validateJwtToken(emptyToken));
    }
}
