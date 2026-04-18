package fr.epsi.healthaicoachapi.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "diet_recommendations")
public class DietRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_patient_id", unique = true)
    private String externalPatientId;

    private Integer age;
    private String gender;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    @Column(name = "height_cm")
    private BigDecimal heightCm;

    private BigDecimal bmi;

    @Column(name = "disease_type")
    private String diseaseType;

    private String severity;

    @Column(name = "physical_activity_level")
    private String physicalActivityLevel;

    @Column(name = "daily_caloric_intake")
    private BigDecimal dailyCaloricIntake;

    @Column(name = "cholesterol_mg_dl")
    private BigDecimal cholesterolMgDl;

    @Column(name = "blood_pressure_mmhg")
    private BigDecimal bloodPressureMmhg;

    @Column(name = "glucose_mg_dl")
    private BigDecimal glucoseMgDl;

    @Column(name = "dietary_restrictions")
    private String dietaryRestrictions;

    private String allergies;

    @Column(name = "preferred_cuisine")
    private String preferredCuisine;

    @Column(name = "weekly_exercise_hours")
    private BigDecimal weeklyExerciseHours;

    @Column(name = "adherence_to_diet_plan")
    private BigDecimal adherenceToDietPlan;

    @Column(name = "nutrient_imbalance_score")
    private BigDecimal nutrientImbalanceScore;

    @Column(name = "diet_recommendation")
    private String dietRecommendation;

    private String source;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public DietRecommendation() {}

    public Long getId() { return id; }
    public String getExternalPatientId() { return externalPatientId; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public BigDecimal getWeightKg() { return weightKg; }
    public BigDecimal getHeightCm() { return heightCm; }
    public BigDecimal getBmi() { return bmi; }
    public String getDiseaseType() { return diseaseType; }
    public String getSeverity() { return severity; }
    public String getPhysicalActivityLevel() { return physicalActivityLevel; }
    public BigDecimal getDailyCaloricIntake() { return dailyCaloricIntake; }
    public BigDecimal getCholesterolMgDl() { return cholesterolMgDl; }
    public BigDecimal getBloodPressureMmhg() { return bloodPressureMmhg; }
    public BigDecimal getGlucoseMgDl() { return glucoseMgDl; }
    public String getDietaryRestrictions() { return dietaryRestrictions; }
    public String getAllergies() { return allergies; }
    public String getPreferredCuisine() { return preferredCuisine; }
    public BigDecimal getWeeklyExerciseHours() { return weeklyExerciseHours; }
    public BigDecimal getAdherenceToDietPlan() { return adherenceToDietPlan; }
    public BigDecimal getNutrientImbalanceScore() { return nutrientImbalanceScore; }
    public String getDietRecommendation() { return dietRecommendation; }
    public String getSource() { return source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
