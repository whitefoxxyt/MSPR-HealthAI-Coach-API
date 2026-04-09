package fr.epsi.healthaicoachapi.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("ResourceNotFoundException - Should store message from simple constructor")
    void testSimpleConstructor() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        assertEquals("User not found", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    @DisplayName("ResourceNotFoundException - Should format message with resource name and ID")
    void testResourceNameAndIdConstructor() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User", 42L);

        assertEquals("User with id 42 not found", ex.getMessage());
    }

    @Test
    @DisplayName("ResourceNotFoundException - Should format message correctly for different resources")
    void testResourceNameAndIdConstructor_DifferentResources() {
        ResourceNotFoundException biometric = new ResourceNotFoundException("BiometricEntry", 99L);
        ResourceNotFoundException nutrition = new ResourceNotFoundException("NutritionEntry", 1L);

        assertEquals("BiometricEntry with id 99 not found", biometric.getMessage());
        assertEquals("NutritionEntry with id 1 not found", nutrition.getMessage());
    }
}
