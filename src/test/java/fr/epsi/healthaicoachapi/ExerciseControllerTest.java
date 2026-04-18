package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.ExerciseController;
import fr.epsi.healthaicoachapi.dto.ExerciseDTO;
import fr.epsi.healthaicoachapi.service.ExerciseService;
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

    private ExerciseDTO testExercise;

    @BeforeEach
    void setUp() {
        testExercise = new ExerciseDTO();
        testExercise.setId(1L);
        testExercise.setExternalId("ex-1");
        testExercise.setName("Running");
    }

    @Test
    @DisplayName("GET /exercises returns paginated list")
    void getAllExercises() {
        Page<ExerciseDTO> page = new PageImpl<>(List.of(testExercise));
        when(exerciseService.getAllExercises(any())).thenReturn(page);

        ResponseEntity<Page<ExerciseDTO>> response = exerciseController.getAllExercises(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Running", response.getBody().getContent().get(0).getName());
    }

    @Test
    @DisplayName("GET /exercises/{id} returns exercise")
    void getExerciseById() {
        when(exerciseService.getExerciseById(1L)).thenReturn(testExercise);

        ResponseEntity<ExerciseDTO> response = exerciseController.getExerciseById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Running", response.getBody().getName());
    }
}
