package com.example.demo.users.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {
    @Test
    public void testConstructor() {
        String message = "User not found";
        UserNotFoundException exception = new UserNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

}