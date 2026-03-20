package fr.epsi.healthaicoachapi.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "biometric_entries")
public class BiometricEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal weightKg;
    private BigDecimal heightCm;
    private BigDecimal bmi;
    private BigDecimal fatPercentage;
    private Integer heartRateRest;
    private Integer heartRateAvg;
    private Integer heartRateMax;
    private String bloodPressure;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String status = "BRUT";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public BiometricEntry() {
    }

    public BiometricEntry(Long id, User user, BigDecimal weightKg, BigDecimal heightCm, BigDecimal bmi,
                          BigDecimal fatPercentage, Integer heartRateRest, Integer heartRateAvg,
                          Integer heartRateMax, String bloodPressure, String source, String status,
                          LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.bmi = bmi;
        this.fatPercentage = fatPercentage;
        this.heartRateRest = heartRateRest;
        this.heartRateAvg = heartRateAvg;
        this.heartRateMax = heartRateMax;
        this.bloodPressure = bloodPressure;
        this.source = source;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
}
