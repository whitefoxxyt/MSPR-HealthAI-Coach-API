package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.HealthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private final HealthController healthController = new HealthController();

    @Test
    @DisplayName("GET /health - Should return UP status")
    void testHealth_ReturnsUpStatus() {
        ResponseEntity<HealthController.HealthStatus> response = healthController.health();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus());
    }

    @Test
    @DisplayName("GET /health - Should return version 1.0.0")
    void testHealth_ReturnsVersion() {
        ResponseEntity<HealthController.HealthStatus> response = healthController.health();

        assertNotNull(response.getBody());
        assertEquals("1.0.0", response.getBody().getVersion());
    }

    @Test
    @DisplayName("GET /health - Should return a non-null timestamp")
    void testHealth_ReturnsTimestamp() {
        ResponseEntity<HealthController.HealthStatus> response = healthController.health();

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("HealthStatus - Should allow setting custom values")
    void testHealthStatus_SettersAndGetters() {
        HealthController.HealthStatus status = new HealthController.HealthStatus();
        status.setStatus("DOWN");
        status.setVersion("2.0.0");

        assertEquals("DOWN", status.getStatus());
        assertEquals("2.0.0", status.getVersion());
    }
}
