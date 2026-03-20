package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.NutritionController;
import fr.epsi.healthaicoachapi.entity.NutritionEntry;
import fr.epsi.healthaicoachapi.entity.User;
import fr.epsi.healthaicoachapi.repository.NutritionEntryRepository;
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
class NutritionControllerTest {

    @Mock
    private NutritionEntryRepository nutritionEntryRepository;

    @InjectMocks
    private NutritionController nutritionController;

    private User testUser;
    private NutritionEntry testNutrition;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .build();

        testNutrition = new NutritionEntry();
        testNutrition.setId(1L);
        testNutrition.setUser(testUser);
        testNutrition.setFoodName("Chicken Breast");
        testNutrition.setCategory("Protein");
        testNutrition.setMealType("Lunch");
        testNutrition.setCalories(BigDecimal.valueOf(165));
        testNutrition.setProteinG(BigDecimal.valueOf(31.0));
        testNutrition.setCarbsG(BigDecimal.valueOf(0.0));
        testNutrition.setFatG(BigDecimal.valueOf(3.6));
    }

    @Test
    @DisplayName("GET /nutrition/user/{userId} - Should retrieve user nutrition entries")
    void testGetUserNutritionEntries_Success() {
        // Given
        List<NutritionEntry> nutritionList = Arrays.asList(testNutrition);
        when(nutritionEntryRepository.findByUserId(1L)).thenReturn(nutritionList);

        // When
        ResponseEntity<List<NutritionEntry>> response = nutritionController.getUserNutritionEntries(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Chicken Breast", response.getBody().get(0).getFoodName());
        verify(nutritionEntryRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("GET /nutrition/{id} - Should retrieve specific nutrition entry")
    void testGetNutritionEntryById_Success() {
        // Given
        when(nutritionEntryRepository.findById(1L)).thenReturn(Optional.of(testNutrition));

        // When
        ResponseEntity<NutritionEntry> response = nutritionController.getNutritionEntryById(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Chicken Breast", response.getBody().getFoodName());
        assertEquals(BigDecimal.valueOf(165), response.getBody().getCalories());
        verify(nutritionEntryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /nutrition/{id} - Should return 404 when nutrition entry not found")
    void testGetNutritionEntryById_NotFound() {
        // Given
        when(nutritionEntryRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<NutritionEntry> response = nutritionController.getNutritionEntryById(999L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(nutritionEntryRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("POST /nutrition - Should create nutrition entry")
    void testCreateNutritionEntry_Success() {
        // Given
        NutritionEntry newNutrition = new NutritionEntry();
        newNutrition.setUser(testUser);
        newNutrition.setFoodName("Apple");
        newNutrition.setCalories(BigDecimal.valueOf(95));

        NutritionEntry savedNutrition = new NutritionEntry();
        savedNutrition.setId(2L);
        savedNutrition.setUser(testUser);
        savedNutrition.setFoodName("Apple");
        savedNutrition.setCalories(BigDecimal.valueOf(95));

        when(nutritionEntryRepository.save(any(NutritionEntry.class))).thenReturn(savedNutrition);

        // When
        ResponseEntity<NutritionEntry> response = nutritionController.createNutritionEntry(newNutrition);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getId());
        assertEquals("Apple", response.getBody().getFoodName());
        verify(nutritionEntryRepository, times(1)).save(any(NutritionEntry.class));
    }

    @Test
    @DisplayName("PUT /nutrition/{id} - Should update nutrition entry")
    void testUpdateNutritionEntry_Success() {
        // Given
        NutritionEntry updateDetails = new NutritionEntry();
        updateDetails.setFoodName("Grilled Chicken");
        updateDetails.setCategory("Protein");
        updateDetails.setMealType("Dinner");
        updateDetails.setCalories(BigDecimal.valueOf(170));
        updateDetails.setProteinG(BigDecimal.valueOf(32.0));
        updateDetails.setCarbsG(BigDecimal.valueOf(0.0));
        updateDetails.setFatG(BigDecimal.valueOf(4.0));
        updateDetails.setStatus("Active");

        when(nutritionEntryRepository.findById(1L)).thenReturn(Optional.of(testNutrition));
        when(nutritionEntryRepository.save(any(NutritionEntry.class))).thenReturn(testNutrition);

        // When
        ResponseEntity<NutritionEntry> response = nutritionController.updateNutritionEntry(1L, updateDetails);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(nutritionEntryRepository, times(1)).findById(1L);
        verify(nutritionEntryRepository, times(1)).save(any(NutritionEntry.class));
    }

    @Test
    @DisplayName("PUT /nutrition/{id} - Should return 404 when updating non-existent entry")
    void testUpdateNutritionEntry_NotFound() {
        // Given
        NutritionEntry updateDetails = new NutritionEntry();
        updateDetails.setFoodName("Updated Food");

        when(nutritionEntryRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<NutritionEntry> response = nutritionController.updateNutritionEntry(999L, updateDetails);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(nutritionEntryRepository, times(1)).findById(999L);
        verify(nutritionEntryRepository, never()).save(any(NutritionEntry.class));
    }

    @Test
    @DisplayName("DELETE /nutrition/{id} - Should delete nutrition entry")
    void testDeleteNutritionEntry_Success() {
        // Given
        when(nutritionEntryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(nutritionEntryRepository).deleteById(1L);

        // When
        ResponseEntity<Void> response = nutritionController.deleteNutritionEntry(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(nutritionEntryRepository, times(1)).existsById(1L);
        verify(nutritionEntryRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /nutrition/{id} - Should return 404 when deleting non-existent entry")
    void testDeleteNutritionEntry_NotFound() {
        // Given
        when(nutritionEntryRepository.existsById(999L)).thenReturn(false);

        // When
        ResponseEntity<Void> response = nutritionController.deleteNutritionEntry(999L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(nutritionEntryRepository, times(1)).existsById(999L);
        verify(nutritionEntryRepository, never()).deleteById(anyLong());
    }
}
