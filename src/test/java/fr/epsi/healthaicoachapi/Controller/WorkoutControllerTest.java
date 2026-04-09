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
class WorkoutControllerTest {

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutController workoutController;

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
    @DisplayName("GET /workouts/user/{userId} - Should return list of workouts")
    void testGetUserWorkouts_Success() {
        ExerciseEntryDTO entry = new ExerciseEntryDTO();
        entry.setId(1L);

        when(workoutService.getUserWorkouts(1L, USER_EMAIL)).thenReturn(List.of(entry));

        ResponseEntity<List<ExerciseEntryDTO>> response = workoutController.getUserWorkouts(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(workoutService, times(1)).getUserWorkouts(1L, USER_EMAIL);
    }

    @Test
    @DisplayName("GET /workouts/user/{userId} - Should return empty list")
    void testGetUserWorkouts_Empty() {
        when(workoutService.getUserWorkouts(1L, USER_EMAIL)).thenReturn(List.of());

        ResponseEntity<List<ExerciseEntryDTO>> response = workoutController.getUserWorkouts(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /workouts/{id} - Should return workout by ID")
    void testGetWorkoutById_Success() {
        ExerciseEntryDTO entry = new ExerciseEntryDTO();
        entry.setId(1L);

        when(workoutService.getWorkoutById(1L, USER_EMAIL)).thenReturn(entry);

        ResponseEntity<ExerciseEntryDTO> response = workoutController.getWorkoutById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(workoutService, times(1)).getWorkoutById(1L, USER_EMAIL);
    }

    @Test
    @DisplayName("GET /workouts/{id} - Should throw exception when not found")
    void testGetWorkoutById_NotFound() {
        when(workoutService.getWorkoutById(99L, USER_EMAIL))
                .thenThrow(new RuntimeException("Workout not found"));

        assertThrows(RuntimeException.class, () -> workoutController.getWorkoutById(99L));
    }

    @Test
    @DisplayName("POST /workouts - Should create a new workout")
    void testCreateWorkout_Success() {
        ExerciseEntryDTO dto = new ExerciseEntryDTO();
        dto.setId(1L);

        when(workoutService.createWorkout(any(ExerciseEntryDTO.class), eq(USER_EMAIL))).thenReturn(dto);

        ResponseEntity<ExerciseEntryDTO> response = workoutController.createWorkout(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(workoutService, times(1)).createWorkout(any(ExerciseEntryDTO.class), eq(USER_EMAIL));
    }

    @Test
    @DisplayName("PUT /workouts/{id} - Should update workout")
    void testUpdateWorkout_Success() {
        ExerciseEntryDTO dto = new ExerciseEntryDTO();
        dto.setId(1L);

        when(workoutService.updateWorkout(eq(1L), any(ExerciseEntryDTO.class), eq(USER_EMAIL))).thenReturn(dto);

        ResponseEntity<ExerciseEntryDTO> response = workoutController.updateWorkout(1L, dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(workoutService, times(1)).updateWorkout(eq(1L), any(ExerciseEntryDTO.class), eq(USER_EMAIL));
    }

    @Test
    @DisplayName("DELETE /workouts/{id} - Should delete workout")
    void testDeleteWorkout_Success() {
        doNothing().when(workoutService).deleteWorkout(1L, USER_EMAIL);

        ResponseEntity<Void> response = workoutController.deleteWorkout(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(workoutService, times(1)).deleteWorkout(1L, USER_EMAIL);
    }
}
