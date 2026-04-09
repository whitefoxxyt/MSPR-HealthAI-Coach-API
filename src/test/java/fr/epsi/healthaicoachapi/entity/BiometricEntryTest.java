package fr.epsi.healthaicoachapi.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BiometricEntryTest {

    private User buildUser() {
        return User.builder()
                .id(1L)
                .authUserId("auth-123")
                .username("testuser")
                .build();
    }

    @Test
    @DisplayName("BiometricEntry - Should set and get all fields via setters")
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        User user = buildUser();
        BiometricEntry entry = new BiometricEntry();

        entry.setId(1L);
        entry.setUser(user);
        entry.setWeightKg(new BigDecimal("75.5"));
        entry.setHeightCm(new BigDecimal("180.0"));
        entry.setBmi(new BigDecimal("23.3"));
        entry.setFatPercentage(new BigDecimal("18.5"));
        entry.setHeartRateRest(60);
        entry.setHeartRateAvg(75);
        entry.setHeartRateMax(180);
        entry.setBloodPressure("120/80");
        entry.setSource("manual");
        entry.setStatus("BRUT");
        entry.setCreatedAt(now);

        assertEquals(1L, entry.getId());
        assertEquals(user, entry.getUser());
        assertEquals(new BigDecimal("75.5"), entry.getWeightKg());
        assertEquals(new BigDecimal("180.0"), entry.getHeightCm());
        assertEquals(new BigDecimal("23.3"), entry.getBmi());
        assertEquals(new BigDecimal("18.5"), entry.getFatPercentage());
        assertEquals(60, entry.getHeartRateRest());
        assertEquals(75, entry.getHeartRateAvg());
        assertEquals(180, entry.getHeartRateMax());
        assertEquals("120/80", entry.getBloodPressure());
        assertEquals("manual", entry.getSource());
        assertEquals("BRUT", entry.getStatus());
        assertEquals(now, entry.getCreatedAt());
    }

    @Test
    @DisplayName("BiometricEntry - Should create with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = buildUser();

        BiometricEntry entry = new BiometricEntry(1L, user,
                new BigDecimal("75.5"), new BigDecimal("180.0"), new BigDecimal("23.3"),
                new BigDecimal("18.5"), 60, 75, 180, "120/80",
                "garmin", "BRUT", now);

        assertEquals(1L, entry.getId());
        assertEquals(user, entry.getUser());
        assertEquals(new BigDecimal("75.5"), entry.getWeightKg());
        assertEquals("garmin", entry.getSource());
        assertEquals("BRUT", entry.getStatus());
        assertEquals(now, entry.getCreatedAt());
    }

    @Test
    @DisplayName("BiometricEntry - Default constructor should set default status and createdAt")
    void testDefaultConstructor_Defaults() {
        BiometricEntry entry = new BiometricEntry();

        assertEquals("BRUT", entry.getStatus());
        assertNotNull(entry.getCreatedAt());
        assertNull(entry.getId());
        assertNull(entry.getUser());
    }

    @Test
    @DisplayName("BiometricEntry - Should allow updating status")
    void testSetStatus() {
        BiometricEntry entry = new BiometricEntry();
        entry.setStatus("VALIDATED");

        assertEquals("VALIDATED", entry.getStatus());
    }
}
