package fr.epsi.healthaicoachapi.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_entries")
public class ExerciseEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "workout_type")
    private String workoutType;

    @Column(name = "duration_min")
    private BigDecimal durationMin;

    @Column(name = "calories_burned")
    private BigDecimal caloriesBurned;

    private Integer steps;

    @Column(name = "heart_rate_avg")
    private Integer heartRateAvg;

    @Column(name = "heart_rate_max")
    private Integer heartRateMax;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String status = "BRUT";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ExerciseEntry() {
    }

    public ExerciseEntry(Long id, User user, String workoutType, BigDecimal durationMin, BigDecimal caloriesBurned,
                         Integer steps, Integer heartRateAvg, Integer heartRateMax, String source, String status,
                         LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.workoutType = workoutType;
        this.durationMin = durationMin;
        this.caloriesBurned = caloriesBurned;
        this.steps = steps;
        this.heartRateAvg = heartRateAvg;
        this.heartRateMax = heartRateMax;
        this.source = source;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
}
