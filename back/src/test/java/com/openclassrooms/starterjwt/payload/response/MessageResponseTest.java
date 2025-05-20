package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageResponseTest {

    @Test
    public void testConstructorAndGetter() {
        String msg = "Hello World!";
        MessageResponse response = new MessageResponse(msg);

        assertEquals(msg, response.getMessage());
    }

    @Test
    public void testSetterAndGetter() {
        MessageResponse response = new MessageResponse("Initial Message");

        response.setMessage("Updated Message");
        assertEquals("Updated Message", response.getMessage());
    }
}
