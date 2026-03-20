package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.WorkoutController;
import fr.epsi.healthaicoachapi.entity.ExerciseEntry;
import fr.epsi.healthaicoachapi.entity.User;
import fr.epsi.healthaicoachapi.repository.ExerciseEntryRepository;
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
class WorkoutControllerTest {

    @Mock
    private ExerciseEntryRepository exerciseEntryRepository;

    @InjectMocks
    private WorkoutController workoutController;

    private User testUser;
    private ExerciseEntry testWorkout;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .build();

        testWorkout = new ExerciseEntry();
        testWorkout.setId(1L);
        testWorkout.setUser(testUser);
        testWorkout.setWorkoutType("Running");
        testWorkout.setDurationMin(BigDecimal.valueOf(30));
        testWorkout.setCaloriesBurned(BigDecimal.valueOf(300));
        testWorkout.setSteps(5000);
        testWorkout.setHeartRateAvg(145);
        testWorkout.setHeartRateMax(165);
    }

    @Test
    @DisplayName("GET /workouts/user/{userId} - Should retrieve user workouts")
    void testGetUserWorkouts_Success() {
        // Given
        List<ExerciseEntry> workouts = Arrays.asList(testWorkout);
        when(exerciseEntryRepository.findByUserId(1L)).thenReturn(workouts);

        // When
        ResponseEntity<List<ExerciseEntry>> response = workoutController.getUserWorkouts(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Running", response.getBody().get(0).getWorkoutType());
        verify(exerciseEntryRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("GET /workouts/{id} - Should retrieve specific workout entry")
    void testGetWorkoutById_Success() {
        // Given
        when(exerciseEntryRepository.findById(1L)).thenReturn(Optional.of(testWorkout));

        // When
        ResponseEntity<ExerciseEntry> response = workoutController.getWorkoutById(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Running", response.getBody().getWorkoutType());
        assertEquals(BigDecimal.valueOf(30), response.getBody().getDurationMin());
        verify(exerciseEntryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /workouts/{id} - Should return 404 when workout not found")
    void testGetWorkoutById_NotFound() {
        // Given
        when(exerciseEntryRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<ExerciseEntry> response = workoutController.getWorkoutById(999L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(exerciseEntryRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("POST /workouts - Should create workout entry")
    void testCreateWorkout_Success() {
        // Given
        ExerciseEntry newWorkout = new ExerciseEntry();
        newWorkout.setUser(testUser);
        newWorkout.setWorkoutType("Cycling");
        newWorkout.setDurationMin(BigDecimal.valueOf(45));
        newWorkout.setCaloriesBurned(BigDecimal.valueOf(400));

        ExerciseEntry savedWorkout = new ExerciseEntry();
        savedWorkout.setId(2L);
        savedWorkout.setUser(testUser);
        savedWorkout.setWorkoutType("Cycling");
        savedWorkout.setDurationMin(BigDecimal.valueOf(45));
        savedWorkout.setCaloriesBurned(BigDecimal.valueOf(400));

        when(exerciseEntryRepository.save(any(ExerciseEntry.class))).thenReturn(savedWorkout);

        // When
        ResponseEntity<ExerciseEntry> response = workoutController.createWorkout(newWorkout);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getId());
        assertEquals("Cycling", response.getBody().getWorkoutType());
        verify(exerciseEntryRepository, times(1)).save(any(ExerciseEntry.class));
    }

    @Test
    @DisplayName("PUT /workouts/{id} - Should update workout entry")
    void testUpdateWorkout_Success() {
        // Given
        ExerciseEntry updateDetails = new ExerciseEntry();
        updateDetails.setWorkoutType("Running");
        updateDetails.setDurationMin(BigDecimal.valueOf(35));
        updateDetails.setCaloriesBurned(BigDecimal.valueOf(350));
        updateDetails.setSteps(5500);
        updateDetails.setHeartRateAvg(150);
        updateDetails.setHeartRateMax(170);
        updateDetails.setStatus("Completed");

        when(exerciseEntryRepository.findById(1L)).thenReturn(Optional.of(testWorkout));
        when(exerciseEntryRepository.save(any(ExerciseEntry.class))).thenReturn(testWorkout);

        // When
        ResponseEntity<ExerciseEntry> response = workoutController.updateWorkout(1L, updateDetails);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(exerciseEntryRepository, times(1)).findById(1L);
        verify(exerciseEntryRepository, times(1)).save(any(ExerciseEntry.class));
    }

    @Test
    @DisplayName("PUT /workouts/{id} - Should return 404 when updating non-existent workout")
    void testUpdateWorkout_NotFound() {
        // Given
        ExerciseEntry updateDetails = new ExerciseEntry();
        updateDetails.setWorkoutType("Swimming");

        when(exerciseEntryRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<ExerciseEntry> response = workoutController.updateWorkout(999L, updateDetails);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(exerciseEntryRepository, times(1)).findById(999L);
        verify(exerciseEntryRepository, never()).save(any(ExerciseEntry.class));
    }

    @Test
    @DisplayName("DELETE /workouts/{id} - Should delete workout entry")
    void testDeleteWorkout_Success() {
        // Given
        when(exerciseEntryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(exerciseEntryRepository).deleteById(1L);

        // When
        ResponseEntity<Void> response = workoutController.deleteWorkout(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(exerciseEntryRepository, times(1)).existsById(1L);
        verify(exerciseEntryRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /workouts/{id} - Should return 404 when deleting non-existent workout")
    void testDeleteWorkout_NotFound() {
        // Given
        when(exerciseEntryRepository.existsById(999L)).thenReturn(false);

        // When
        ResponseEntity<Void> response = workoutController.deleteWorkout(999L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(exerciseEntryRepository, times(1)).existsById(999L);
        verify(exerciseEntryRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("POST /workouts - Should create workout with all fields")
    void testCreateWorkout_AllFields() {
        // Given
        ExerciseEntry fullWorkout = new ExerciseEntry();
        fullWorkout.setUser(testUser);
        fullWorkout.setWorkoutType("HIIT");
        fullWorkout.setDurationMin(BigDecimal.valueOf(25));
        fullWorkout.setCaloriesBurned(BigDecimal.valueOf(320));
        fullWorkout.setSteps(3000);
        fullWorkout.setHeartRateAvg(165);
        fullWorkout.setHeartRateMax(185);

        ExerciseEntry savedWorkout = new ExerciseEntry();
        savedWorkout.setId(3L);
        savedWorkout.setUser(testUser);
        savedWorkout.setWorkoutType("HIIT");
        savedWorkout.setDurationMin(BigDecimal.valueOf(25));
        savedWorkout.setCaloriesBurned(BigDecimal.valueOf(320));
        savedWorkout.setSteps(3000);
        savedWorkout.setHeartRateAvg(165);
        savedWorkout.setHeartRateMax(185);

        when(exerciseEntryRepository.save(any(ExerciseEntry.class))).thenReturn(savedWorkout);

        // When
        ResponseEntity<ExerciseEntry> response = workoutController.createWorkout(fullWorkout);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("HIIT", response.getBody().getWorkoutType());
        assertEquals(3000, response.getBody().getSteps());
        assertEquals(165, response.getBody().getHeartRateAvg());
        verify(exerciseEntryRepository, times(1)).save(any(ExerciseEntry.class));
    }
}
