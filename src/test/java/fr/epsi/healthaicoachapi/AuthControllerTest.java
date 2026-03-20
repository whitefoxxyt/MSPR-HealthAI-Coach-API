package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.AuthController;
import fr.epsi.healthaicoachapi.dto.AuthResponse;
import fr.epsi.healthaicoachapi.dto.LoginRequest;
import fr.epsi.healthaicoachapi.dto.RegisterRequest;
import fr.epsi.healthaicoachapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("POST /auth/register - Should register a new user successfully")
    void testRegisterUser_Success() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("Password123!");
        registerRequest.setAge(25);
        registerRequest.setGender("M");
        registerRequest.setWeightKg(75.0);
        registerRequest.setHeightCm(180.0);
        registerRequest.setObjective("Perte de poids");

        AuthResponse expectedResponse = AuthResponse.builder()
                .token("jwt-token-123")
                .userId(1L)
                .email("test@example.com")
                .username("testuser")
                .role("USER")
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<AuthResponse> response = authController.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("jwt-token-123", response.getBody().getToken());
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Should throw exception when email already exists")
    void testRegisterUser_EmailExists() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("existing@example.com");
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("Password123!");

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        // When & Then
        assertThrows(RuntimeException.class, () -> authController.register(registerRequest));
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Should login with valid credentials")
    void testLoginUser_Success() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123!");

        AuthResponse expectedResponse = AuthResponse.builder()
                .token("jwt-token-456")
                .userId(1L)
                .email("test@example.com")
                .username("testuser")
                .role("USER")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
        assertEquals("jwt-token-456", response.getBody().getToken());
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Should throw exception with invalid credentials")
    void testLoginUser_InvalidCredentials() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("WrongPassword!");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid password"));

        // When & Then
        assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Should throw exception when user not found")
    void testLoginUser_UserNotFound() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("Password123!");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("User not found"));

        // When & Then
        assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
        verify(authService, times(1)).login(any(LoginRequest.class));
    }
}

