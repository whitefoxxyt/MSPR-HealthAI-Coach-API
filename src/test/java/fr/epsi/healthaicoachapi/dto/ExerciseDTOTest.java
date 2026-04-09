package fr.epsi.healthaicoachapi.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseDTOTest {

    @Test
    @DisplayName("ExerciseDTO - Should build with all fields using builder")
    void testBuilder_AllFields() {
        LocalDateTime now = LocalDateTime.now();
        List<String> bodyParts = List.of("chest", "shoulders");
        List<String> muscles = List.of("pectoralis major");
        List<String> secondary = List.of("triceps");
        List<String> equipments = List.of("barbell");

        ExerciseDTO dto = ExerciseDTO.builder()
                .id(1L)
                .externalId("ext-001")
                .name("Bench Press")
                .bodyParts(bodyParts)
                .targetMuscles(muscles)
                .secondaryMuscles(secondary)
                .equipments(equipments)
                .instructions("Lie on bench and press the bar.")
                .gifUrl("https://example.com/bench.gif")
                .source("external")
                .createdAt(now)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("ext-001", dto.getExternalId());
        assertEquals("Bench Press", dto.getName());
        assertEquals(bodyParts, dto.getBodyParts());
        assertEquals(muscles, dto.getTargetMuscles());
        assertEquals(secondary, dto.getSecondaryMuscles());
        assertEquals(equipments, dto.getEquipments());
        assertEquals("Lie on bench and press the bar.", dto.getInstructions());
        assertEquals("https://example.com/bench.gif", dto.getGifUrl());
        assertEquals("external", dto.getSource());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    @DisplayName("ExerciseDTO - Should set and get all fields via setters")
    void testSettersAndGetters() {
        ExerciseDTO dto = new ExerciseDTO();

        dto.setId(2L);
        dto.setName("Squat");
        dto.setExternalId("ext-002");
        dto.setBodyParts(List.of("legs"));
        dto.setTargetMuscles(List.of("quadriceps"));
        dto.setSecondaryMuscles(List.of("hamstrings"));
        dto.setEquipments(List.of("barbell"));
        dto.setInstructions("Stand with feet shoulder-width apart.");
        dto.setGifUrl("https://example.com/squat.gif");
        dto.setSource("manual");

        assertEquals(2L, dto.getId());
        assertEquals("Squat", dto.getName());
        assertEquals("ext-002", dto.getExternalId());
        assertEquals(List.of("legs"), dto.getBodyParts());
        assertEquals(List.of("quadriceps"), dto.getTargetMuscles());
    }

    @Test
    @DisplayName("ExerciseDTO - Should have null fields with default constructor")
    void testDefaultConstructor_NullFields() {
        ExerciseDTO dto = new ExerciseDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getBodyParts());
        assertNull(dto.getCreatedAt());
    }
}
