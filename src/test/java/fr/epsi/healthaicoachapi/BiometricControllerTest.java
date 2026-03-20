package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.BiometricController;
import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import fr.epsi.healthaicoachapi.entity.User;
import fr.epsi.healthaicoachapi.repository.BiometricEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BiometricControllerTest {

    @Mock
    private BiometricEntryRepository biometricEntryRepository;

    @InjectMocks
    private BiometricController biometricController;

    private User testUser;
    private BiometricEntry testBiometric;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .build();

        testBiometric = new BiometricEntry();
        testBiometric.setId(1L);
        testBiometric.setUser(testUser);
        testBiometric.setWeightKg(BigDecimal.valueOf(75.5));
        testBiometric.setHeightCm(BigDecimal.valueOf(180.0));
        testBiometric.setBmi(BigDecimal.valueOf(23.3));
        testBiometric.setFatPercentage(BigDecimal.valueOf(15.5));
        testBiometric.setHeartRateRest(72);
        testBiometric.setBloodPressure("120/80");
    }

    @Test
    @DisplayName("GET /biometrics/user/{userId} - Should retrieve user biometrics")
    void testGetUserBiometrics_Success() {
        // Given
        List<BiometricEntry> biometrics = Arrays.asList(testBiometric);
        when(biometricEntryRepository.findByUserId(1L)).thenReturn(biometrics);

        // When
        ResponseEntity<List<BiometricEntry>> response = biometricController.getUserBiometrics(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(BigDecimal.valueOf(75.5), response.getBody().get(0).getWeightKg());
        verify(biometricEntryRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("GET /biometrics/{id} - Should retrieve specific biometric entry")
    void testGetBiometricById_Success() {
        // Given
        when(biometricEntryRepository.findById(1L)).thenReturn(Optional.of(testBiometric));

        // When
        ResponseEntity<BiometricEntry> response = biometricController.getBiometricById(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.valueOf(75.5), response.getBody().getWeightKg());
        assertEquals(BigDecimal.valueOf(180.0), response.getBody().getHeightCm());
        verify(biometricEntryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /biometrics/{id} - Should return 404 when biometric not found")
    void testGetBiometricById_NotFound() {
        // Given
        when(biometricEntryRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<BiometricEntry> response = biometricController.getBiometricById(999L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(biometricEntryRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("POST /biometrics - Should create biometric entry")
    void testCreateBiometric_Success() {
        // Given
        BiometricEntry newBiometric = new BiometricEntry();
        newBiometric.setUser(testUser);
        newBiometric.setWeightKg(BigDecimal.valueOf(76.0));
        newBiometric.setHeightCm(BigDecimal.valueOf(180.0));

        BiometricEntry savedBiometric = new BiometricEntry();
        savedBiometric.setId(2L);
        savedBiometric.setUser(testUser);
        savedBiometric.setWeightKg(BigDecimal.valueOf(76.0));
        savedBiometric.setHeightCm(BigDecimal.valueOf(180.0));

        when(biometricEntryRepository.save(any(BiometricEntry.class))).thenReturn(savedBiometric);

        // When
        ResponseEntity<BiometricEntry> response = biometricController.createBiometric(newBiometric);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getId());
        assertEquals(BigDecimal.valueOf(76.0), response.getBody().getWeightKg());
        verify(biometricEntryRepository, times(1)).save(any(BiometricEntry.class));
    }

    @Test
    @DisplayName("PUT /biometrics/{id} - Should update biometric entry")
    void testUpdateBiometric_Success() {
        // Given
        BiometricEntry updateDetails = new BiometricEntry();
        updateDetails.setWeightKg(BigDecimal.valueOf(77.0));
        updateDetails.setHeightCm(BigDecimal.valueOf(180.0));
        updateDetails.setBmi(BigDecimal.valueOf(23.8));
        updateDetails.setFatPercentage(BigDecimal.valueOf(14.5));
        updateDetails.setHeartRateRest(70);
        updateDetails.setBloodPressure("118/78");
        updateDetails.setStatus("Active");

        when(biometricEntryRepository.findById(1L)).thenReturn(Optional.of(testBiometric));
        when(biometricEntryRepository.save(any(BiometricEntry.class))).thenReturn(testBiometric);

        // When
        ResponseEntity<BiometricEntry> response = biometricController.updateBiometric(1L, updateDetails);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(biometricEntryRepository, times(1)).findById(1L);
        verify(biometricEntryRepository, times(1)).save(any(BiometricEntry.class));
    }

    @Test
    @DisplayName("PUT /biometrics/{id} - Should return 404 when updating non-existent biometric")
    void testUpdateBiometric_NotFound() {
        // Given
        BiometricEntry updateDetails = new BiometricEntry();
        updateDetails.setWeightKg(BigDecimal.valueOf(77.0));

        when(biometricEntryRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<BiometricEntry> response = biometricController.updateBiometric(999L, updateDetails);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(biometricEntryRepository, times(1)).findById(999L);
        verify(biometricEntryRepository, never()).save(any(BiometricEntry.class));
    }

    @Test
    @DisplayName("DELETE /biometrics/{id} - Should delete biometric entry")
    void testDeleteBiometric_Success() {
        // Given
        when(biometricEntryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(biometricEntryRepository).deleteById(1L);

        // When
        ResponseEntity<Void> response = biometricController.deleteBiometric(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(biometricEntryRepository, times(1)).existsById(1L);
        verify(biometricEntryRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /biometrics/{id} - Should return 404 when deleting non-existent biometric")
    void testDeleteBiometric_NotFound() {
        // Given
        when(biometricEntryRepository.existsById(999L)).thenReturn(false);

        // When
        ResponseEntity<Void> response = biometricController.deleteBiometric(999L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(biometricEntryRepository, times(1)).existsById(999L);
        verify(biometricEntryRepository, never()).deleteById(anyLong());
    }
}
