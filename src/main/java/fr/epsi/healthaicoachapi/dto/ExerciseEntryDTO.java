package fr.epsi.healthaicoachapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExerciseEntryDTO {

    private Long id;

    private String workoutType;

    @Positive(message = "Duration must be positive")
    private BigDecimal durationMin;

    @Positive(message = "Calories burned must be positive")
    private BigDecimal caloriesBurned;

    private Integer steps;

    private Integer heartRateAvg;

    private Integer heartRateMax;

    @NotBlank(message = "Source is required")
    private String source;

    private String status;

    private LocalDateTime createdAt;

    public ExerciseEntryDTO() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWorkoutType() { return workoutType; }
    public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }
    public BigDecimal getDurationMin() { return durationMin; }
    public void setDurationMin(BigDecimal durationMin) { this.durationMin = durationMin; }
    public BigDecimal getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(BigDecimal caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    public Integer getSteps() { return steps; }
    public void setSteps(Integer steps) { this.steps = steps; }
    public Integer getHeartRateAvg() { return heartRateAvg; }
    public void setHeartRateAvg(Integer heartRateAvg) { this.heartRateAvg = heartRateAvg; }
    public Integer getHeartRateMax() { return heartRateMax; }
    public void setHeartRateMax(Integer heartRateMax) { this.heartRateMax = heartRateMax; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class Builder {
        private ExerciseEntryDTO dto = new ExerciseEntryDTO();

        public Builder id(Long id) { dto.id = id; return this; }
        public Builder workoutType(String workoutType) { dto.workoutType = workoutType; return this; }
        public Builder durationMin(BigDecimal durationMin) { dto.durationMin = durationMin; return this; }
        public Builder caloriesBurned(BigDecimal caloriesBurned) { dto.caloriesBurned = caloriesBurned; return this; }
        public Builder steps(Integer steps) { dto.steps = steps; return this; }
        public Builder heartRateAvg(Integer heartRateAvg) { dto.heartRateAvg = heartRateAvg; return this; }
        public Builder heartRateMax(Integer heartRateMax) { dto.heartRateMax = heartRateMax; return this; }
        public Builder source(String source) { dto.source = source; return this; }
        public Builder status(String status) { dto.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { dto.createdAt = createdAt; return this; }
        public ExerciseEntryDTO build() { return dto; }
    }
}
