package fr.epsi.healthaicoachapi.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseTest {

    @Test
    @DisplayName("Exercise - Should set and get all fields via setters")
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        Exercise exercise = new Exercise();

        exercise.setId(1L);
        exercise.setExternalId("ext-001");
        exercise.setName("Bench Press");
        exercise.setBodyParts(List.of("chest", "shoulders"));
        exercise.setTargetMuscles(List.of("pectoralis major"));
        exercise.setSecondaryMuscles(List.of("triceps"));
        exercise.setEquipments(List.of("barbell"));
        exercise.setInstructions("Lie flat and press upward.");
        exercise.setGifUrl("https://example.com/bench.gif");
        exercise.setSource("EXERCISEDB");
        exercise.setCreatedAt(now);

        assertEquals(1L, exercise.getId());
        assertEquals("ext-001", exercise.getExternalId());
        assertEquals("Bench Press", exercise.getName());
        assertEquals(List.of("chest", "shoulders"), exercise.getBodyParts());
        assertEquals(List.of("pectoralis major"), exercise.getTargetMuscles());
        assertEquals(List.of("triceps"), exercise.getSecondaryMuscles());
        assertEquals(List.of("barbell"), exercise.getEquipments());
        assertEquals("Lie flat and press upward.", exercise.getInstructions());
        assertEquals("https://example.com/bench.gif", exercise.getGifUrl());
        assertEquals("EXERCISEDB", exercise.getSource());
        assertEquals(now, exercise.getCreatedAt());
    }

    @Test
    @DisplayName("Exercise - Should create with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        List<String> bodyParts = List.of("legs");
        List<String> muscles = List.of("quadriceps");
        List<String> secondary = List.of("hamstrings");
        List<String> equipments = List.of("barbell");

        Exercise exercise = new Exercise(1L, "ext-002", "Squat",
                bodyParts, muscles, secondary, equipments,
                "Stand with feet shoulder-width apart.", "https://example.com/squat.gif",
                "EXERCISEDB", now);

        assertEquals(1L, exercise.getId());
        assertEquals("ext-002", exercise.getExternalId());
        assertEquals("Squat", exercise.getName());
        assertEquals(bodyParts, exercise.getBodyParts());
        assertEquals("EXERCISEDB", exercise.getSource());
        assertEquals(now, exercise.getCreatedAt());
    }

    @Test
    @DisplayName("Exercise - Default constructor should set default source and createdAt")
    void testDefaultConstructor_Defaults() {
        Exercise exercise = new Exercise();

        assertEquals("EXERCISEDB", exercise.getSource());
        assertNotNull(exercise.getCreatedAt());
        assertNull(exercise.getId());
        assertNull(exercise.getName());
    }

    @Test
    @DisplayName("Exercise - Should handle empty lists for muscles and equipment")
    void testEmptyLists() {
        Exercise exercise = new Exercise();
        exercise.setBodyParts(List.of());
        exercise.setTargetMuscles(List.of());
        exercise.setSecondaryMuscles(List.of());
        exercise.setEquipments(List.of());

        assertTrue(exercise.getBodyParts().isEmpty());
        assertTrue(exercise.getTargetMuscles().isEmpty());
        assertTrue(exercise.getSecondaryMuscles().isEmpty());
        assertTrue(exercise.getEquipments().isEmpty());
    }
}
