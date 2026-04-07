package fr.epsi.healthaicoachapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NutritionEntryDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Food name is required")
    private String foodName;

    private String category;

    private String mealType;

    @Positive(message = "Calories must be positive")
    private BigDecimal calories;

    private BigDecimal cholesterolMg;

    @Positive(message = "Protein must be positive")
    private BigDecimal proteinG;

    @Positive(message = "Carbs must be positive")
    private BigDecimal carbsG;

    @Positive(message = "Fat must be positive")
    private BigDecimal fatG;

    private BigDecimal fiberG;
    private BigDecimal sugarsG;
    private BigDecimal sodiumMg;
    private BigDecimal waterMl;

    @NotBlank(message = "Source is required")
    private String source;

    private String status;

    private LocalDateTime createdAt;

    public NutritionEntryDTO() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
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

    public static class Builder {
        private NutritionEntryDTO dto = new NutritionEntryDTO();

        public Builder id(Long id) { dto.id = id; return this; }
        public Builder userId(Long userId) { dto.userId = userId; return this; }
        public Builder foodName(String foodName) { dto.foodName = foodName; return this; }
        public Builder category(String category) { dto.category = category; return this; }
        public Builder mealType(String mealType) { dto.mealType = mealType; return this; }
        public Builder calories(BigDecimal calories) { dto.calories = calories; return this; }
        public Builder cholesterolMg(BigDecimal cholesterolMg) { dto.cholesterolMg = cholesterolMg; return this; }
        public Builder proteinG(BigDecimal proteinG) { dto.proteinG = proteinG; return this; }
        public Builder carbsG(BigDecimal carbsG) { dto.carbsG = carbsG; return this; }
        public Builder fatG(BigDecimal fatG) { dto.fatG = fatG; return this; }
        public Builder fiberG(BigDecimal fiberG) { dto.fiberG = fiberG; return this; }
        public Builder sugarsG(BigDecimal sugarsG) { dto.sugarsG = sugarsG; return this; }
        public Builder sodiumMg(BigDecimal sodiumMg) { dto.sodiumMg = sodiumMg; return this; }
        public Builder waterMl(BigDecimal waterMl) { dto.waterMl = waterMl; return this; }
        public Builder source(String source) { dto.source = source; return this; }
        public Builder status(String status) { dto.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { dto.createdAt = createdAt; return this; }
        public NutritionEntryDTO build() { return dto; }
    }
}
