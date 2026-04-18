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
class NutritionControllerTest {

    @Mock
    private NutritionService nutritionService;

    @InjectMocks
    private NutritionController nutritionController;

    private NutritionEntryDTO entryDTO;

    @BeforeEach
    void setUp() {
        entryDTO = NutritionEntryDTO.builder()
                .id(1L)
                .foodName("Apple")
                .calories(new BigDecimal("95"))
                .source("manual")
                .status("BRUT")
                .build();
    }

    @Test
    @DisplayName("GET /nutrition returns paginated list")
    void listNutritionEntries() {
        Page<NutritionEntryDTO> page = new PageImpl<>(List.of(entryDTO));
        when(nutritionService.listNutritionEntries(any())).thenReturn(page);

        ResponseEntity<Page<NutritionEntryDTO>> response = nutritionController.listNutritionEntries(PageRequest.of(0, 20));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("GET /nutrition/{id} returns entry")
    void getNutritionEntryById() {
        when(nutritionService.getNutritionEntryById(1L)).thenReturn(entryDTO);

        ResponseEntity<NutritionEntryDTO> response = nutritionController.getNutritionEntryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Apple", response.getBody().getFoodName());
    }

    @Test
    @DisplayName("POST /nutrition creates entry and returns 201")
    void createNutritionEntry() {
        when(nutritionService.createNutritionEntry(any())).thenReturn(entryDTO);

        ResponseEntity<NutritionEntryDTO> response = nutritionController.createNutritionEntry(entryDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Apple", response.getBody().getFoodName());
    }

    @Test
    @DisplayName("PUT /nutrition/{id} updates entry")
    void updateNutritionEntry() {
        when(nutritionService.updateNutritionEntry(eq(1L), any())).thenReturn(entryDTO);

        ResponseEntity<NutritionEntryDTO> response = nutritionController.updateNutritionEntry(1L, entryDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("DELETE /nutrition/{id} returns 204")
    void deleteNutritionEntry() {
        doNothing().when(nutritionService).deleteNutritionEntry(1L);

        ResponseEntity<Void> response = nutritionController.deleteNutritionEntry(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(nutritionService).deleteNutritionEntry(1L);
    }
}
