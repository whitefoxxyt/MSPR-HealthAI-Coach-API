package fr.epsi.healthaicoachapi;

import fr.epsi.healthaicoachapi.controller.HealthController;
import fr.epsi.healthaicoachapi.controller.HealthController.HealthStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @Test
    @DisplayName("GET /health - Should return health status")
    void testHealthCheck_Success() {
        // When
        ResponseEntity<HealthStatus> response = healthController.health();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus());
        assertEquals("1.0.0", response.getBody().getVersion());
        assertNotNull(response.getBody().getTimestamp());
    }
}
