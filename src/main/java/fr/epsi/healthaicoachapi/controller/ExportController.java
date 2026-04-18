package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/export")
@Tag(name = "Export", description = "Export des données")
@SecurityRequirement(name = "bearer-jwt")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping
    @Operation(summary = "Exporter les données en JSON ou CSV")
    public ResponseEntity<byte[]> exportData(@RequestBody Map<String, Object> options) throws IOException {
        String format = (String) options.getOrDefault("format", "json");
        @SuppressWarnings("unchecked")
        List<String> entityTypes = (List<String>) options.get("entityTypes");

        byte[] data = exportService.export(format, entityTypes);

        MediaType mediaType = "csv".equalsIgnoreCase(format)
                ? MediaType.parseMediaType("text/csv")
                : MediaType.APPLICATION_JSON;
        String filename = "export." + format.toLowerCase();

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(data);
    }
}
