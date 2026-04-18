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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BiometricControllerTest {

    @Mock
    private BiometricService biometricService;

    @InjectMocks
    private BiometricController biometricController;

    private BiometricEntryDTO entryDTO;

    @BeforeEach
    void setUp() {
        entryDTO = BiometricEntryDTO.builder()
                .id(1L)
                .weightKg(new BigDecimal("72.5"))
                .heightCm(new BigDecimal("178"))
                .bmi(new BigDecimal("22.9"))
                .source("manual")
                .status("BRUT")
                .build();
    }

    @Test
    @DisplayName("GET /biometrics returns paginated list")
    void listBiometrics() {
        Page<BiometricEntryDTO> page = new PageImpl<>(List.of(entryDTO));
        when(biometricService.listBiometrics(any())).thenReturn(page);

        ResponseEntity<Page<BiometricEntryDTO>> response = biometricController.listBiometrics(PageRequest.of(0, 20));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("GET /biometrics/{id} returns entry")
    void getBiometricById() {
        when(biometricService.getBiometricById(1L)).thenReturn(entryDTO);

        ResponseEntity<BiometricEntryDTO> response = biometricController.getBiometricById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new BigDecimal("72.5"), response.getBody().getWeightKg());
    }

    @Test
    @DisplayName("POST /biometrics creates entry and returns 201")
    void createBiometric() {
        when(biometricService.createBiometric(any())).thenReturn(entryDTO);

        ResponseEntity<BiometricEntryDTO> response = biometricController.createBiometric(entryDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("PUT /biometrics/{id} updates entry")
    void updateBiometric() {
        when(biometricService.updateBiometric(eq(1L), any())).thenReturn(entryDTO);

        ResponseEntity<BiometricEntryDTO> response = biometricController.updateBiometric(1L, entryDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("DELETE /biometrics/{id} returns 204")
    void deleteBiometric() {
        doNothing().when(biometricService).deleteBiometric(1L);

        ResponseEntity<Void> response = biometricController.deleteBiometric(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(biometricService).deleteBiometric(1L);
    }
}
