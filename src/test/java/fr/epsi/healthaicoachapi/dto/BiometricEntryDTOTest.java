package fr.epsi.healthaicoachapi.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BiometricEntryDTOTest {

    @Test
    @DisplayName("BiometricEntryDTO - Should build with all fields using builder")
    void testBuilder_AllFields() {
        LocalDateTime now = LocalDateTime.now();

        BiometricEntryDTO dto = BiometricEntryDTO.builder()
                .id(1L)
                .userId(10L)
                .weightKg(new BigDecimal("75.5"))
                .heightCm(new BigDecimal("180.0"))
                .bmi(new BigDecimal("23.3"))
                .fatPercentage(new BigDecimal("18.5"))
                .heartRateRest(60)
                .heartRateAvg(75)
                .heartRateMax(180)
                .bloodPressure("120/80")
                .source("manual")
                .status("ACTIVE")
                .createdAt(now)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(new BigDecimal("75.5"), dto.getWeightKg());
        assertEquals(new BigDecimal("180.0"), dto.getHeightCm());
        assertEquals(new BigDecimal("23.3"), dto.getBmi());
        assertEquals(new BigDecimal("18.5"), dto.getFatPercentage());
        assertEquals(60, dto.getHeartRateRest());
        assertEquals(75, dto.getHeartRateAvg());
        assertEquals(180, dto.getHeartRateMax());
        assertEquals("120/80", dto.getBloodPressure());
        assertEquals("manual", dto.getSource());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    @DisplayName("BiometricEntryDTO - Should set and get all fields via setters")
    void testSettersAndGetters() {
        BiometricEntryDTO dto = new BiometricEntryDTO();

        dto.setId(2L);
        dto.setUserId(20L);
        dto.setWeightKg(new BigDecimal("80.0"));
        dto.setHeightCm(new BigDecimal("175.0"));
        dto.setBmi(new BigDecimal("26.1"));
        dto.setFatPercentage(new BigDecimal("22.0"));
        dto.setHeartRateRest(65);
        dto.setHeartRateAvg(80);
        dto.setHeartRateMax(170);
        dto.setBloodPressure("130/85");
        dto.setSource("fitbit");
        dto.setStatus("PENDING");

        assertEquals(2L, dto.getId());
        assertEquals(20L, dto.getUserId());
        assertEquals(new BigDecimal("80.0"), dto.getWeightKg());
        assertEquals(new BigDecimal("175.0"), dto.getHeightCm());
        assertEquals(new BigDecimal("26.1"), dto.getBmi());
        assertEquals(new BigDecimal("22.0"), dto.getFatPercentage());
        assertEquals(65, dto.getHeartRateRest());
        assertEquals(80, dto.getHeartRateAvg());
        assertEquals(170, dto.getHeartRateMax());
        assertEquals("130/85", dto.getBloodPressure());
        assertEquals("fitbit", dto.getSource());
        assertEquals("PENDING", dto.getStatus());
    }

    @Test
    @DisplayName("BiometricEntryDTO - Should have null fields with default constructor")
    void testDefaultConstructor_NullFields() {
        BiometricEntryDTO dto = new BiometricEntryDTO();

        assertNull(dto.getId());
        assertNull(dto.getUserId());
        assertNull(dto.getWeightKg());
        assertNull(dto.getSource());
    }
}
