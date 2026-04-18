package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.*;
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
    public PaginatedResponseDTO<DataRecordDTO> getRecords(int page, int pageSize, String status,
                                                           String type, String source,
                                                           String dateFrom, String dateTo) {
        List<String> dbStatuses = toDbStatuses(status);
        List<DataRecordDTO> all = collectFiltered(dbStatuses, type, source, parseDate(dateFrom, false), parseDate(dateTo, true));

        int total = all.size();
        int fromIndex = Math.min((page - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<DataRecordDTO> pageData = all.subList(fromIndex, toIndex);

        return new PaginatedResponseDTO<>(pageData, total, page, pageSize);
    }

    @Transactional(readOnly = true)
    public ValidationCountsDTO getCounts() {
        // "pending" couvre les records en attente de validation : le default DB BRUT
        // et le statut post-ETL NETTOYE (ecrit par les cleaners Python).
        long pending = biometricRepo.countByStatus("BRUT")
                + nutritionRepo.countByStatus("BRUT")
                + exerciseRepo.countByStatus("BRUT")
                + biometricRepo.countByStatus("NETTOYE")
                + nutritionRepo.countByStatus("NETTOYE")
                + exerciseRepo.countByStatus("NETTOYE");
        long approvedCleaned = biometricRepo.countByStatus("CLEANED")
                + nutritionRepo.countByStatus("CLEANED")
                + exerciseRepo.countByStatus("CLEANED");
        long approvedValidated = biometricRepo.countByStatus("VALIDATED")
                + nutritionRepo.countByStatus("VALIDATED")
                + exerciseRepo.countByStatus("VALIDATED");
        long approved = approvedCleaned + approvedValidated;
        long rejected = biometricRepo.countByStatus("REJECTED")
                + nutritionRepo.countByStatus("REJECTED")
                + exerciseRepo.countByStatus("REJECTED");
        long total = pending + approved + rejected;
        return new ValidationCountsDTO(pending, approved, rejected, total);
    }

    @Transactional(readOnly = true)
    public List<String> getFilteredIds(String status, String type, String source,
                                        String dateFrom, String dateTo) {
        List<String> dbStatuses = toDbStatuses(status);
        LocalDateTime from = parseDate(dateFrom, false);
        LocalDateTime to = parseDate(dateTo, true);
        List<String> ids = new ArrayList<>();

        if (type == null || "biometric".equals(type)) {
            biometricRepo.findFilteredIds(dbStatuses, source, from, to)
                    .forEach(id -> ids.add("biometric_" + id));
        }
        if (type == null || "nutrition".equals(type)) {
            nutritionRepo.findFilteredIds(dbStatuses, source, from, to)
                    .forEach(id -> ids.add("nutrition_" + id));
        }
        if (type == null || "exercise".equals(type)) {
            exerciseRepo.findFilteredIds(dbStatuses, source, from, to)
                    .forEach(id -> ids.add("exercise_" + id));
        }
        return ids;
    }

    @Transactional(readOnly = true)
    public List<DataRecordDTO> getSample(int size, String status, String type, String source,
                                          String dateFrom, String dateTo) {
        List<String> dbStatuses = toDbStatuses(status);
        List<DataRecordDTO> all = collectFiltered(dbStatuses, type, source, parseDate(dateFrom, false), parseDate(dateTo, true));
        if (all.size() <= size) return all;
        Collections.shuffle(all, new Random());
        return new ArrayList<>(all.subList(0, size));
    }

    @Transactional(readOnly = true)
    public SourcesByTypeDTO getSources() {
        return new SourcesByTypeDTO(
                biometricRepo.findDistinctSources(),
                nutritionRepo.findDistinctSources(),
                exerciseRepo.findDistinctSources()
        );
    }

    @Transactional
    public BulkValidateResponseDTO bulkValidate(List<String> ids, String validationStatus) {
        if (ids == null || ids.isEmpty()) {
            return new BulkValidateResponseDTO(0, 0, new ArrayList<>());
        }
        String dbStatus = "approved".equals(validationStatus) ? "CLEANED" : "REJECTED";

        List<Long> biometricIds = new ArrayList<>();
        List<Long> nutritionIds = new ArrayList<>();
        List<Long> exerciseIds = new ArrayList<>();
        List<String> failedIds = new ArrayList<>();

        for (String compositeId : ids) {
            String[] parts = compositeId.split("_", 2);
            if (parts.length != 2) {
                failedIds.add(compositeId);
                continue;
            }
            try {
                Long id = Long.parseLong(parts[1]);
                switch (parts[0]) {
                    case "biometric" -> biometricIds.add(id);
                    case "nutrition" -> nutritionIds.add(id);
                    case "exercise" -> exerciseIds.add(id);
                    default -> failedIds.add(compositeId);
                }
            } catch (NumberFormatException e) {
                failedIds.add(compositeId);
            }
        }

        int updated = 0;
        updated += updateBiometricBatch(biometricIds, dbStatus);
        updated += updateNutritionBatch(nutritionIds, dbStatus);
        updated += updateExerciseBatch(exerciseIds, dbStatus);

        return new BulkValidateResponseDTO(updated, failedIds.size(), failedIds);
    }

    private int updateBiometricBatch(List<Long> ids, String dbStatus) {
        if (ids.isEmpty()) return 0;
        List<BiometricEntry> entries = biometricRepo.findAllById(ids);
        entries.forEach(e -> e.setStatus(dbStatus));
        biometricRepo.saveAll(entries);
        return entries.size();
    }

    private int updateNutritionBatch(List<Long> ids, String dbStatus) {
        if (ids.isEmpty()) return 0;
        List<NutritionEntry> entries = nutritionRepo.findAllById(ids);
        entries.forEach(e -> e.setStatus(dbStatus));
        nutritionRepo.saveAll(entries);
        return entries.size();
    }

    private int updateExerciseBatch(List<Long> ids, String dbStatus) {
        if (ids.isEmpty()) return 0;
        List<ExerciseEntry> entries = exerciseRepo.findAllById(ids);
        entries.forEach(e -> e.setStatus(dbStatus));
        exerciseRepo.saveAll(entries);
        return entries.size();
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

    // --- helpers ---

    /**
     * Parse une date reçue en query-string en LocalDateTime tolérant plusieurs formats :
     * - "2026-04-17" → 00:00:00 ou 23:59:59 selon endOfDay
     * - "2026-04-17T00:00:00"
     * - "2026-04-17T00:00:00.000Z"
     * Retourne null si la chaîne est vide / invalide.
     */
    private LocalDateTime parseDate(String raw, boolean endOfDay) {
        if (raw == null || raw.isBlank()) return null;
        String trimmed = raw.trim();
        try {
            if (trimmed.length() == 10) {
                return endOfDay
                        ? java.time.LocalDate.parse(trimmed).atTime(23, 59, 59)
                        : java.time.LocalDate.parse(trimmed).atStartOfDay();
            }
            String noZ = trimmed.endsWith("Z") ? trimmed.substring(0, trimmed.length() - 1) : trimmed;
            int dotIndex = noZ.indexOf('.');
            if (dotIndex > 0) noZ = noZ.substring(0, dotIndex);
            return LocalDateTime.parse(noZ);
        } catch (Exception e) {
            return null;
        }
    }

    private List<DataRecordDTO> collectFiltered(List<String> dbStatuses, String type, String source,
                                                 LocalDateTime dateFrom, LocalDateTime dateTo) {
        List<DataRecordDTO> biometricRecords = (type == null || "biometric".equals(type))
                ? biometricRepo.findFiltered(dbStatuses, source, dateFrom, dateTo).stream()
                        .map(this::toRecord).toList()
                : List.of();
        List<DataRecordDTO> nutritionRecords = (type == null || "nutrition".equals(type))
                ? nutritionRepo.findFiltered(dbStatuses, source, dateFrom, dateTo).stream()
                        .map(this::toRecord).toList()
                : List.of();
        List<DataRecordDTO> exerciseRecords = (type == null || "exercise".equals(type))
                ? exerciseRepo.findFiltered(dbStatuses, source, dateFrom, dateTo).stream()
                        .map(this::toRecord).toList()
                : List.of();

        return Stream.of(biometricRecords, nutritionRecords, exerciseRecords)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(DataRecordDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
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
            case "pending" -> "NETTOYE";
            case "approved" -> "CLEANED";
            case "rejected" -> "REJECTED";
            default -> null;
        };
    }

    // Plusieurs statuts DB peuvent correspondre à un même statut de validation cote API :
    // - pending : les records BRUT (non traites par ETL) ET NETTOYE (traites mais en attente de validation)
    // - approved : CLEANED et VALIDATED
    // - rejected : REJECTED
    // - null (pas de filtre) : tous les statuts connus
    private static final List<String> ALL_DB_STATUSES = List.of("BRUT", "NETTOYE", "CLEANED", "VALIDATED", "REJECTED");

    private List<String> toDbStatuses(String validationStatus) {
        if (validationStatus == null) return ALL_DB_STATUSES;
        return switch (validationStatus) {
            case "pending" -> List.of("BRUT", "NETTOYE");
            case "approved" -> List.of("CLEANED", "VALIDATED");
            case "rejected" -> List.of("REJECTED");
            default -> ALL_DB_STATUSES;
        };
    }
}
