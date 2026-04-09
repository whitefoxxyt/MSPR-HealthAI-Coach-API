package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.UserController;
import fr.epsi.healthaicoachapi.dto.UserDTO;
import fr.epsi.healthaicoachapi.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private void mockSecurityContext(String email) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("GET /users/me - Should return current user profile")
    void testGetCurrentUser_Success() {
        mockSecurityContext("test@example.com");

        UserDTO expectedUser = new UserDTO();
        expectedUser.setEmail("test@example.com");
        expectedUser.setUsername("testuser");

        when(userService.getUserByEmail("test@example.com")).thenReturn(expectedUser);

        ResponseEntity<UserDTO> response = userController.getCurrentUser();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService, times(1)).getUserByEmail("test@example.com");
    }

    @Test
    @DisplayName("GET /users/{userId} - Should return user by ID")
    void testGetUserById_Success() {
        UserDTO expectedUser = new UserDTO();
        expectedUser.setId(1L);
        expectedUser.setEmail("test@example.com");

        when(userService.getUserById(1L)).thenReturn(expectedUser);

        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("GET /users/{userId} - Should throw exception when user not found")
    void testGetUserById_NotFound() {
        when(userService.getUserById(99L)).thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> userController.getUserById(99L));
        verify(userService, times(1)).getUserById(99L);
    }

    @Test
    @DisplayName("PUT /users/{userId} - Should update user profile")
    void testUpdateUser_Success() {
        UserDTO inputDTO = new UserDTO();
        inputDTO.setUsername("newname");

        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(1L);
        updatedDTO.setUsername("newname");

        when(userService.updateUserProfile(1L, inputDTO)).thenReturn(updatedDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(1L, inputDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newname", response.getBody().getUsername());
        verify(userService, times(1)).updateUserProfile(1L, inputDTO);
    }

    @Test
    @DisplayName("PUT /users/me/activity - Should update last activity")
    void testUpdateLastActivity_Success() {
        mockSecurityContext("test@example.com");

        UserDTO updatedUser = new UserDTO();
        updatedUser.setEmail("test@example.com");

        when(userService.updateLastActivity("test@example.com")).thenReturn(updatedUser);

        ResponseEntity<UserDTO> response = userController.updateLastActivity();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService, times(1)).updateLastActivity("test@example.com");
    }
}
