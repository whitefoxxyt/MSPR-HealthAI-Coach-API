package fr.epsi.healthaicoachapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BiometricEntryDTO {

    private Long id;

    @Positive(message = "Weight must be positive")
    private BigDecimal weightKg;

    @Positive(message = "Height must be positive")
    private BigDecimal heightCm;

    private BigDecimal bmi;

    private BigDecimal fatPercentage;

    private Integer heartRateRest;

    private Integer heartRateAvg;

    private Integer heartRateMax;

    private String bloodPressure;

    @NotBlank(message = "Source is required")
    private String source;

    private String status;

    private LocalDateTime createdAt;

    public BiometricEntryDTO() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public BigDecimal getHeightCm() { return heightCm; }
    public void setHeightCm(BigDecimal heightCm) { this.heightCm = heightCm; }
    public BigDecimal getBmi() { return bmi; }
    public void setBmi(BigDecimal bmi) { this.bmi = bmi; }
    public BigDecimal getFatPercentage() { return fatPercentage; }
    public void setFatPercentage(BigDecimal fatPercentage) { this.fatPercentage = fatPercentage; }
    public Integer getHeartRateRest() { return heartRateRest; }
    public void setHeartRateRest(Integer heartRateRest) { this.heartRateRest = heartRateRest; }
    public Integer getHeartRateAvg() { return heartRateAvg; }
    public void setHeartRateAvg(Integer heartRateAvg) { this.heartRateAvg = heartRateAvg; }
    public Integer getHeartRateMax() { return heartRateMax; }
    public void setHeartRateMax(Integer heartRateMax) { this.heartRateMax = heartRateMax; }
    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class Builder {
        private BiometricEntryDTO dto = new BiometricEntryDTO();

        public Builder id(Long id) { dto.id = id; return this; }
        public Builder weightKg(BigDecimal weightKg) { dto.weightKg = weightKg; return this; }
        public Builder heightCm(BigDecimal heightCm) { dto.heightCm = heightCm; return this; }
        public Builder bmi(BigDecimal bmi) { dto.bmi = bmi; return this; }
        public Builder fatPercentage(BigDecimal fatPercentage) { dto.fatPercentage = fatPercentage; return this; }
        public Builder heartRateRest(Integer heartRateRest) { dto.heartRateRest = heartRateRest; return this; }
        public Builder heartRateAvg(Integer heartRateAvg) { dto.heartRateAvg = heartRateAvg; return this; }
        public Builder heartRateMax(Integer heartRateMax) { dto.heartRateMax = heartRateMax; return this; }
        public Builder bloodPressure(String bloodPressure) { dto.bloodPressure = bloodPressure; return this; }
        public Builder source(String source) { dto.source = source; return this; }
        public Builder status(String status) { dto.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { dto.createdAt = createdAt; return this; }
        public BiometricEntryDTO build() { return dto; }
    }
}
