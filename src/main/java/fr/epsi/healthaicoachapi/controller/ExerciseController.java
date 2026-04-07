package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.ExerciseDTO;
import fr.epsi.healthaicoachapi.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercises")
@Tag(name = "Exercises", description = "API du catalogue d'exercices")
@SecurityRequirement(name = "bearer-jwt")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    @Operation(summary = "Liste tous les exercices")
    public ResponseEntity<Page<ExerciseDTO>> getAllExercises(Pageable pageable) {
        Page<ExerciseDTO> exercises = exerciseService.getAllExercises(pageable);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un exercice par ID")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Long id) {
        ExerciseDTO exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(exercise);
    }
}

