package fr.epsi.healthaicoachapi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT[]")
    private List<String> bodyParts;

    @Column(columnDefinition = "TEXT[]")
    private List<String> targetMuscles;

    @Column(columnDefinition = "TEXT[]")
    private List<String> secondaryMuscles;

    @Column(columnDefinition = "TEXT[]")
    private List<String> equipments;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    private String gifUrl;

    @Column(nullable = false)
    private String source = "EXERCISEDB";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Exercise() {
    }

    public Exercise(Long id, String externalId, String name, List<String> bodyParts, List<String> targetMuscles,
                    List<String> secondaryMuscles, List<String> equipments, String instructions, String gifUrl,
                    String source, LocalDateTime createdAt) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
        this.bodyParts = bodyParts;
        this.targetMuscles = targetMuscles;
        this.secondaryMuscles = secondaryMuscles;
        this.equipments = equipments;
        this.instructions = instructions;
        this.gifUrl = gifUrl;
        this.source = source;
        this.createdAt = createdAt;
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
}
