package fr.epsi.healthaicoachapi.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NutritionEntryDTOTest {

    @Test
    @DisplayName("NutritionEntryDTO - Should build with all fields using builder")
    void testBuilder_AllFields() {
        LocalDateTime now = LocalDateTime.now();

        NutritionEntryDTO dto = NutritionEntryDTO.builder()
                .id(1L)
                .userId(10L)
                .foodName("Poulet rôti")
                .category("Viande")
                .mealType("LUNCH")
                .calories(new BigDecimal("250.0"))
                .cholesterolMg(new BigDecimal("80.0"))
                .proteinG(new BigDecimal("35.0"))
                .carbsG(new BigDecimal("0.0"))
                .fatG(new BigDecimal("10.0"))
                .fiberG(new BigDecimal("0.0"))
                .sugarsG(new BigDecimal("0.0"))
                .sodiumMg(new BigDecimal("300.0"))
                .waterMl(new BigDecimal("50.0"))
                .source("manual")
                .status("ACTIVE")
                .createdAt(now)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals("Poulet rôti", dto.getFoodName());
        assertEquals("Viande", dto.getCategory());
        assertEquals("LUNCH", dto.getMealType());
        assertEquals(new BigDecimal("250.0"), dto.getCalories());
        assertEquals(new BigDecimal("80.0"), dto.getCholesterolMg());
        assertEquals(new BigDecimal("35.0"), dto.getProteinG());
        assertEquals(new BigDecimal("0.0"), dto.getCarbsG());
        assertEquals(new BigDecimal("10.0"), dto.getFatG());
        assertEquals(new BigDecimal("0.0"), dto.getFiberG());
        assertEquals(new BigDecimal("0.0"), dto.getSugarsG());
        assertEquals(new BigDecimal("300.0"), dto.getSodiumMg());
        assertEquals(new BigDecimal("50.0"), dto.getWaterMl());
        assertEquals("manual", dto.getSource());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    @DisplayName("NutritionEntryDTO - Should set and get all fields via setters")
    void testSettersAndGetters() {
        NutritionEntryDTO dto = new NutritionEntryDTO();

        dto.setId(2L);
        dto.setUserId(20L);
        dto.setFoodName("Riz blanc");
        dto.setCategory("Féculents");
        dto.setMealType("DINNER");
        dto.setCalories(new BigDecimal("130.0"));
        dto.setProteinG(new BigDecimal("2.5"));
        dto.setCarbsG(new BigDecimal("28.0"));
        dto.setFatG(new BigDecimal("0.3"));
        dto.setFiberG(new BigDecimal("0.4"));
        dto.setSugarsG(new BigDecimal("0.1"));
        dto.setSodiumMg(new BigDecimal("5.0"));
        dto.setWaterMl(new BigDecimal("100.0"));
        dto.setSource("application");
        dto.setStatus("ACTIVE");

        assertEquals(2L, dto.getId());
        assertEquals(20L, dto.getUserId());
        assertEquals("Riz blanc", dto.getFoodName());
        assertEquals("Féculents", dto.getCategory());
        assertEquals("DINNER", dto.getMealType());
        assertEquals(new BigDecimal("130.0"), dto.getCalories());
        assertEquals(new BigDecimal("2.5"), dto.getProteinG());
        assertEquals(new BigDecimal("28.0"), dto.getCarbsG());
        assertEquals(new BigDecimal("0.3"), dto.getFatG());
        assertEquals("application", dto.getSource());
    }

    @Test
    @DisplayName("NutritionEntryDTO - Should have null fields with default constructor")
    void testDefaultConstructor_NullFields() {
        NutritionEntryDTO dto = new NutritionEntryDTO();

        assertNull(dto.getId());
        assertNull(dto.getUserId());
        assertNull(dto.getFoodName());
        assertNull(dto.getCalories());
        assertNull(dto.getSource());
    }
}
