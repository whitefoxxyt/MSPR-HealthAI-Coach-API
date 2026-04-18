package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.WorkoutController;
import fr.epsi.healthaicoachapi.dto.ExerciseEntryDTO;
import fr.epsi.healthaicoachapi.service.WorkoutService;
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
class WorkoutControllerTest {

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutController workoutController;

    private ExerciseEntryDTO workoutDTO;

    @BeforeEach
    void setUp() {
        workoutDTO = ExerciseEntryDTO.builder()
                .id(1L)
                .workoutType("Running")
                .durationMin(new BigDecimal("30"))
                .caloriesBurned(new BigDecimal("250"))
                .source("manual")
                .status("BRUT")
                .build();
    }

    @Test
    @DisplayName("GET /workouts returns paginated list")
    void listWorkouts() {
        Page<ExerciseEntryDTO> page = new PageImpl<>(List.of(workoutDTO));
        when(workoutService.listWorkouts(any())).thenReturn(page);

        ResponseEntity<Page<ExerciseEntryDTO>> response = workoutController.listWorkouts(PageRequest.of(0, 20));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("GET /workouts/{id} returns workout")
    void getWorkoutById() {
        when(workoutService.getWorkoutById(1L)).thenReturn(workoutDTO);

        ResponseEntity<ExerciseEntryDTO> response = workoutController.getWorkoutById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Running", response.getBody().getWorkoutType());
    }

    @Test
    @DisplayName("POST /workouts creates entry and returns 201")
    void createWorkout() {
        when(workoutService.createWorkout(any())).thenReturn(workoutDTO);

        ResponseEntity<ExerciseEntryDTO> response = workoutController.createWorkout(workoutDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Running", response.getBody().getWorkoutType());
    }

    @Test
    @DisplayName("PUT /workouts/{id} updates entry")
    void updateWorkout() {
        when(workoutService.updateWorkout(eq(1L), any())).thenReturn(workoutDTO);

        ResponseEntity<ExerciseEntryDTO> response = workoutController.updateWorkout(1L, workoutDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("DELETE /workouts/{id} returns 204")
    void deleteWorkout() {
        doNothing().when(workoutService).deleteWorkout(1L);

        ResponseEntity<Void> response = workoutController.deleteWorkout(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(workoutService).deleteWorkout(1L);
    }
}
