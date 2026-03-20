package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.ExerciseController;
import fr.epsi.healthaicoachapi.entity.Exercise;
import fr.epsi.healthaicoachapi.repository.ExerciseRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseControllerTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseController exerciseController;

    private Exercise testExercise;

    @BeforeEach
    void setUp() {
        testExercise = new Exercise();
        testExercise.setId(1L);
        testExercise.setExternalId("ex-1");
        testExercise.setName("Running");
    }

    @Test
    @DisplayName("GET /exercises - Should retrieve paginated exercises")
    void testGetAllExercises_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> exercisePage = new PageImpl<>(Arrays.asList(testExercise), pageable, 1);
        when(exerciseRepository.findAll(any(Pageable.class))).thenReturn(exercisePage);

        // When
        ResponseEntity<Page<Exercise>> response = exerciseController.getAllExercises(pageable);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("Running", response.getBody().getContent().get(0).getName());
        verify(exerciseRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /exercises/{id} - Should retrieve exercise by ID")
    void testGetExerciseById_Success() {
        // Given
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(testExercise));

        // When
        ResponseEntity<Exercise> response = exerciseController.getExerciseById(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Running", response.getBody().getName());
        assertEquals("ex-1", response.getBody().getExternalId());
        verify(exerciseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /exercises/{id} - Should return 404 for non-existent exercise")
    void testGetExerciseById_NotFound() {
        // Given
        when(exerciseRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Exercise> response = exerciseController.getExerciseById(999L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(exerciseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET /exercises - Should return empty page when no exercises exist")
    void testGetAllExercises_EmptyResult() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);
        when(exerciseRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // When
        ResponseEntity<Page<Exercise>> response = exerciseController.getAllExercises(pageable);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getContent().size());
        verify(exerciseRepository, times(1)).findAll(any(Pageable.class));
    }
}
