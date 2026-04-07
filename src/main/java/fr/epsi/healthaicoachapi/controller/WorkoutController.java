package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.ExerciseEntryDTO;
import fr.epsi.healthaicoachapi.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@Tag(name = "Workouts", description = "API de suivi des séances d'exercice")
@SecurityRequirement(name = "bearer-jwt")
public class WorkoutController {

    private static final Logger log = LoggerFactory.getLogger(WorkoutController.class);

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Liste les séances d'exercice d'un utilisateur")
    public ResponseEntity<List<ExerciseEntryDTO>> getUserWorkouts(@PathVariable Long userId) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ExerciseEntryDTO> entries = workoutService.getUserWorkouts(userId, email);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une séance d'exercice par ID")
    public ResponseEntity<ExerciseEntryDTO> getWorkoutById(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ExerciseEntryDTO entry = workoutService.getWorkoutById(id, email);
        return ResponseEntity.ok(entry);
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle séance d'exercice")
    public ResponseEntity<ExerciseEntryDTO> createWorkout(@Valid @RequestBody ExerciseEntryDTO dto) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ExerciseEntryDTO saved = workoutService.createWorkout(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une séance d'exercice")
    public ResponseEntity<ExerciseEntryDTO> updateWorkout(@PathVariable Long id, @Valid @RequestBody ExerciseEntryDTO dto) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ExerciseEntryDTO updated = workoutService.updateWorkout(id, dto, email);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une séance d'exercice")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        workoutService.deleteWorkout(id, email);
        return ResponseEntity.noContent().build();
    }
}

