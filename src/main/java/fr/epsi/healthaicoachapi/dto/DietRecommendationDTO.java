package fr.epsi.healthaicoachapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DietRecommendationDTO {

    private Long id;
    private String externalPatientId;
    private Integer age;
    private String gender;
    private BigDecimal weightKg;
    private BigDecimal heightCm;
    private BigDecimal bmi;
    private String diseaseType;
    private String severity;
    private String physicalActivityLevel;
    private BigDecimal dailyCaloricIntake;
    private BigDecimal cholesterolMgDl;
    private BigDecimal bloodPressureMmhg;
    private BigDecimal glucoseMgDl;
    private String dietaryRestrictions;
    private String allergies;
    private String preferredCuisine;
    private BigDecimal weeklyExerciseHours;
    private BigDecimal adherenceToDietPlan;
    private BigDecimal nutrientImbalanceScore;
    private String dietRecommendation;
    private String source;
    private LocalDateTime createdAt;

    public DietRecommendationDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExternalPatientId() { return externalPatientId; }
    public void setExternalPatientId(String externalPatientId) { this.externalPatientId = externalPatientId; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public BigDecimal getHeightCm() { return heightCm; }
    public void setHeightCm(BigDecimal heightCm) { this.heightCm = heightCm; }
    public BigDecimal getBmi() { return bmi; }
    public void setBmi(BigDecimal bmi) { this.bmi = bmi; }
    public String getDiseaseType() { return diseaseType; }
    public void setDiseaseType(String diseaseType) { this.diseaseType = diseaseType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getPhysicalActivityLevel() { return physicalActivityLevel; }
    public void setPhysicalActivityLevel(String physicalActivityLevel) { this.physicalActivityLevel = physicalActivityLevel; }
    public BigDecimal getDailyCaloricIntake() { return dailyCaloricIntake; }
    public void setDailyCaloricIntake(BigDecimal dailyCaloricIntake) { this.dailyCaloricIntake = dailyCaloricIntake; }
    public BigDecimal getCholesterolMgDl() { return cholesterolMgDl; }
    public void setCholesterolMgDl(BigDecimal cholesterolMgDl) { this.cholesterolMgDl = cholesterolMgDl; }
    public BigDecimal getBloodPressureMmhg() { return bloodPressureMmhg; }
    public void setBloodPressureMmhg(BigDecimal bloodPressureMmhg) { this.bloodPressureMmhg = bloodPressureMmhg; }
    public BigDecimal getGlucoseMgDl() { return glucoseMgDl; }
    public void setGlucoseMgDl(BigDecimal glucoseMgDl) { this.glucoseMgDl = glucoseMgDl; }
    public String getDietaryRestrictions() { return dietaryRestrictions; }
    public void setDietaryRestrictions(String dietaryRestrictions) { this.dietaryRestrictions = dietaryRestrictions; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getPreferredCuisine() { return preferredCuisine; }
    public void setPreferredCuisine(String preferredCuisine) { this.preferredCuisine = preferredCuisine; }
    public BigDecimal getWeeklyExerciseHours() { return weeklyExerciseHours; }
    public void setWeeklyExerciseHours(BigDecimal weeklyExerciseHours) { this.weeklyExerciseHours = weeklyExerciseHours; }
    public BigDecimal getAdherenceToDietPlan() { return adherenceToDietPlan; }
    public void setAdherenceToDietPlan(BigDecimal adherenceToDietPlan) { this.adherenceToDietPlan = adherenceToDietPlan; }
    public BigDecimal getNutrientImbalanceScore() { return nutrientImbalanceScore; }
    public void setNutrientImbalanceScore(BigDecimal nutrientImbalanceScore) { this.nutrientImbalanceScore = nutrientImbalanceScore; }
    public String getDietRecommendation() { return dietRecommendation; }
    public void setDietRecommendation(String dietRecommendation) { this.dietRecommendation = dietRecommendation; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // DTO for aggregate stats used by the dashboard
    public static class StatsDTO {
        private long totalCount;
        private double avgAdherenceRate;
        private double avgNutrientImbalanceScore;
        private List<Item> diseaseTypeDistribution;
        private List<Item> severityDistribution;
        private List<Item> activityLevelDistribution;
        private List<Item> dietRecommendationDistribution;

        public StatsDTO() {}

        public long getTotalCount() { return totalCount; }
        public void setTotalCount(long totalCount) { this.totalCount = totalCount; }
        public double getAvgAdherenceRate() { return avgAdherenceRate; }
        public void setAvgAdherenceRate(double avgAdherenceRate) { this.avgAdherenceRate = avgAdherenceRate; }
        public double getAvgNutrientImbalanceScore() { return avgNutrientImbalanceScore; }
        public void setAvgNutrientImbalanceScore(double avgNutrientImbalanceScore) { this.avgNutrientImbalanceScore = avgNutrientImbalanceScore; }
        public List<Item> getDiseaseTypeDistribution() { return diseaseTypeDistribution; }
        public void setDiseaseTypeDistribution(List<Item> diseaseTypeDistribution) { this.diseaseTypeDistribution = diseaseTypeDistribution; }
        public List<Item> getSeverityDistribution() { return severityDistribution; }
        public void setSeverityDistribution(List<Item> severityDistribution) { this.severityDistribution = severityDistribution; }
        public List<Item> getActivityLevelDistribution() { return activityLevelDistribution; }
        public void setActivityLevelDistribution(List<Item> activityLevelDistribution) { this.activityLevelDistribution = activityLevelDistribution; }
        public List<Item> getDietRecommendationDistribution() { return dietRecommendationDistribution; }
        public void setDietRecommendationDistribution(List<Item> dietRecommendationDistribution) { this.dietRecommendationDistribution = dietRecommendationDistribution; }

        public static class Item {
            private String label;
            private long value;
            public Item() {}
            public Item(String label, long value) { this.label = label; this.value = value; }
            public String getLabel() { return label; }
            public void setLabel(String label) { this.label = label; }
            public long getValue() { return value; }
            public void setValue(long value) { this.value = value; }
        }
    }
}
