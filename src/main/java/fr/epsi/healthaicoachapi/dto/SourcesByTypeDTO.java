package fr.epsi.healthaicoachapi.dto;

import java.util.List;

public class SourcesByTypeDTO {

    private List<String> biometric;
    private List<String> nutrition;
    private List<String> exercise;

    public SourcesByTypeDTO() {}

    public SourcesByTypeDTO(List<String> biometric, List<String> nutrition, List<String> exercise) {
        this.biometric = biometric;
        this.nutrition = nutrition;
        this.exercise = exercise;
    }

    public List<String> getBiometric() { return biometric; }
    public void setBiometric(List<String> biometric) { this.biometric = biometric; }
    public List<String> getNutrition() { return nutrition; }
    public void setNutrition(List<String> nutrition) { this.nutrition = nutrition; }
    public List<String> getExercise() { return exercise; }
    public void setExercise(List<String> exercise) { this.exercise = exercise; }
}
