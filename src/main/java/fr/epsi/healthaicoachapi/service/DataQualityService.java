package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.*;
import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import fr.epsi.healthaicoachapi.entity.ExerciseEntry;
import fr.epsi.healthaicoachapi.entity.NutritionEntry;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DataQualityService {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final BigDecimal MAX_CALORIES = new BigDecimal("5000");
    private static final BigDecimal BMI_MAX = new BigDecimal("60");
    private static final BigDecimal BMI_MIN = new BigDecimal("10");
    private static final int HEART_RATE_MAX_THRESHOLD = 200;

    // Seuils physiologiques biometric_entries (colonnes weight_kg, height_cm, fat_percentage, heart_rate_rest)
    private static final BigDecimal WEIGHT_MIN = new BigDecimal("30");
    private static final BigDecimal WEIGHT_MAX = new BigDecimal("300");
    private static final BigDecimal HEIGHT_MIN = new BigDecimal("100");
    private static final BigDecimal HEIGHT_MAX = new BigDecimal("250");
    private static final BigDecimal FAT_MIN = new BigDecimal("3");
    private static final BigDecimal FAT_MAX = new BigDecimal("60");
    private static final int HR_REST_MIN = 30;
    private static final int HR_REST_MAX = 130;

    // Seuils nutrition_entries (par entree)
    private static final BigDecimal PROTEIN_MAX = new BigDecimal("500");
    private static final BigDecimal CARBS_MAX = new BigDecimal("1000");
    private static final BigDecimal FAT_G_MAX = new BigDecimal("500");
    private static final BigDecimal SODIUM_MAX = new BigDecimal("10000");

    // Seuils activity exercise_entries
    private static final BigDecimal DURATION_MAX = new BigDecimal("600"); // 10h
    private static final BigDecimal CALORIES_BURNED_MAX = new BigDecimal("3000");
    private static final int STEPS_MAX = 100_000;

    private final UserRepository userRepository;
    private final BiometricEntryRepository biometricRepo;
    private final NutritionEntryRepository nutritionRepo;
    private final ExerciseEntryRepository exerciseRepo;
    private final DietRecommendationRepository dietRepo;

    public DataQualityService(UserRepository userRepository,
                               BiometricEntryRepository biometricRepo,
                               NutritionEntryRepository nutritionRepo,
                               ExerciseEntryRepository exerciseRepo,
                               DietRecommendationRepository dietRepo) {
        this.userRepository = userRepository;
        this.biometricRepo = biometricRepo;
        this.nutritionRepo = nutritionRepo;
        this.exerciseRepo = exerciseRepo;
        this.dietRepo = dietRepo;
    }

    public DataQualityMetricsDTO getMetrics() {
        long biometricTotal = biometricRepo.count();
        long nutritionTotal = nutritionRepo.count();
        long exerciseTotal = exerciseRepo.count();
        long userTotal = userRepository.count();
        long dietTotal = dietRepo.count();
        long totalRecords = biometricTotal + nutritionTotal + exerciseTotal + userTotal + dietTotal;

        List<DataAnomalyDTO> anomalies = detectAnomalies();
        long anomalyCount = anomalies.size();

        long brutBiometric = biometricRepo.countByStatus("BRUT");
        long brutNutrition = nutritionRepo.countByStatus("BRUT");
        long brutExercise = exerciseRepo.countByStatus("BRUT");
        long missingValues = brutBiometric + brutNutrition + brutExercise;

        double completenessRate = totalRecords > 0
                ? Math.round(((double)(totalRecords - anomalyCount) / totalRecords) * 1000.0) / 10.0
                : 100.0;

        LocalDateTime today = LocalDate.now().atStartOfDay();
        long todayBiometric = biometricRepo.countByCreatedAtAfter(today);
        long todayNutrition = nutritionRepo.countByCreatedAtAfter(today);
        long todayExercise = exerciseRepo.countByCreatedAtAfter(today);
        String dataFlowStatus = (todayBiometric + todayNutrition + todayExercise) > 0 ? "active" : "inactive";

        LocalDateTime latestBiometric = biometricRepo.findLatestCreatedAt();
        LocalDateTime latestNutrition = nutritionRepo.findLatestCreatedAt();
        LocalDateTime latestExercise = exerciseRepo.findLatestCreatedAt();
        LocalDateTime latestDiet = dietRepo.findLatestCreatedAt();
        LocalDateTime lastUpdate = max(latestDiet, max(latestBiometric, max(latestNutrition, latestExercise)));
        String lastUpdateStr = lastUpdate != null ? lastUpdate.format(ISO) : LocalDateTime.now().format(ISO);

        DataQualityMetricsDTO dto = new DataQualityMetricsDTO();
        dto.setTotalRecords(totalRecords);
        dto.setMissingValues(missingValues);
        dto.setDuplicates(0);
        dto.setAnomalies(anomalyCount);
        dto.setCompletenessRate(completenessRate);
        dto.setDataFlowStatus(dataFlowStatus);
        dto.setLastUpdate(lastUpdateStr + "Z");
        return dto;
    }

    public List<DataAnomalyDTO> detectAnomalies() {
        List<DataAnomalyDTO> anomalies = new ArrayList<>();

        // ---- Biometric : HR max + BMI (existant) ----
        List<BiometricEntry> hrBmiOutliers = biometricRepo.findOutliers(
                HEART_RATE_MAX_THRESHOLD, BMI_MAX, BMI_MIN);
        for (BiometricEntry b : hrBmiOutliers) {
            if (b.getHeartRateMax() != null && b.getHeartRateMax() > HEART_RATE_MAX_THRESHOLD) {
                anomalies.add(buildAnomaly(
                        "biometric_hr_" + b.getId(), "outlier", "biometric", String.valueOf(b.getId()),
                        "heartRateMax", String.valueOf(b.getHeartRateMax()), "75",
                        b.getCreatedAt(), "high"));
            }
            if (b.getBmi() != null && (b.getBmi().compareTo(BMI_MAX) > 0 || b.getBmi().compareTo(BMI_MIN) < 0)) {
                anomalies.add(buildAnomaly(
                        "biometric_bmi_" + b.getId(), "outlier", "biometric", String.valueOf(b.getId()),
                        "bmi", b.getBmi().toPlainString(), "22",
                        b.getCreatedAt(), "medium"));
            }
        }

        // ---- Biometric : poids, taille, masse grasse, FC repos (physiologiques) ----
        List<BiometricEntry> physioOutliers = biometricRepo.findPhysiologicalOutliers(
                WEIGHT_MIN, WEIGHT_MAX, HEIGHT_MIN, HEIGHT_MAX,
                FAT_MIN, FAT_MAX, HR_REST_MIN, HR_REST_MAX);
        for (BiometricEntry b : physioOutliers) {
            if (b.getWeightKg() != null &&
                    (b.getWeightKg().compareTo(WEIGHT_MIN) < 0 || b.getWeightKg().compareTo(WEIGHT_MAX) > 0)) {
                anomalies.add(buildAnomaly(
                        "biometric_weight_" + b.getId(), "outlier", "biometric", String.valueOf(b.getId()),
                        "weightKg", b.getWeightKg().toPlainString(), "70",
                        b.getCreatedAt(), "medium"));
            }
            if (b.getHeightCm() != null &&
                    (b.getHeightCm().compareTo(HEIGHT_MIN) < 0 || b.getHeightCm().compareTo(HEIGHT_MAX) > 0)) {
                anomalies.add(buildAnomaly(
                        "biometric_height_" + b.getId(), "outlier", "biometric", String.valueOf(b.getId()),
                        "heightCm", b.getHeightCm().toPlainString(), "170",
                        b.getCreatedAt(), "medium"));
            }
            if (b.getFatPercentage() != null &&
                    (b.getFatPercentage().compareTo(FAT_MIN) < 0 || b.getFatPercentage().compareTo(FAT_MAX) > 0)) {
                anomalies.add(buildAnomaly(
                        "biometric_fat_" + b.getId(), "outlier", "biometric", String.valueOf(b.getId()),
                        "fatPercentage", b.getFatPercentage().toPlainString(), "20",
                        b.getCreatedAt(), "medium"));
            }
            if (b.getHeartRateRest() != null &&
                    (b.getHeartRateRest() < HR_REST_MIN || b.getHeartRateRest() > HR_REST_MAX)) {
                anomalies.add(buildAnomaly(
                        "biometric_hrrest_" + b.getId(), "outlier", "biometric", String.valueOf(b.getId()),
                        "heartRateRest", String.valueOf(b.getHeartRateRest()), "65",
                        b.getCreatedAt(), "high"));
            }
        }

        // ---- Nutrition : calories (existant) ----
        List<NutritionEntry> calorieOutliers = nutritionRepo.findCalorieOutliers(MAX_CALORIES);
        for (NutritionEntry n : calorieOutliers) {
            anomalies.add(buildAnomaly(
                    "nutrition_cal_" + n.getId(), "outlier", "nutrition", String.valueOf(n.getId()),
                    "calories", n.getCalories() != null ? n.getCalories().toPlainString() : null, "500",
                    n.getCreatedAt(), "high"));
        }

        // ---- Nutrition : macros (proteines, glucides, lipides, sodium) ----
        List<NutritionEntry> macroOutliers = nutritionRepo.findMacroOutliers(
                PROTEIN_MAX, CARBS_MAX, FAT_G_MAX, SODIUM_MAX);
        for (NutritionEntry n : macroOutliers) {
            if (n.getProteinG() != null &&
                    (n.getProteinG().signum() < 0 || n.getProteinG().compareTo(PROTEIN_MAX) > 0)) {
                anomalies.add(buildAnomaly(
                        "nutrition_protein_" + n.getId(), "outlier", "nutrition", String.valueOf(n.getId()),
                        "proteinG", n.getProteinG().toPlainString(), "25",
                        n.getCreatedAt(), "medium"));
            }
            if (n.getCarbsG() != null &&
                    (n.getCarbsG().signum() < 0 || n.getCarbsG().compareTo(CARBS_MAX) > 0)) {
                anomalies.add(buildAnomaly(
                        "nutrition_carbs_" + n.getId(), "outlier", "nutrition", String.valueOf(n.getId()),
                        "carbsG", n.getCarbsG().toPlainString(), "60",
                        n.getCreatedAt(), "medium"));
            }
            if (n.getFatG() != null &&
                    (n.getFatG().signum() < 0 || n.getFatG().compareTo(FAT_G_MAX) > 0)) {
                anomalies.add(buildAnomaly(
                        "nutrition_fat_" + n.getId(), "outlier", "nutrition", String.valueOf(n.getId()),
                        "fatG", n.getFatG().toPlainString(), "20",
                        n.getCreatedAt(), "medium"));
            }
            if (n.getSodiumMg() != null &&
                    (n.getSodiumMg().signum() < 0 || n.getSodiumMg().compareTo(SODIUM_MAX) > 0)) {
                anomalies.add(buildAnomaly(
                        "nutrition_sodium_" + n.getId(), "outlier", "nutrition", String.valueOf(n.getId()),
                        "sodiumMg", n.getSodiumMg().toPlainString(), "500",
                        n.getCreatedAt(), "high"));
            }
        }

        // ---- Exercise : duration, calories, steps (activite) ----
        List<ExerciseEntry> activityOutliers = exerciseRepo.findActivityOutliers(
                DURATION_MAX, CALORIES_BURNED_MAX, STEPS_MAX);
        for (ExerciseEntry e : activityOutliers) {
            if (e.getDurationMin() != null && e.getDurationMin().compareTo(DURATION_MAX) > 0) {
                anomalies.add(buildAnomaly(
                        "exercise_dur_" + e.getId(), "outlier", "exercise", String.valueOf(e.getId()),
                        "durationMin", e.getDurationMin().toPlainString(), "60",
                        e.getCreatedAt(), "medium"));
            }
            if (e.getCaloriesBurned() != null && e.getCaloriesBurned().compareTo(CALORIES_BURNED_MAX) > 0) {
                anomalies.add(buildAnomaly(
                        "exercise_cal_" + e.getId(), "outlier", "exercise", String.valueOf(e.getId()),
                        "caloriesBurned", e.getCaloriesBurned().toPlainString(), "500",
                        e.getCreatedAt(), "medium"));
            }
            if (e.getSteps() != null && e.getSteps() > STEPS_MAX) {
                anomalies.add(buildAnomaly(
                        "exercise_steps_" + e.getId(), "outlier", "exercise", String.valueOf(e.getId()),
                        "steps", String.valueOf(e.getSteps()), "8000",
                        e.getCreatedAt(), "medium"));
            }
        }

        return anomalies;
    }

    @Transactional
    public DataAnomalyDTO resolveAnomaly(String anomalyId, Map<String, Object> updates) {
        // Format anomaly id : "{entityType}_{checkType}_{entryId}"
        // entityType : biometric | nutrition | exercise
        String[] parts = anomalyId.split("_");
        if (parts.length < 3) {
            throw new ResourceNotFoundException("Anomaly not found: " + anomalyId);
        }
        String entityType = parts[0];
        Long entryId = Long.parseLong(parts[parts.length - 1]);

        switch (entityType) {
            case "biometric" -> {
                BiometricEntry entry = biometricRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("BiometricEntry", entryId));
                entry.setStatus("CLEANED");
                biometricRepo.save(entry);
            }
            case "nutrition" -> {
                NutritionEntry entry = nutritionRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("NutritionEntry", entryId));
                entry.setStatus("CLEANED");
                nutritionRepo.save(entry);
            }
            case "exercise" -> {
                ExerciseEntry entry = exerciseRepo.findById(entryId)
                        .orElseThrow(() -> new ResourceNotFoundException("ExerciseEntry", entryId));
                entry.setStatus("CLEANED");
                exerciseRepo.save(entry);
            }
            default -> throw new ResourceNotFoundException("Unknown entity type: " + entityType);
        }

        DataAnomalyDTO dto = new DataAnomalyDTO();
        dto.setId(anomalyId);
        dto.setStatus("approved");
        return dto;
    }

    @Transactional
    public void deleteAnomaly(String anomalyId) {
        String[] parts = anomalyId.split("_");
        if (parts.length < 3) return;
        String entityType = parts[0];
        Long entryId = Long.parseLong(parts[parts.length - 1]);

        switch (entityType) {
            case "biometric" -> biometricRepo.findById(entryId).ifPresent(b -> {
                b.setStatus("REJECTED");
                biometricRepo.save(b);
            });
            case "nutrition" -> nutritionRepo.findById(entryId).ifPresent(n -> {
                n.setStatus("REJECTED");
                nutritionRepo.save(n);
            });
            case "exercise" -> exerciseRepo.findById(entryId).ifPresent(e -> {
                e.setStatus("REJECTED");
                exerciseRepo.save(e);
            });
            default -> { /* ignore */ }
        }
    }

    public List<DataFlowStatsDTO> getDataFlowStats() {
        LocalDateTime today = LocalDate.now().atStartOfDay();

        LocalDateTime latestBiometric = biometricRepo.findLatestCreatedAt();
        LocalDateTime latestNutrition = nutritionRepo.findLatestCreatedAt();
        LocalDateTime latestExercise = exerciseRepo.findLatestCreatedAt();

        List<DataAnomalyDTO> anomalies = detectAnomalies();
        long totalEntries = biometricRepo.count() + nutritionRepo.count() + exerciseRepo.count() + dietRepo.count();
        double globalErrorRate = totalEntries > 0
                ? Math.round(((double) anomalies.size() / totalEntries) * 1000.0) / 10.0
                : 0.0;

        List<DataFlowStatsDTO> flows = new ArrayList<>();

        DataFlowStatsDTO userFlow = new DataFlowStatsDTO();
        userFlow.setName("Profils utilisateurs");
        userFlow.setType("user");
        long userToday = userRepository.countByLastActivityAfter(today);
        userFlow.setRecordsToday(userToday);
        userFlow.setLastSync(LocalDateTime.now().format(ISO) + "Z");
        userFlow.setStatus(userToday > 0 ? "active" : "inactive");
        userFlow.setErrorRate(0.0);
        flows.add(userFlow);

        DataFlowStatsDTO nutritionFlow = new DataFlowStatsDTO();
        nutritionFlow.setName("Données nutritionnelles");
        nutritionFlow.setType("nutrition");
        nutritionFlow.setRecordsToday(nutritionRepo.countByCreatedAtAfter(today));
        nutritionFlow.setLastSync(latestNutrition != null ? latestNutrition.format(ISO) + "Z" : LocalDateTime.now().format(ISO) + "Z");
        nutritionFlow.setStatus(nutritionRepo.countByCreatedAtAfter(today) > 0 ? "active" : "inactive");
        nutritionFlow.setErrorRate(globalErrorRate);
        flows.add(nutritionFlow);

        DataFlowStatsDTO exerciseFlow = new DataFlowStatsDTO();
        exerciseFlow.setName("Exercices");
        exerciseFlow.setType("exercise");
        exerciseFlow.setRecordsToday(exerciseRepo.countByCreatedAtAfter(today));
        exerciseFlow.setLastSync(latestExercise != null ? latestExercise.format(ISO) + "Z" : LocalDateTime.now().format(ISO) + "Z");
        exerciseFlow.setStatus(exerciseRepo.countByCreatedAtAfter(today) > 0 ? "active" : "inactive");
        exerciseFlow.setErrorRate(0.0);
        flows.add(exerciseFlow);

        DataFlowStatsDTO biometricFlow = new DataFlowStatsDTO();
        biometricFlow.setName("Métriques biométriques");
        biometricFlow.setType("biometric");
        biometricFlow.setRecordsToday(biometricRepo.countByCreatedAtAfter(today));
        biometricFlow.setLastSync(latestBiometric != null ? latestBiometric.format(ISO) + "Z" : LocalDateTime.now().format(ISO) + "Z");
        biometricFlow.setStatus(biometricRepo.countByCreatedAtAfter(today) > 0 ? "active" : "inactive");
        biometricFlow.setErrorRate(globalErrorRate);
        flows.add(biometricFlow);

        LocalDateTime latestDiet = dietRepo.findLatestCreatedAt();
        DataFlowStatsDTO dietFlow = new DataFlowStatsDTO();
        dietFlow.setName("Recommandations alimentaires");
        dietFlow.setType("diet");
        dietFlow.setRecordsToday(dietRepo.countByCreatedAtAfter(today));
        dietFlow.setLastSync(latestDiet != null ? latestDiet.format(ISO) + "Z" : LocalDateTime.now().format(ISO) + "Z");
        dietFlow.setStatus(latestDiet != null ? "active" : "inactive");
        dietFlow.setErrorRate(0.0);
        flows.add(dietFlow);

        return flows;
    }

    public AnalyticsOverviewDTO getAnalyticsOverview() {
        AnalyticsOverviewDTO overview = new AnalyticsOverviewDTO();
        long totalUsers = userRepository.count();

        // Age distribution
        Object[] ageDist = userRepository.ageDistribution();
        List<AnalyticsOverviewDTO.DistributionItem> ageDistList = new ArrayList<>();
        if (ageDist != null && ageDist.length == 4) {
            ageDistList.add(new AnalyticsOverviewDTO.DistributionItem("18-25 ans", toLong(ageDist[0])));
            ageDistList.add(new AnalyticsOverviewDTO.DistributionItem("26-35 ans", toLong(ageDist[1])));
            ageDistList.add(new AnalyticsOverviewDTO.DistributionItem("36-50 ans", toLong(ageDist[2])));
            ageDistList.add(new AnalyticsOverviewDTO.DistributionItem("50+ ans", toLong(ageDist[3])));
        }
        overview.setAgeDistribution(ageDistList);

        // Objective distribution
        List<Object[]> objDist = userRepository.countByObjective();
        List<AnalyticsOverviewDTO.DistributionItem> objDistList = objDist.stream()
                .map(row -> new AnalyticsOverviewDTO.DistributionItem(
                        String.valueOf(row[0]), toLong(row[1])))
                .collect(Collectors.toList());
        overview.setObjectiveDistribution(objDistList);

        // Progression rates (% of users with activity in each period)
        LocalDateTime now = LocalDateTime.now();
        long active7d = userRepository.countByLastActivityAfter(now.minusDays(7));
        long active30d = userRepository.countByLastActivityAfter(now.minusDays(30));
        long active90d = userRepository.countByLastActivityAfter(now.minusDays(90));
        Map<String, Double> progressionRate = new LinkedHashMap<>();
        progressionRate.put("7d", totalUsers > 0 ? round1((double) active7d / totalUsers * 100) : 0.0);
        progressionRate.put("30d", totalUsers > 0 ? round1((double) active30d / totalUsers * 100) : 0.0);
        progressionRate.put("90d", totalUsers > 0 ? round1((double) active90d / totalUsers * 100) : 0.0);
        overview.setProgressionRateByPeriod(progressionRate);

        // Daily trends from real data
        overview.setUserProgressionTrend(buildTrendFromRows(
                exerciseRepo.countByDaySince(now.minusDays(7)),
                exerciseRepo.countByDaySince(now.minusDays(30)),
                exerciseRepo.countByDaySince(now.minusDays(90))));
        overview.setFoodTrends(buildTrendFromRows(
                nutritionRepo.countByDaySince(now.minusDays(7)),
                nutritionRepo.countByDaySince(now.minusDays(30)),
                nutritionRepo.countByDaySince(now.minusDays(90))));

        // Nutrition balance: deviation from 500 cal target per meal type
        List<Object[]> mealAvgs = nutritionRepo.avgCaloriesByMealType();
        List<AnalyticsOverviewDTO.NutritionBalance> nutritionBalance = mealAvgs.stream()
                .map(row -> {
                    String mealType = String.valueOf(row[0]);
                    double avg = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
                    double target = 500.0;
                    double deficit = avg < target ? round1(target - avg) : 0.0;
                    double excess = avg > target ? round1(avg - target) : 0.0;
                    return new AnalyticsOverviewDTO.NutritionBalance(mealType, deficit, excess);
                })
                .collect(Collectors.toList());
        overview.setNutritionBalanceByProfile(nutritionBalance);

        // Top exercises
        List<Object[]> topEx = exerciseRepo.countByWorkoutType();
        List<AnalyticsOverviewDTO.DistributionItem> topExList = topEx.stream()
                .limit(5)
                .map(row -> new AnalyticsOverviewDTO.DistributionItem(
                        String.valueOf(row[0]), toLong(row[1])))
                .collect(Collectors.toList());
        overview.setTopExercises(topExList);

        // Intensity levels
        long low = exerciseRepo.countLowIntensity();
        long moderate = exerciseRepo.countModerateIntensity();
        long high = exerciseRepo.countHighIntensity();
        long totalWithHR = low + moderate + high;
        List<AnalyticsOverviewDTO.DistributionItem> intensityList = new ArrayList<>();
        intensityList.add(new AnalyticsOverviewDTO.DistributionItem("Faible",
                totalWithHR > 0 ? round1((double) low / totalWithHR * 100) : 0));
        intensityList.add(new AnalyticsOverviewDTO.DistributionItem("Modérée",
                totalWithHR > 0 ? round1((double) moderate / totalWithHR * 100) : 0));
        intensityList.add(new AnalyticsOverviewDTO.DistributionItem("Élevée",
                totalWithHR > 0 ? round1((double) high / totalWithHR * 100) : 0));
        overview.setIntensityLevels(intensityList);

        // Business KPIs
        long premiumUsers = userRepository.countByIsPremiumTrue();
        double premiumRate = totalUsers > 0 ? round1((double) premiumUsers / totalUsers * 100) : 0.0;
        double engagementRate = progressionRate.getOrDefault("30d", 0.0);
        double satisfactionRate = round1(100.0 - (getMetrics().getAnomalies() > 0
                ? Math.min(10.0, getMetrics().getAnomalies() * 0.1)
                : 0.0));
        overview.setBusinessKpis(new AnalyticsOverviewDTO.BusinessKpis(engagementRate, premiumRate, satisfactionRate));

        return overview;
    }

    // --- helpers ---

    private DataAnomalyDTO buildAnomaly(String id, String type, String entityType, String entityId,
                                         String field, String currentValue, String suggestedValue,
                                         LocalDateTime detectedAt, String severity) {
        DataAnomalyDTO dto = new DataAnomalyDTO();
        dto.setId(id);
        dto.setType(type);
        dto.setEntityType(entityType);
        dto.setEntityId(entityId);
        dto.setField(field);
        dto.setCurrentValue(currentValue);
        dto.setSuggestedValue(suggestedValue);
        dto.setDetectedAt(detectedAt != null ? detectedAt.format(ISO) + "Z" : LocalDateTime.now().format(ISO) + "Z");
        dto.setSeverity(severity);
        dto.setStatus("pending");
        return dto;
    }

    private LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.isAfter(b) ? a : b;
    }

    private long toLong(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).longValue();
        return 0;
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private Map<String, List<AnalyticsOverviewDTO.TrendPoint>> buildTrendFromRows(
            List<Object[]> rows7d, List<Object[]> rows30d, List<Object[]> rows90d) {
        Map<String, List<AnalyticsOverviewDTO.TrendPoint>> trend = new LinkedHashMap<>();
        trend.put("7d", toTrendPoints(rows7d));
        trend.put("30d", toTrendPoints(rows30d));
        trend.put("90d", toTrendPoints(rows90d));
        return trend;
    }

    private List<AnalyticsOverviewDTO.TrendPoint> toTrendPoints(List<Object[]> rows) {
        return rows.stream()
                .map(row -> new AnalyticsOverviewDTO.TrendPoint(
                        String.valueOf(row[0]),
                        row[1] != null ? ((Number) row[1]).doubleValue() : 0.0))
                .collect(Collectors.toList());
    }
}
