package fr.epsi.healthaicoachapi.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NutritionEntryTest {

    private User buildUser() {
        return User.builder()
                .id(1L)
                .authUserId("auth-123")
                .username("testuser")
                .build();
    }

    @Test
    @DisplayName("NutritionEntry - Should set and get all fields via setters")
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        User user = buildUser();
        NutritionEntry entry = new NutritionEntry();

        entry.setId(1L);
        entry.setUser(user);
        entry.setFoodName("Poulet rôti");
        entry.setCategory("Viande");
        entry.setMealType("LUNCH");
        entry.setCalories(new BigDecimal("250.0"));
        entry.setCholesterolMg(new BigDecimal("80.0"));
        entry.setProteinG(new BigDecimal("35.0"));
        entry.setCarbsG(new BigDecimal("0.0"));
        entry.setFatG(new BigDecimal("10.0"));
        entry.setFiberG(new BigDecimal("0.0"));
        entry.setSugarsG(new BigDecimal("0.0"));
        entry.setSodiumMg(new BigDecimal("300.0"));
        entry.setWaterMl(new BigDecimal("50.0"));
        entry.setSource("manual");
        entry.setStatus("BRUT");
        entry.setCreatedAt(now);

        assertEquals(1L, entry.getId());
        assertEquals(user, entry.getUser());
        assertEquals("Poulet rôti", entry.getFoodName());
        assertEquals("Viande", entry.getCategory());
        assertEquals("LUNCH", entry.getMealType());
        assertEquals(new BigDecimal("250.0"), entry.getCalories());
        assertEquals(new BigDecimal("80.0"), entry.getCholesterolMg());
        assertEquals(new BigDecimal("35.0"), entry.getProteinG());
        assertEquals(new BigDecimal("0.0"), entry.getCarbsG());
        assertEquals(new BigDecimal("10.0"), entry.getFatG());
        assertEquals(new BigDecimal("0.0"), entry.getFiberG());
        assertEquals(new BigDecimal("0.0"), entry.getSugarsG());
        assertEquals(new BigDecimal("300.0"), entry.getSodiumMg());
        assertEquals(new BigDecimal("50.0"), entry.getWaterMl());
        assertEquals("manual", entry.getSource());
        assertEquals("BRUT", entry.getStatus());
        assertEquals(now, entry.getCreatedAt());
    }

    @Test
    @DisplayName("NutritionEntry - Should create with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = buildUser();

        NutritionEntry entry = new NutritionEntry(1L, user, "Riz blanc", "Féculents", "DINNER",
                new BigDecimal("130.0"), new BigDecimal("0.0"),
                new BigDecimal("2.5"), new BigDecimal("28.0"), new BigDecimal("0.3"),
                new BigDecimal("0.4"), new BigDecimal("0.1"), new BigDecimal("5.0"),
                new BigDecimal("100.0"), "manual", "BRUT", now);

        assertEquals(1L, entry.getId());
        assertEquals(user, entry.getUser());
        assertEquals("Riz blanc", entry.getFoodName());
        assertEquals("DINNER", entry.getMealType());
        assertEquals(new BigDecimal("130.0"), entry.getCalories());
        assertEquals("manual", entry.getSource());
        assertEquals("BRUT", entry.getStatus());
        assertEquals(now, entry.getCreatedAt());
    }

    @Test
    @DisplayName("NutritionEntry - Default constructor should set default status and createdAt")
    void testDefaultConstructor_Defaults() {
        NutritionEntry entry = new NutritionEntry();

        assertEquals("BRUT", entry.getStatus());
        assertNotNull(entry.getCreatedAt());
        assertNull(entry.getId());
        assertNull(entry.getFoodName());
    }

    @Test
    @DisplayName("NutritionEntry - Should allow updating status")
    void testSetStatus() {
        NutritionEntry entry = new NutritionEntry();
        entry.setStatus("VALIDATED");

        assertEquals("VALIDATED", entry.getStatus());
    }
}
