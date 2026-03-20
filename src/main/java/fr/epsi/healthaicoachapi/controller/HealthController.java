package fr.epsi.healthaicoachapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "API de santé")
public class HealthController {

    public static class HealthStatus {
        private String status = "UP";
        private LocalDateTime timestamp = LocalDateTime.now();
        private String version = "1.0.0";

        public HealthStatus() {
        }

        public HealthStatus(String status, LocalDateTime timestamp, String version) {
            this.status = status;
            this.timestamp = timestamp;
            this.version = version;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    @GetMapping
    @Operation(summary = "Vérifier la santé de l'API")
    public ResponseEntity<HealthStatus> health() {
        return ResponseEntity.ok(new HealthStatus());
    }
}

