package fr.epsi.healthaicoachapi.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("User - Should build with all fields using builder")
    void testBuilder_AllFields() {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .authUserId("auth-123")
                .username("testuser")
                .age(25)
                .gender("M")
                .weightKg(75.0)
                .heightCm(180.0)
                .objective("Perte de poids")
                .createdAt(now)
                .lastActivity(now)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("auth-123", user.getAuthUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals(25, user.getAge());
        assertEquals("M", user.getGender());
        assertEquals(75.0, user.getWeightKg());
        assertEquals(180.0, user.getHeightCm());
        assertEquals("Perte de poids", user.getObjective());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getLastActivity());
    }

    @Test
    @DisplayName("User - Should set and get all fields via setters")
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();

        user.setId(2L);
        user.setAuthUserId("auth-456");
        user.setUsername("anotheruser");
        user.setAge(30);
        user.setGender("F");
        user.setWeightKg(60.0);
        user.setHeightCm(165.0);
        user.setObjective("Prise de masse");
        user.setCreatedAt(now);
        user.setLastActivity(now);

        assertEquals(2L, user.getId());
        assertEquals("auth-456", user.getAuthUserId());
        assertEquals("anotheruser", user.getUsername());
        assertEquals(30, user.getAge());
        assertEquals("F", user.getGender());
        assertEquals(60.0, user.getWeightKg());
        assertEquals(165.0, user.getHeightCm());
        assertEquals("Prise de masse", user.getObjective());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getLastActivity());
    }

    @Test
    @DisplayName("User - Should create with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        User user = new User(1L, "auth-123", "testuser", 25, "M",
                75.0, 180.0, "Endurance", now, now);

        assertEquals(1L, user.getId());
        assertEquals("auth-123", user.getAuthUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("Endurance", user.getObjective());
    }

    @Test
    @DisplayName("User - Default constructor should initialize createdAt automatically")
    void testDefaultConstructor_CreatedAtNotNull() {
        User user = new User();

        assertNotNull(user.getCreatedAt());
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getLastActivity());
    }
}
