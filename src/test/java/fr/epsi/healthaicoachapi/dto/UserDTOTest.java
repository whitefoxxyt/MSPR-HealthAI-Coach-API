package fr.epsi.healthaicoachapi.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    @DisplayName("UserDTO - Should build with all fields using builder")
    void testBuilder_AllFields() {
        LocalDateTime now = LocalDateTime.now();

        UserDTO dto = UserDTO.builder()
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

        assertEquals(1L, dto.getId());
        assertEquals("auth-123", dto.getAuthUserId());
        assertEquals("testuser", dto.getUsername());
        assertEquals(25, dto.getAge());
        assertEquals("M", dto.getGender());
        assertEquals(75.0, dto.getWeightKg());
        assertEquals(180.0, dto.getHeightCm());
        assertEquals("Perte de poids", dto.getObjective());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getLastActivity());
    }

    @Test
    @DisplayName("UserDTO - Should set and get all fields via setters")
    void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        UserDTO dto = new UserDTO();

        dto.setId(2L);
        dto.setAuthUserId("auth-456");
        dto.setUsername("anotheruser");
        dto.setAge(30);
        dto.setGender("F");
        dto.setWeightKg(60.0);
        dto.setHeightCm(165.0);
        dto.setObjective("Prise de masse");
        dto.setCreatedAt(now);
        dto.setLastActivity(now);

        assertEquals(2L, dto.getId());
        assertEquals("auth-456", dto.getAuthUserId());
        assertEquals("anotheruser", dto.getUsername());
        assertEquals(30, dto.getAge());
        assertEquals("F", dto.getGender());
        assertEquals(60.0, dto.getWeightKg());
        assertEquals(165.0, dto.getHeightCm());
        assertEquals("Prise de masse", dto.getObjective());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getLastActivity());
    }

    @Test
    @DisplayName("UserDTO - Should create with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        UserDTO dto = new UserDTO(1L, "auth-123", "testuser", 25, "M",
                75.0, 180.0, "Endurance", now, now);

        assertEquals(1L, dto.getId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("Endurance", dto.getObjective());
    }

    @Test
    @DisplayName("UserDTO - Should have null fields with default constructor")
    void testDefaultConstructor_NullFields() {
        UserDTO dto = new UserDTO();

        assertNull(dto.getId());
        assertNull(dto.getUsername());
        assertNull(dto.getObjective());
        assertNull(dto.getCreatedAt());
    }
}
