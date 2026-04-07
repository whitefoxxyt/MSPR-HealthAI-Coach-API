package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.ExerciseEntryDTO;
import fr.epsi.healthaicoachapi.entity.ExerciseEntry;
import fr.epsi.healthaicoachapi.entity.User;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.exception.UnauthorizedAccessException;
import fr.epsi.healthaicoachapi.repository.ExerciseEntryRepository;
import fr.epsi.healthaicoachapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private static final Logger log = LoggerFactory.getLogger(WorkoutService.class);

    private final ExerciseEntryRepository exerciseEntryRepository;
    private final UserRepository userRepository;

    public WorkoutService(ExerciseEntryRepository exerciseEntryRepository, UserRepository userRepository) {
        this.exerciseEntryRepository = exerciseEntryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ExerciseEntryDTO> getUserWorkouts(Long userId, String authenticatedUserEmail) {
        User authenticatedUser = getUserByEmail(authenticatedUserEmail);
        
        if (!authenticatedUser.getId().equals(userId) && !"ADMIN".equals(authenticatedUser.getRole())) {
            throw new UnauthorizedAccessException("You can only access your own workout data");
        }

        List<ExerciseEntry> entries = exerciseEntryRepository.findByUserId(userId);
        return entries.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExerciseEntryDTO getWorkoutById(Long id, String authenticatedUserEmail) {
        ExerciseEntry entry = exerciseEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout entry", id));

        User authenticatedUser = getUserByEmail(authenticatedUserEmail);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId()) && !"ADMIN".equals(authenticatedUser.getRole())) {
            throw new UnauthorizedAccessException();
        }

        return mapToDTO(entry);
    }

    @Transactional
    public ExerciseEntryDTO createWorkout(ExerciseEntryDTO dto, String authenticatedUserEmail) {
        User authenticatedUser = getUserByEmail(authenticatedUserEmail);
        
        if (!authenticatedUser.getId().equals(dto.getUserId()) && !"ADMIN".equals(authenticatedUser.getRole())) {
            throw new UnauthorizedAccessException("You can only create workout entries for yourself");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.getUserId()));

        ExerciseEntry entry = new ExerciseEntry();
        entry.setUser(user);
        entry.setWorkoutType(dto.getWorkoutType());
        entry.setDurationMin(dto.getDurationMin());
        entry.setCaloriesBurned(dto.getCaloriesBurned());
        entry.setSteps(dto.getSteps());
        entry.setHeartRateAvg(dto.getHeartRateAvg());
        entry.setHeartRateMax(dto.getHeartRateMax());
        entry.setSource(dto.getSource());
        entry.setStatus(dto.getStatus() != null ? dto.getStatus() : "BRUT");

        ExerciseEntry saved = exerciseEntryRepository.save(entry);
        log.info("Workout entry created for user: {}", user.getId());

        return mapToDTO(saved);
    }

    @Transactional
    public ExerciseEntryDTO updateWorkout(Long id, ExerciseEntryDTO dto, String authenticatedUserEmail) {
        ExerciseEntry entry = exerciseEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout entry", id));

        User authenticatedUser = getUserByEmail(authenticatedUserEmail);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId()) && !"ADMIN".equals(authenticatedUser.getRole())) {
            throw new UnauthorizedAccessException();
        }

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
    public void deleteWorkout(Long id, String authenticatedUserEmail) {
        ExerciseEntry entry = exerciseEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout entry", id));

        User authenticatedUser = getUserByEmail(authenticatedUserEmail);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId()) && !"ADMIN".equals(authenticatedUser.getRole())) {
            throw new UnauthorizedAccessException();
        }

        exerciseEntryRepository.deleteById(id);
        log.info("Workout entry {} deleted", id);
    }

    private ExerciseEntryDTO mapToDTO(ExerciseEntry entry) {
        return ExerciseEntryDTO.builder()
                .id(entry.getId())
                .userId(entry.getUser().getId())
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

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
