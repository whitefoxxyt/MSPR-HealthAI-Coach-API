package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.DataRecordDTO;
import fr.epsi.healthaicoachapi.dto.PaginatedResponseDTO;
import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import fr.epsi.healthaicoachapi.entity.ExerciseEntry;
import fr.epsi.healthaicoachapi.entity.NutritionEntry;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.repository.BiometricEntryRepository;
import fr.epsi.healthaicoachapi.repository.ExerciseEntryRepository;
import fr.epsi.healthaicoachapi.repository.NutritionEntryRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ValidationService {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final BiometricEntryRepository biometricRepo;
    private final NutritionEntryRepository nutritionRepo;
    private final ExerciseEntryRepository exerciseRepo;

    public ValidationService(BiometricEntryRepository biometricRepo,
                              NutritionEntryRepository nutritionRepo,
                              ExerciseEntryRepository exerciseRepo) {
        this.biometricRepo = biometricRepo;
        this.nutritionRepo = nutritionRepo;
        this.exerciseRepo = exerciseRepo;
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<DataRecordDTO> getRecords(int page, int pageSize, String status) {
        String dbStatus = toDbStatus(status);

        List<DataRecordDTO> biometricRecords = biometricRepo.findAll().stream()
                .filter(b -> dbStatus == null || dbStatus.equals(b.getStatus()))
                .map(this::toRecord)
                .collect(Collectors.toList());

        List<DataRecordDTO> nutritionRecords = nutritionRepo.findAll().stream()
                .filter(n -> dbStatus == null || dbStatus.equals(n.getStatus()))
                .map(this::toRecord)
                .collect(Collectors.toList());

        List<DataRecordDTO> exerciseRecords = exerciseRepo.findAll().stream()
                .filter(e -> dbStatus == null || dbStatus.equals(e.getStatus()))
                .map(this::toRecord)
                .collect(Collectors.toList());

        List<DataRecordDTO> all = Stream.of(biometricRecords, nutritionRecords, exerciseRecords)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(DataRecordDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());

        int total = all.size();
        int fromIndex = Math.min((page - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<DataRecordDTO> pageData = all.subList(fromIndex, toIndex);

        return new PaginatedResponseDTO<>(pageData, total, page, pageSize);
    }

    @Transactional
    public DataRecordDTO validateRecord(String compositeId, String validationStatus) {
        String[] parts = compositeId.split("_", 2);
        if (parts.length != 2) {
            throw new ResourceNotFoundException("Record not found: " + compositeId);
        }
        String entityType = parts[0];
        Long entryId = Long.parseLong(parts[1]);
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String dbStatus = "approved".equals(validationStatus) ? "CLEANED" : "REJECTED";
        String now = LocalDateTime.now().format(ISO) + "Z";

        return switch (entityType) {
            case "biometric" -> {
                BiometricEntry b = biometricRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("BiometricEntry", entryId));
                b.setStatus(dbStatus);
                biometricRepo.save(b);
                DataRecordDTO dto = toRecord(b);
                dto.setStatus(validationStatus);
                dto.setValidatedBy(email);
                dto.setValidatedAt(now);
                yield dto;
            }
            case "nutrition" -> {
                NutritionEntry n = nutritionRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("NutritionEntry", entryId));
                n.setStatus(dbStatus);
                nutritionRepo.save(n);
                DataRecordDTO dto = toRecord(n);
                dto.setStatus(validationStatus);
                dto.setValidatedBy(email);
                dto.setValidatedAt(now);
                yield dto;
            }
            case "exercise" -> {
                ExerciseEntry e = exerciseRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("ExerciseEntry", entryId));
                e.setStatus(dbStatus);
                exerciseRepo.save(e);
                DataRecordDTO dto = toRecord(e);
                dto.setStatus(validationStatus);
                dto.setValidatedBy(email);
                dto.setValidatedAt(now);
                yield dto;
            }
            default -> throw new ResourceNotFoundException("Unknown entity type: " + entityType);
        };
    }

    @Transactional
    public DataRecordDTO updateRecord(String compositeId, Map<String, Object> updates) {
        String[] parts = compositeId.split("_", 2);
        if (parts.length != 2) {
            throw new ResourceNotFoundException("Record not found: " + compositeId);
        }
        String entityType = parts[0];
        Long entryId = Long.parseLong(parts[1]);

        return switch (entityType) {
            case "biometric" -> {
                BiometricEntry b = biometricRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("BiometricEntry", entryId));
                biometricRepo.save(b);
                yield toRecord(b);
            }
            case "nutrition" -> {
                NutritionEntry n = nutritionRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("NutritionEntry", entryId));
                nutritionRepo.save(n);
                yield toRecord(n);
            }
            case "exercise" -> {
                ExerciseEntry e = exerciseRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("ExerciseEntry", entryId));
                exerciseRepo.save(e);
                yield toRecord(e);
            }
            default -> throw new ResourceNotFoundException("Unknown entity type: " + entityType);
        };
    }

    // --- mappers ---

    private DataRecordDTO toRecord(BiometricEntry b) {
        DataRecordDTO dto = new DataRecordDTO();
        dto.setId("biometric_" + b.getId());
        dto.setType("biometric");
        dto.setStatus(toValidationStatus(b.getStatus()));
        dto.setCreatedAt(b.getCreatedAt().format(ISO) + "Z");
        dto.setUpdatedAt(b.getCreatedAt().format(ISO) + "Z");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("weightKg", b.getWeightKg());
        data.put("heightCm", b.getHeightCm());
        data.put("bmi", b.getBmi());
        data.put("heartRateMax", b.getHeartRateMax());
        data.put("heartRateAvg", b.getHeartRateAvg());
        data.put("source", b.getSource());
        dto.setData(data);
        return dto;
    }

    private DataRecordDTO toRecord(NutritionEntry n) {
        DataRecordDTO dto = new DataRecordDTO();
        dto.setId("nutrition_" + n.getId());
        dto.setType("nutrition");
        dto.setStatus(toValidationStatus(n.getStatus()));
        dto.setCreatedAt(n.getCreatedAt().format(ISO) + "Z");
        dto.setUpdatedAt(n.getCreatedAt().format(ISO) + "Z");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("foodName", n.getFoodName());
        data.put("calories", n.getCalories());
        data.put("proteinG", n.getProteinG());
        data.put("carbsG", n.getCarbsG());
        data.put("fatG", n.getFatG());
        data.put("source", n.getSource());
        dto.setData(data);
        return dto;
    }

    private DataRecordDTO toRecord(ExerciseEntry e) {
        DataRecordDTO dto = new DataRecordDTO();
        dto.setId("exercise_" + e.getId());
        dto.setType("exercise");
        dto.setStatus(toValidationStatus(e.getStatus()));
        dto.setCreatedAt(e.getCreatedAt().format(ISO) + "Z");
        dto.setUpdatedAt(e.getCreatedAt().format(ISO) + "Z");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("workoutType", e.getWorkoutType());
        data.put("durationMin", e.getDurationMin());
        data.put("caloriesBurned", e.getCaloriesBurned());
        data.put("heartRateAvg", e.getHeartRateAvg());
        data.put("source", e.getSource());
        dto.setData(data);
        return dto;
    }

    private String toValidationStatus(String dbStatus) {
        if (dbStatus == null) return "pending";
        return switch (dbStatus) {
            case "CLEANED", "VALIDATED" -> "approved";
            case "REJECTED" -> "rejected";
            default -> "pending";
        };
    }

    private String toDbStatus(String validationStatus) {
        if (validationStatus == null) return null;
        return switch (validationStatus) {
            case "pending" -> "BRUT";
            case "approved" -> "CLEANED";
            case "rejected" -> "REJECTED";
            default -> null;
        };
    }
}
