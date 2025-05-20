package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthEntryPointJwtTest {

    @Test
    public void testCommence_shouldReturnUnauthorizedJson() throws Exception {
        // Arrange
        AuthEntryPointJwt entryPoint = new AuthEntryPointJwt();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);

        when(request.getServletPath()).thenReturn("/api/test");
        when(authException.getMessage()).thenReturn("Invalid JWT token");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) {
                byteArrayOutputStream.write(b);
            }
        };

        when(response.getOutputStream()).thenReturn(servletOutputStream);

        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> json = mapper.readValue(byteArrayOutputStream.toByteArray(), Map.class);

        assertEquals(401, json.get("status"));
        assertEquals("Unauthorized", json.get("error"));
        assertEquals("Invalid JWT token", json.get("message"));
        assertEquals("/api/test", json.get("path"));
    }

}
