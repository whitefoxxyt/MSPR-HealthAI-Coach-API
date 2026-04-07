package fr.epsi.healthaicoachapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ExerciseDTO {

    private Long id;
    private String externalId;
    private String name;
    private List<String> bodyParts;
    private List<String> targetMuscles;
    private List<String> secondaryMuscles;
    private List<String> equipments;
    private String instructions;
    private String gifUrl;
    private String source;
    private LocalDateTime createdAt;

    public ExerciseDTO() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<String> getBodyParts() { return bodyParts; }
    public void setBodyParts(List<String> bodyParts) { this.bodyParts = bodyParts; }
    public List<String> getTargetMuscles() { return targetMuscles; }
    public void setTargetMuscles(List<String> targetMuscles) { this.targetMuscles = targetMuscles; }
    public List<String> getSecondaryMuscles() { return secondaryMuscles; }
    public void setSecondaryMuscles(List<String> secondaryMuscles) { this.secondaryMuscles = secondaryMuscles; }
    public List<String> getEquipments() { return equipments; }
    public void setEquipments(List<String> equipments) { this.equipments = equipments; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getGifUrl() { return gifUrl; }
    public void setGifUrl(String gifUrl) { this.gifUrl = gifUrl; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class Builder {
        private ExerciseDTO dto = new ExerciseDTO();

        public Builder id(Long id) { dto.id = id; return this; }
        public Builder externalId(String externalId) { dto.externalId = externalId; return this; }
        public Builder name(String name) { dto.name = name; return this; }
        public Builder bodyParts(List<String> bodyParts) { dto.bodyParts = bodyParts; return this; }
        public Builder targetMuscles(List<String> targetMuscles) { dto.targetMuscles = targetMuscles; return this; }
        public Builder secondaryMuscles(List<String> secondaryMuscles) { dto.secondaryMuscles = secondaryMuscles; return this; }
        public Builder equipments(List<String> equipments) { dto.equipments = equipments; return this; }
        public Builder instructions(String instructions) { dto.instructions = instructions; return this; }
        public Builder gifUrl(String gifUrl) { dto.gifUrl = gifUrl; return this; }
        public Builder source(String source) { dto.source = source; return this; }
        public Builder createdAt(LocalDateTime createdAt) { dto.createdAt = createdAt; return this; }
        public ExerciseDTO build() { return dto; }
    }
}
