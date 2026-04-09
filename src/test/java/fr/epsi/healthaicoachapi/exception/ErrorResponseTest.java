package fr.epsi.healthaicoachapi.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    @DisplayName("ErrorResponse - Should build with all fields using builder")
    void testBuilder_AllFields() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Resource not found")
                .error("Not Found")
                .timestamp(now)
                .path("/api/users/99")
                .build();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Resource not found", response.getMessage());
        assertEquals("Not Found", response.getError());
        assertEquals(now, response.getTimestamp());
        assertEquals("/api/users/99", response.getPath());
    }

    @Test
    @DisplayName("ErrorResponse - Should set and get all fields via setters")
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse();

        response.setStatus(HttpStatus.FORBIDDEN);
        response.setMessage("Access denied");
        response.setError("Forbidden");
        response.setTimestamp(now);
        response.setPath("/api/nutrition/1");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatus());
        assertEquals("Access denied", response.getMessage());
        assertEquals("Forbidden", response.getError());
        assertEquals(now, response.getTimestamp());
        assertEquals("/api/nutrition/1", response.getPath());
    }

    @Test
    @DisplayName("ErrorResponse - Should create with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid input",
                "Bad Request", now, "/api/workouts");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Invalid input", response.getMessage());
        assertEquals("Bad Request", response.getError());
        assertEquals(now, response.getTimestamp());
        assertEquals("/api/workouts", response.getPath());
    }

    @Test
    @DisplayName("ErrorResponse.of() - Should create response with auto timestamp")
    void testStaticOfFactory() {
        LocalDateTime before = LocalDateTime.now();

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.NOT_FOUND,
                "User not found",
                "Resource not found",
                "/api/users/99"
        );

        LocalDateTime after = LocalDateTime.now();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("User not found", response.getMessage());
        assertEquals("Resource not found", response.getError());
        assertEquals("/api/users/99", response.getPath());
        assertNotNull(response.getTimestamp());
        assertFalse(response.getTimestamp().isBefore(before));
        assertFalse(response.getTimestamp().isAfter(after));
    }

    @Test
    @DisplayName("ErrorResponse - Default constructor should have null fields")
    void testDefaultConstructor_NullFields() {
        ErrorResponse response = new ErrorResponse();

        assertNull(response.getStatus());
        assertNull(response.getMessage());
        assertNull(response.getError());
        assertNull(response.getTimestamp());
        assertNull(response.getPath());
    }
}
