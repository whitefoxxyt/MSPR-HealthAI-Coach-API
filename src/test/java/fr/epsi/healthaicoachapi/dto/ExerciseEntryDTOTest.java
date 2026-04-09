package fr.epsi.healthaicoachapi.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseEntryDTOTest {

    @Test
    @DisplayName("ExerciseEntryDTO - Should build with all fields using builder")
    void testBuilder_AllFields() {
        LocalDateTime now = LocalDateTime.now();

        ExerciseEntryDTO dto = ExerciseEntryDTO.builder()
                .id(1L)
                .userId(10L)
                .workoutType("Running")
                .durationMin(new BigDecimal("45.0"))
                .caloriesBurned(new BigDecimal("350.0"))
                .steps(6000)
                .heartRateAvg(140)
                .heartRateMax(175)
                .source("garmin")
                .status("ACTIVE")
                .createdAt(now)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals("Running", dto.getWorkoutType());
        assertEquals(new BigDecimal("45.0"), dto.getDurationMin());
        assertEquals(new BigDecimal("350.0"), dto.getCaloriesBurned());
        assertEquals(6000, dto.getSteps());
        assertEquals(140, dto.getHeartRateAvg());
        assertEquals(175, dto.getHeartRateMax());
        assertEquals("garmin", dto.getSource());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    @DisplayName("ExerciseEntryDTO - Should set and get all fields via setters")
    void testSettersAndGetters() {
        ExerciseEntryDTO dto = new ExerciseEntryDTO();

        dto.setId(2L);
        dto.setUserId(20L);
        dto.setWorkoutType("Cycling");
        dto.setDurationMin(new BigDecimal("60.0"));
        dto.setCaloriesBurned(new BigDecimal("500.0"));
        dto.setSteps(0);
        dto.setHeartRateAvg(130);
        dto.setHeartRateMax(160);
        dto.setSource("manual");
        dto.setStatus("PENDING");

        assertEquals(2L, dto.getId());
        assertEquals(20L, dto.getUserId());
        assertEquals("Cycling", dto.getWorkoutType());
        assertEquals(new BigDecimal("60.0"), dto.getDurationMin());
        assertEquals(new BigDecimal("500.0"), dto.getCaloriesBurned());
        assertEquals(0, dto.getSteps());
        assertEquals(130, dto.getHeartRateAvg());
        assertEquals(160, dto.getHeartRateMax());
        assertEquals("manual", dto.getSource());
        assertEquals("PENDING", dto.getStatus());
    }

    @Test
    @DisplayName("ExerciseEntryDTO - Should have null fields with default constructor")
    void testDefaultConstructor_NullFields() {
        ExerciseEntryDTO dto = new ExerciseEntryDTO();

        assertNull(dto.getId());
        assertNull(dto.getUserId());
        assertNull(dto.getWorkoutType());
        assertNull(dto.getSource());
    }
}
