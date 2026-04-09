package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.NutritionController;
import fr.epsi.healthaicoachapi.dto.NutritionEntryDTO;
import fr.epsi.healthaicoachapi.service.NutritionService;
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
class NutritionControllerTest {

    @Mock
    private NutritionService nutritionService;

    @InjectMocks
    private NutritionController nutritionController;

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
    @DisplayName("GET /nutrition/user/{userId} - Should return list of nutrition entries")
    void testGetUserNutritionEntries_Success() {
        NutritionEntryDTO entry = new NutritionEntryDTO();
        entry.setId(1L);

        when(nutritionService.getUserNutritionEntries(1L, USER_EMAIL)).thenReturn(List.of(entry));

        ResponseEntity<List<NutritionEntryDTO>> response = nutritionController.getUserNutritionEntries(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(nutritionService, times(1)).getUserNutritionEntries(1L, USER_EMAIL);
    }

    @Test
    @DisplayName("GET /nutrition/user/{userId} - Should return empty list")
    void testGetUserNutritionEntries_Empty() {
        when(nutritionService.getUserNutritionEntries(1L, USER_EMAIL)).thenReturn(List.of());

        ResponseEntity<List<NutritionEntryDTO>> response = nutritionController.getUserNutritionEntries(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /nutrition/{id} - Should return nutrition entry by ID")
    void testGetNutritionEntryById_Success() {
        NutritionEntryDTO entry = new NutritionEntryDTO();
        entry.setId(1L);

        when(nutritionService.getNutritionEntryById(1L, USER_EMAIL)).thenReturn(entry);

        ResponseEntity<NutritionEntryDTO> response = nutritionController.getNutritionEntryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(nutritionService, times(1)).getNutritionEntryById(1L, USER_EMAIL);
    }

    @Test
    @DisplayName("GET /nutrition/{id} - Should throw exception when entry not found")
    void testGetNutritionEntryById_NotFound() {
        when(nutritionService.getNutritionEntryById(99L, USER_EMAIL))
                .thenThrow(new RuntimeException("Entry not found"));

        assertThrows(RuntimeException.class, () -> nutritionController.getNutritionEntryById(99L));
    }

    @Test
    @DisplayName("POST /nutrition - Should create a new nutrition entry")
    void testCreateNutritionEntry_Success() {
        NutritionEntryDTO dto = new NutritionEntryDTO();
        dto.setId(1L);

        when(nutritionService.createNutritionEntry(any(NutritionEntryDTO.class), eq(USER_EMAIL))).thenReturn(dto);

        ResponseEntity<NutritionEntryDTO> response = nutritionController.createNutritionEntry(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(nutritionService, times(1)).createNutritionEntry(any(NutritionEntryDTO.class), eq(USER_EMAIL));
    }

    @Test
    @DisplayName("PUT /nutrition/{id} - Should update nutrition entry")
    void testUpdateNutritionEntry_Success() {
        NutritionEntryDTO dto = new NutritionEntryDTO();
        dto.setId(1L);

        when(nutritionService.updateNutritionEntry(eq(1L), any(NutritionEntryDTO.class), eq(USER_EMAIL))).thenReturn(dto);

        ResponseEntity<NutritionEntryDTO> response = nutritionController.updateNutritionEntry(1L, dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(nutritionService, times(1)).updateNutritionEntry(eq(1L), any(NutritionEntryDTO.class), eq(USER_EMAIL));
    }

    @Test
    @DisplayName("DELETE /nutrition/{id} - Should delete nutrition entry")
    void testDeleteNutritionEntry_Success() {
        doNothing().when(nutritionService).deleteNutritionEntry(1L, USER_EMAIL);

        ResponseEntity<Void> response = nutritionController.deleteNutritionEntry(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(nutritionService, times(1)).deleteNutritionEntry(1L, USER_EMAIL);
    }
}
