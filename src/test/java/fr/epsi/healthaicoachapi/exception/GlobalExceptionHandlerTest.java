package fr.epsi.healthaicoachapi.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");
    }

    @Test
    @DisplayName("Should return 404 for ResourceNotFoundException")
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User", 1L);

        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
        assertEquals("User with id 1 not found", response.getBody().getMessage());
        assertEquals("Resource not found", response.getBody().getError());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    @DisplayName("Should return 403 for UnauthorizedAccessException")
    void testHandleUnauthorizedAccessException() {
        UnauthorizedAccessException ex = new UnauthorizedAccessException("Access denied to this resource");

        ResponseEntity<ErrorResponse> response = handler.handleUnauthorizedAccessException(ex, webRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
        assertEquals("Access denied to this resource", response.getBody().getMessage());
        assertEquals("Access denied", response.getBody().getError());
    }

    @Test
    @DisplayName("Should return 400 for RuntimeException")
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Email already exists");

        ResponseEntity<ErrorResponse> response = handler.handleRuntimeException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
        assertEquals("Email already exists", response.getBody().getMessage());
        assertEquals("Invalid request", response.getBody().getError());
    }

    @Test
    @DisplayName("Should return 401 for AuthenticationException")
    void testHandleAuthenticationException() {
        AuthenticationException ex = mock(AuthenticationException.class);
        when(ex.getMessage()).thenReturn("Bad credentials");

        ResponseEntity<ErrorResponse> response = handler.handleAuthenticationException(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getBody().getStatus());
        assertEquals("Authentication failed", response.getBody().getMessage());
        assertEquals("Bad credentials", response.getBody().getError());
    }

    @Test
    @DisplayName("Should return 422 for MethodArgumentNotValidException")
    void testHandleValidationException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        ObjectError objectError = new ObjectError("field", "Food name is required");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(objectError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, webRequest);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getBody().getStatus());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("Food name is required", response.getBody().getError());
    }

    @Test
    @DisplayName("Should return 500 for unexpected Exception")
    void testHandleGlobalException() {
        Exception ex = new Exception("Unexpected database error");

        ResponseEntity<ErrorResponse> response = handler.handleGlobalException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().getStatus());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertEquals("Unexpected database error", response.getBody().getError());
    }

    @Test
    @DisplayName("Should strip 'uri=' prefix from request path")
    void testPathFormatting() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Exercise", 5L);

        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFoundException(ex, webRequest);

        assertEquals("/api/test", response.getBody().getPath());
        assertFalse(response.getBody().getPath().startsWith("uri="));
    }

    @Test
    @DisplayName("Should include a non-null timestamp in error response")
    void testTimestampIsSet() {
        RuntimeException ex = new RuntimeException("Some error");

        ResponseEntity<ErrorResponse> response = handler.handleRuntimeException(ex, webRequest);

        assertNotNull(response.getBody().getTimestamp());
    }
}
