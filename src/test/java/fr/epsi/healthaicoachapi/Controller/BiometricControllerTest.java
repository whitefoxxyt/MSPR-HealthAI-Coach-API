package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.BiometricController;
import fr.epsi.healthaicoachapi.dto.BiometricEntryDTO;
import fr.epsi.healthaicoachapi.service.BiometricService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BiometricControllerTest {

    @Mock
    private BiometricService biometricService;

    @InjectMocks
    private BiometricController biometricController;

    private static final String USER_EMAIL = "test@example.com";

    @BeforeEach
    void setupSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER_EMAIL);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("GET /biometrics/user/{userId} - Should return list of biometric entries")
    void testGetUserBiometrics_Success() {
        BiometricEntryDTO entry = new BiometricEntryDTO();
        entry.setId(1L);

        when(biometricService.getUserBiometrics(1L, USER_EMAIL)).thenReturn(List.of(entry));

        ResponseEntity<List<BiometricEntryDTO>> response = biometricController.getUserBiometrics(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(biometricService, times(1)).getUserBiometrics(1L, USER_EMAIL);
    }

    @Test
    @DisplayName("GET /biometrics/user/{userId} - Should return empty list")
    void testGetUserBiometrics_Empty() {
        when(biometricService.getUserBiometrics(1L, USER_EMAIL)).thenReturn(List.of());

        ResponseEntity<List<BiometricEntryDTO>> response = biometricController.getUserBiometrics(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /biometrics/{id} - Should return biometric entry by ID")
    void testGetBiometricById_Success() {
        BiometricEntryDTO entry = new BiometricEntryDTO();
        entry.setId(1L);

        when(biometricService.getBiometricById(1L, USER_EMAIL)).thenReturn(entry);

        ResponseEntity<BiometricEntryDTO> response = biometricController.getBiometricById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(biometricService, times(1)).getBiometricById(1L, USER_EMAIL);
    }

    @Test
    @DisplayName("GET /biometrics/{id} - Should throw exception when not found")
    void testGetBiometricById_NotFound() {
        when(biometricService.getBiometricById(99L, USER_EMAIL))
                .thenThrow(new RuntimeException("Biometric not found"));

        assertThrows(RuntimeException.class, () -> biometricController.getBiometricById(99L));
    }

    @Test
    @DisplayName("POST /biometrics - Should create a new biometric entry")
    void testCreateBiometric_Success() {
        BiometricEntryDTO dto = new BiometricEntryDTO();
        dto.setId(1L);

        when(biometricService.createBiometric(any(BiometricEntryDTO.class), eq(USER_EMAIL))).thenReturn(dto);

        ResponseEntity<BiometricEntryDTO> response = biometricController.createBiometric(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(biometricService, times(1)).createBiometric(any(BiometricEntryDTO.class), eq(USER_EMAIL));
    }

    @Test
    @DisplayName("PUT /biometrics/{id} - Should update biometric entry")
    void testUpdateBiometric_Success() {
        BiometricEntryDTO dto = new BiometricEntryDTO();
        dto.setId(1L);

        when(biometricService.updateBiometric(eq(1L), any(BiometricEntryDTO.class), eq(USER_EMAIL))).thenReturn(dto);

        ResponseEntity<BiometricEntryDTO> response = biometricController.updateBiometric(1L, dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(biometricService, times(1)).updateBiometric(eq(1L), any(BiometricEntryDTO.class), eq(USER_EMAIL));
    }

    @Test
    @DisplayName("DELETE /biometrics/{id} - Should delete biometric entry")
    void testDeleteBiometric_Success() {
        doNothing().when(biometricService).deleteBiometric(1L, USER_EMAIL);

        ResponseEntity<Void> response = biometricController.deleteBiometric(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(biometricService, times(1)).deleteBiometric(1L, USER_EMAIL);
    }
}
