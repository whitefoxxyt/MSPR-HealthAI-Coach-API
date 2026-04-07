package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.UserController;
import fr.epsi.healthaicoachapi.dto.UserDTO;
import fr.epsi.healthaicoachapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    private static final String AUTH_USER_ID = "auth-user-uuid-123";

    private UserDTO testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserDTO();
        testUser.setId(1L);
        testUser.setAuthUserId(AUTH_USER_ID);
        testUser.setUsername("testuser");
        testUser.setAge(25);
        testUser.setGender("M");
        testUser.setWeightKg(75.0);
        testUser.setHeightCm(180.0);
        testUser.setObjective("Perte de poids");
    }

    @Test
    @DisplayName("GET /users/me - Should retrieve current user profile")
    void testGetCurrentUser_Success() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(AUTH_USER_ID);
        SecurityContextHolder.setContext(securityContext);
        when(userService.getUserByAuthUserId(AUTH_USER_ID)).thenReturn(testUser);

        // When
        ResponseEntity<UserDTO> response = userController.getCurrentUser();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(AUTH_USER_ID, response.getBody().getAuthUserId());
        assertEquals("testuser", response.getBody().getUsername());
        verify(userService, times(1)).getUserByAuthUserId(AUTH_USER_ID);
    }

    @Test
    @DisplayName("GET /users/{userId} - Should retrieve user by ID")
    void testGetUserById_Success() {
        // Given
        when(userService.getUserById(1L)).thenReturn(testUser);

        // When
        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(AUTH_USER_ID, response.getBody().getAuthUserId());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("PUT /users/{userId} - Should update user profile")
    void testUpdateUser_Success() {
        // Given
        UserDTO updateRequest = new UserDTO();
        updateRequest.setUsername("updateduser");
        updateRequest.setAge(26);
        updateRequest.setWeightKg(74.0);

        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(1L);
        updatedUser.setAuthUserId(AUTH_USER_ID);
        updatedUser.setUsername("updateduser");
        updatedUser.setAge(26);
        updatedUser.setWeightKg(74.0);

        when(userService.updateUserProfile(anyLong(), any(UserDTO.class))).thenReturn(updatedUser);

        // When
        ResponseEntity<UserDTO> response = userController.updateUser(1L, updateRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("updateduser", response.getBody().getUsername());
        assertEquals(26, response.getBody().getAge());
        verify(userService, times(1)).updateUserProfile(anyLong(), any(UserDTO.class));
    }

    @Test
    @DisplayName("PUT /users/me/activity - Should update user's last activity")
    void testUpdateLastActivity_Success() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(AUTH_USER_ID);
        SecurityContextHolder.setContext(securityContext);
        when(userService.updateLastActivity(AUTH_USER_ID)).thenReturn(testUser);

        // When
        ResponseEntity<UserDTO> response = userController.updateLastActivity();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(AUTH_USER_ID, response.getBody().getAuthUserId());
        verify(userService, times(1)).updateLastActivity(AUTH_USER_ID);
    }
}
