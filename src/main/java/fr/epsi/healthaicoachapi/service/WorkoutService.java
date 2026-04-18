package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.ExerciseEntryDTO;
import fr.epsi.healthaicoachapi.entity.ExerciseEntry;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.repository.ExerciseEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private static final Logger log = LoggerFactory.getLogger(WorkoutService.class);

    private final ExerciseEntryRepository exerciseEntryRepository;

    public WorkoutService(ExerciseEntryRepository exerciseEntryRepository) {
        this.exerciseEntryRepository = exerciseEntryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ExerciseEntryDTO> listWorkouts(Pageable pageable) {
        return exerciseEntryRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<ExerciseEntryDTO> listAllWorkouts() {
        return exerciseEntryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExerciseEntryDTO getWorkoutById(Long id) {
        ExerciseEntry entry = exerciseEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout entry", id));
        return mapToDTO(entry);
    }

    @Transactional
    public ExerciseEntryDTO createWorkout(ExerciseEntryDTO dto) {
        ExerciseEntry entry = new ExerciseEntry();
        entry.setWorkoutType(dto.getWorkoutType());
        entry.setDurationMin(dto.getDurationMin());
        entry.setCaloriesBurned(dto.getCaloriesBurned());
        entry.setSteps(dto.getSteps());
        entry.setHeartRateAvg(dto.getHeartRateAvg());
        entry.setHeartRateMax(dto.getHeartRateMax());
        entry.setSource(dto.getSource());
        entry.setStatus(dto.getStatus() != null ? dto.getStatus() : "BRUT");

        ExerciseEntry saved = exerciseEntryRepository.save(entry);
        log.info("Workout entry {} created", saved.getId());
        return mapToDTO(saved);
    }

    @Transactional
    public ExerciseEntryDTO updateWorkout(Long id, ExerciseEntryDTO dto) {
        ExerciseEntry entry = exerciseEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout entry", id));

        if (dto.getWorkoutType() != null) entry.setWorkoutType(dto.getWorkoutType());
        if (dto.getDurationMin() != null) entry.setDurationMin(dto.getDurationMin());
        if (dto.getCaloriesBurned() != null) entry.setCaloriesBurned(dto.getCaloriesBurned());
        if (dto.getSteps() != null) entry.setSteps(dto.getSteps());
        if (dto.getHeartRateAvg() != null) entry.setHeartRateAvg(dto.getHeartRateAvg());
        if (dto.getHeartRateMax() != null) entry.setHeartRateMax(dto.getHeartRateMax());
        if (dto.getStatus() != null) entry.setStatus(dto.getStatus());

        ExerciseEntry updated = exerciseEntryRepository.save(entry);
        log.info("Workout entry {} updated", id);
        return mapToDTO(updated);
    }

    @Transactional
    public void deleteWorkout(Long id) {
        if (!exerciseEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workout entry", id);
        }
        exerciseEntryRepository.deleteById(id);
        log.info("Workout entry {} deleted", id);
    }

    private ExerciseEntryDTO mapToDTO(ExerciseEntry entry) {
        return ExerciseEntryDTO.builder()
                .id(entry.getId())
                .workoutType(entry.getWorkoutType())
                .durationMin(entry.getDurationMin())
                .caloriesBurned(entry.getCaloriesBurned())
                .steps(entry.getSteps())
                .heartRateAvg(entry.getHeartRateAvg())
                .heartRateMax(entry.getHeartRateMax())
                .source(entry.getSource())
                .status(entry.getStatus())
                .createdAt(entry.getCreatedAt())
                .build();
    }
}
