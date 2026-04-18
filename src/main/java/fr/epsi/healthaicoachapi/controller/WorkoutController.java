package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.ExerciseEntryDTO;
import fr.epsi.healthaicoachapi.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workouts")
@Tag(name = "Workouts", description = "API de suivi des séances d'exercice")
@SecurityRequirement(name = "bearer-jwt")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    @Operation(summary = "Liste paginée des séances d'exercice")
    public ResponseEntity<Page<ExerciseEntryDTO>> listWorkouts(Pageable pageable) {
        return ResponseEntity.ok(workoutService.listWorkouts(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une séance d'exercice par ID")
    public ResponseEntity<ExerciseEntryDTO> getWorkoutById(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.getWorkoutById(id));
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle séance d'exercice")
    public ResponseEntity<ExerciseEntryDTO> createWorkout(@Valid @RequestBody ExerciseEntryDTO dto) {
        ExerciseEntryDTO saved = workoutService.createWorkout(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une séance d'exercice")
    public ResponseEntity<ExerciseEntryDTO> updateWorkout(@PathVariable Long id, @Valid @RequestBody ExerciseEntryDTO dto) {
        return ResponseEntity.ok(workoutService.updateWorkout(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une séance d'exercice")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }
}
