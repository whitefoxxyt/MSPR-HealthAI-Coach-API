package fr.epsi.healthaicoachapi.dto;

public class RegisterRequest {
    private String email;
    private String username;
    private String password;
    private Integer age;
    private String gender;
    private Double weightKg;
    private Double heightCm;
    private String objective;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String username, String password, Integer age, String gender,
                           Double weightKg, Double heightCm, String objective) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.objective = objective;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
