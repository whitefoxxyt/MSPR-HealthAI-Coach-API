package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.*;
import fr.epsi.healthaicoachapi.service.DataQualityService;
import fr.epsi.healthaicoachapi.service.EtlReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Data Quality", description = "Métriques de qualité des données et détection d'anomalies")
@SecurityRequirement(name = "bearer-jwt")
public class DataQualityController {

    private final DataQualityService dataQualityService;
    private final EtlReportService etlReportService;

    public DataQualityController(DataQualityService dataQualityService, EtlReportService etlReportService) {
        this.dataQualityService = dataQualityService;
        this.etlReportService = etlReportService;
    }

    @GetMapping("/data-quality/metrics")
    @Operation(summary = "Métriques globales de qualité des données")
    public ResponseEntity<DataQualityMetricsDTO> getMetrics() {
        return ResponseEntity.ok(dataQualityService.getMetrics());
    }

    @GetMapping("/data-quality/anomalies")
    @Operation(summary = "Liste des anomalies détectées")
    public ResponseEntity<PaginatedResponseDTO<DataAnomalyDTO>> getAnomalies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<DataAnomalyDTO> anomalies = dataQualityService.detectAnomalies();
        int total = anomalies.size();
        int from = Math.min((page - 1) * pageSize, total);
        int to = Math.min(from + pageSize, total);
        return ResponseEntity.ok(new PaginatedResponseDTO<>(anomalies.subList(from, to), total, page, pageSize));
    }

    @PutMapping("/data-quality/anomalies/{id}")
    @Operation(summary = "Résoudre une anomalie")
    public ResponseEntity<DataAnomalyDTO> updateAnomaly(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(dataQualityService.resolveAnomaly(id, updates));
    }

    @DeleteMapping("/data-quality/anomalies/{id}")
    @Operation(summary = "Supprimer une anomalie (rejeter l'entrée)")
    public ResponseEntity<Void> deleteAnomaly(@PathVariable String id) {
        dataQualityService.deleteAnomaly(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/data-quality/flows")
    @Operation(summary = "Statistiques des flux de données")
    public ResponseEntity<List<DataFlowStatsDTO>> getDataFlowStats() {
        return ResponseEntity.ok(dataQualityService.getDataFlowStats());
    }

    @GetMapping("/analytics/overview")
    @Operation(summary = "Vue d'ensemble analytique")
    public ResponseEntity<AnalyticsOverviewDTO> getAnalyticsOverview() {
        return ResponseEntity.ok(dataQualityService.getAnalyticsOverview());
    }

    @GetMapping("/data-quality/etl-report")
    @Operation(summary = "Rapport qualité ETL (dernière exécution)")
    public ResponseEntity<?> getEtlReport() {
        try {
            return ResponseEntity.ok(etlReportService.getReport());
        } catch (IOException e) {
            return ResponseEntity.status(503).body(Map.of("error", e.getMessage()));
        }
    }
}
