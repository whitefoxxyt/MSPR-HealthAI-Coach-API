package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.ExerciseDTO;
import fr.epsi.healthaicoachapi.entity.Exercise;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.repository.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciseService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseService.class);

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional(readOnly = true)
    public Page<ExerciseDTO> getAllExercises(Pageable pageable) {
        Page<Exercise> exercises = exerciseRepository.findAll(pageable);
        return exercises.map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public ExerciseDTO getExerciseById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", id));
        return mapToDTO(exercise);
    }

    private ExerciseDTO mapToDTO(Exercise exercise) {
        return ExerciseDTO.builder()
                .id(exercise.getId())
                .externalId(exercise.getExternalId())
                .name(exercise.getName())
                .bodyParts(exercise.getBodyParts())
                .targetMuscles(exercise.getTargetMuscles())
                .secondaryMuscles(exercise.getSecondaryMuscles())
                .equipments(exercise.getEquipments())
                .instructions(exercise.getInstructions())
                .gifUrl(exercise.getGifUrl())
                .source(exercise.getSource())
                .createdAt(exercise.getCreatedAt())
                .build();
    }
}
