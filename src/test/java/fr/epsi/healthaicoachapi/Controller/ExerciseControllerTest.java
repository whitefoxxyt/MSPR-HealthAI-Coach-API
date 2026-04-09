package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.ExerciseController;
import fr.epsi.healthaicoachapi.dto.ExerciseDTO;
import fr.epsi.healthaicoachapi.service.ExerciseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    @Test
    @DisplayName("GET /exercises - Should return a page of exercises")
    void testGetAllExercises_Success() {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(1L);
        dto.setName("Squat");

        Pageable pageable = PageRequest.of(0, 10);
        Page<ExerciseDTO> page = new PageImpl<>(List.of(dto));

        when(exerciseService.getAllExercises(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<ExerciseDTO>> response = exerciseController.getAllExercises(pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(exerciseService, times(1)).getAllExercises(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /exercises - Should return empty page when no exercises")
    void testGetAllExercises_EmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExerciseDTO> emptyPage = new PageImpl<>(List.of());

        when(exerciseService.getAllExercises(any(Pageable.class))).thenReturn(emptyPage);

        ResponseEntity<Page<ExerciseDTO>> response = exerciseController.getAllExercises(pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("GET /exercises/{id} - Should return exercise by ID")
    void testGetExerciseById_Success() {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(1L);
        dto.setName("Deadlift");

        when(exerciseService.getExerciseById(1L)).thenReturn(dto);

        ResponseEntity<ExerciseDTO> response = exerciseController.getExerciseById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Deadlift", response.getBody().getName());
        verify(exerciseService, times(1)).getExerciseById(1L);
    }

    @Test
    @DisplayName("GET /exercises/{id} - Should throw exception when exercise not found")
    void testGetExerciseById_NotFound() {
        when(exerciseService.getExerciseById(99L))
                .thenThrow(new RuntimeException("Exercise not found"));

        assertThrows(RuntimeException.class, () -> exerciseController.getExerciseById(99L));
        verify(exerciseService, times(1)).getExerciseById(99L);
    }
}
