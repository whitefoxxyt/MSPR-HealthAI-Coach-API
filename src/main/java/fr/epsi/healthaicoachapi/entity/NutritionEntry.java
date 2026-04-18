package fr.epsi.healthaicoachapi.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "nutrition_entries")
public class NutritionEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    private String category;

    @Column(name = "meal_type")
    private String mealType;

    private BigDecimal calories;

    @Column(name = "cholesterol_mg")
    private BigDecimal cholesterolMg;

    @Column(name = "protein_g")
    private BigDecimal proteinG;

    @Column(name = "carbs_g")
    private BigDecimal carbsG;

    @Column(name = "fat_g")
    private BigDecimal fatG;

    @Column(name = "fiber_g")
    private BigDecimal fiberG;

    @Column(name = "sugars_g")
    private BigDecimal sugarsG;

    @Column(name = "sodium_mg")
    private BigDecimal sodiumMg;

    @Column(name = "water_ml")
    private BigDecimal waterMl;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String status = "BRUT";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public NutritionEntry() {
    }

    public NutritionEntry(Long id, String foodName, String category, String mealType, BigDecimal calories,
                          BigDecimal cholesterolMg, BigDecimal proteinG, BigDecimal carbsG, BigDecimal fatG,
                          BigDecimal fiberG, BigDecimal sugarsG, BigDecimal sodiumMg, BigDecimal waterMl,
                          String source, String status, LocalDateTime createdAt) {
        this.id = id;
        this.foodName = foodName;
        this.category = category;
        this.mealType = mealType;
        this.calories = calories;
        this.cholesterolMg = cholesterolMg;
        this.proteinG = proteinG;
        this.carbsG = carbsG;
        this.fatG = fatG;
        this.fiberG = fiberG;
        this.sugarsG = sugarsG;
        this.sodiumMg = sodiumMg;
        this.waterMl = waterMl;
        this.source = source;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public BigDecimal getCalories() { return calories; }
    public void setCalories(BigDecimal calories) { this.calories = calories; }
    public BigDecimal getCholesterolMg() { return cholesterolMg; }
    public void setCholesterolMg(BigDecimal cholesterolMg) { this.cholesterolMg = cholesterolMg; }
    public BigDecimal getProteinG() { return proteinG; }
    public void setProteinG(BigDecimal proteinG) { this.proteinG = proteinG; }
    public BigDecimal getCarbsG() { return carbsG; }
    public void setCarbsG(BigDecimal carbsG) { this.carbsG = carbsG; }
    public BigDecimal getFatG() { return fatG; }
    public void setFatG(BigDecimal fatG) { this.fatG = fatG; }
    public BigDecimal getFiberG() { return fiberG; }
    public void setFiberG(BigDecimal fiberG) { this.fiberG = fiberG; }
    public BigDecimal getSugarsG() { return sugarsG; }
    public void setSugarsG(BigDecimal sugarsG) { this.sugarsG = sugarsG; }
    public BigDecimal getSodiumMg() { return sodiumMg; }
    public void setSodiumMg(BigDecimal sodiumMg) { this.sodiumMg = sodiumMg; }
    public BigDecimal getWaterMl() { return waterMl; }
    public void setWaterMl(BigDecimal waterMl) { this.waterMl = waterMl; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
