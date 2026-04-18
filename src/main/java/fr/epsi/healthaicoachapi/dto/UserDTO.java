package fr.epsi.healthaicoachapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String role;
    private Boolean isPremium;
    private Integer age;
    private String gender;
    private BigDecimal weightKg;
    private BigDecimal heightCm;
    private String objective;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivity;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String username, String role, Boolean isPremium, Integer age, String gender,
                   BigDecimal weightKg, BigDecimal heightCm, String objective, LocalDateTime createdAt,
                   LocalDateTime lastActivity) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.isPremium = isPremium;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Boolean premium) {
        isPremium = premium;
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

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(BigDecimal heightCm) {
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
        private String email;
        private String username;
        private String role;
        private Boolean isPremium;
        private Integer age;
        private String gender;
        private BigDecimal weightKg;
        private BigDecimal heightCm;
        private String objective;
        private LocalDateTime createdAt;
        private LocalDateTime lastActivity;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder isPremium(Boolean isPremium) {
            this.isPremium = isPremium;
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

        public Builder weightKg(BigDecimal weightKg) {
            this.weightKg = weightKg;
            return this;
        }

        public Builder heightCm(BigDecimal heightCm) {
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

        public UserDTO build() {
            return new UserDTO(id, email, username, role, isPremium, age, gender, weightKg, heightCm, objective,
                    createdAt, lastActivity);
        }
    }
}
