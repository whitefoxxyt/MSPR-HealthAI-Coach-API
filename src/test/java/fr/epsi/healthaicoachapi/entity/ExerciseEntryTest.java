package fr.epsi.healthaicoachapi.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseEntryTest {

    private User buildUser() {
        return User.builder()
                .id(1L)
                .authUserId("auth-123")
                .username("testuser")
                .build();
    }

    @Test
    @DisplayName("ExerciseEntry - Should set and get all fields via setters")
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        User user = buildUser();
        ExerciseEntry entry = new ExerciseEntry();

        entry.setId(1L);
        entry.setUser(user);
        entry.setWorkoutType("Running");
        entry.setDurationMin(new BigDecimal("45.0"));
        entry.setCaloriesBurned(new BigDecimal("350.0"));
        entry.setSteps(6000);
        entry.setHeartRateAvg(140);
        entry.setHeartRateMax(175);
        entry.setSource("garmin");
        entry.setStatus("BRUT");
        entry.setCreatedAt(now);

        assertEquals(1L, entry.getId());
        assertEquals(user, entry.getUser());
        assertEquals("Running", entry.getWorkoutType());
        assertEquals(new BigDecimal("45.0"), entry.getDurationMin());
        assertEquals(new BigDecimal("350.0"), entry.getCaloriesBurned());
        assertEquals(6000, entry.getSteps());
        assertEquals(140, entry.getHeartRateAvg());
        assertEquals(175, entry.getHeartRateMax());
        assertEquals("garmin", entry.getSource());
        assertEquals("BRUT", entry.getStatus());
        assertEquals(now, entry.getCreatedAt());
    }

    @Test
    @DisplayName("ExerciseEntry - Should create with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = buildUser();

        ExerciseEntry entry = new ExerciseEntry(1L, user, "Cycling",
                new BigDecimal("60.0"), new BigDecimal("500.0"),
                0, 130, 160, "manual", "BRUT", now);

        assertEquals(1L, entry.getId());
        assertEquals(user, entry.getUser());
        assertEquals("Cycling", entry.getWorkoutType());
        assertEquals(new BigDecimal("60.0"), entry.getDurationMin());
        assertEquals("manual", entry.getSource());
        assertEquals("BRUT", entry.getStatus());
        assertEquals(now, entry.getCreatedAt());
    }

    @Test
    @DisplayName("ExerciseEntry - Default constructor should set default status and createdAt")
    void testDefaultConstructor_Defaults() {
        ExerciseEntry entry = new ExerciseEntry();

        assertEquals("BRUT", entry.getStatus());
        assertNotNull(entry.getCreatedAt());
        assertNull(entry.getId());
        assertNull(entry.getUser());
    }

    @Test
    @DisplayName("ExerciseEntry - Should allow updating status")
    void testSetStatus() {
        ExerciseEntry entry = new ExerciseEntry();
        entry.setStatus("VALIDATED");

        assertEquals("VALIDATED", entry.getStatus());
    }
}
