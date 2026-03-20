package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.entity.Exercise;
import fr.epsi.healthaicoachapi.repository.ExerciseRepository;
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

    private final ExerciseRepository exerciseRepository;

    public ExerciseController(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping
    @Operation(summary = "Liste tous les exercices")
    public ResponseEntity<Page<Exercise>> getAllExercises(Pageable pageable) {
        Page<Exercise> exercises = exerciseRepository.findAll(pageable);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un exercice par ID")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long id) {
        return exerciseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

