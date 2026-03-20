package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.entity.ExerciseEntry;
import fr.epsi.healthaicoachapi.repository.ExerciseEntryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@Tag(name = "Workouts", description = "API de suivi des séances d'exercice")
@SecurityRequirement(name = "bearer-jwt")
public class WorkoutController {

    private static final Logger log = LoggerFactory.getLogger(WorkoutController.class);

    private final ExerciseEntryRepository exerciseEntryRepository;

    public WorkoutController(ExerciseEntryRepository exerciseEntryRepository) {
        this.exerciseEntryRepository = exerciseEntryRepository;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Liste les séances d'exercice d'un utilisateur")
    public ResponseEntity<List<ExerciseEntry>> getUserWorkouts(@PathVariable Long userId) {
        List<ExerciseEntry> entries = exerciseEntryRepository.findByUserId(userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une séance d'exercice par ID")
    public ResponseEntity<ExerciseEntry> getWorkoutById(@PathVariable Long id) {
        return exerciseEntryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle séance d'exercice")
    public ResponseEntity<ExerciseEntry> createWorkout(@RequestBody ExerciseEntry entry) {
        try {
            ExerciseEntry saved = exerciseEntryRepository.save(entry);
            log.info("Workout entry created for user: {}", entry.getUser().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Error creating workout entry: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une séance d'exercice")
    public ResponseEntity<ExerciseEntry> updateWorkout(@PathVariable Long id, @RequestBody ExerciseEntry entryDetails) {
        return exerciseEntryRepository.findById(id)
                .map(entry -> {
                    entry.setWorkoutType(entryDetails.getWorkoutType());
                    entry.setDurationMin(entryDetails.getDurationMin());
                    entry.setCaloriesBurned(entryDetails.getCaloriesBurned());
                    entry.setSteps(entryDetails.getSteps());
                    entry.setHeartRateAvg(entryDetails.getHeartRateAvg());
                    entry.setHeartRateMax(entryDetails.getHeartRateMax());
                    entry.setStatus(entryDetails.getStatus());
                    ExerciseEntry updated = exerciseEntryRepository.save(entry);
                    log.info("Workout entry {} updated", id);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une séance d'exercice")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        if (exerciseEntryRepository.existsById(id)) {
            exerciseEntryRepository.deleteById(id);
            log.info("Workout entry {} deleted", id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

