package fr.epsi.healthaicoachapi.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedAccessExceptionTest {

    @Test
    @DisplayName("UnauthorizedAccessException - Should store custom message")
    void testCustomMessageConstructor() {
        UnauthorizedAccessException ex = new UnauthorizedAccessException("You cannot access this user's data");

        assertEquals("You cannot access this user's data", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    @DisplayName("UnauthorizedAccessException - Should use default message when no args")
    void testDefaultConstructor() {
        UnauthorizedAccessException ex = new UnauthorizedAccessException();

        assertEquals("You are not authorized to access this resource", ex.getMessage());
    }
}
