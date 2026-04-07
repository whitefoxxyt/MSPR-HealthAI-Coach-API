package fr.epsi.healthaicoachapi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String authUserId;

    @Column(nullable = false)
    private String username;

    private Integer age;
    private String gender;
    private Double weightKg;
    private Double heightCm;
    private String objective;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastActivity;

    public User() {
    }

    public User(Long id, String authUserId, String username, Integer age, String gender,
                Double weightKg, Double heightCm, String objective,
                LocalDateTime createdAt, LocalDateTime lastActivity) {
        this.id = id;
        this.authUserId = authUserId;
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.objective = objective;
        this.createdAt = createdAt;
        this.lastActivity = lastActivity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public static class Builder {
        private Long id;
        private String authUserId;
        private String username;
        private Integer age;
        private String gender;
        private Double weightKg;
        private Double heightCm;
        private String objective;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime lastActivity;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder authUserId(String authUserId) {
            this.authUserId = authUserId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder weightKg(Double weightKg) {
            this.weightKg = weightKg;
            return this;
        }

        public Builder heightCm(Double heightCm) {
            this.heightCm = heightCm;
            return this;
        }

        public Builder objective(String objective) {
            this.objective = objective;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastActivity(LocalDateTime lastActivity) {
            this.lastActivity = lastActivity;
            return this;
        }

        public User build() {
            return new User(id, authUserId, username, age, gender, weightKg, heightCm, objective, createdAt,
                    lastActivity);
        }
    }
}
