package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.BulkValidateRequestDTO;
import fr.epsi.healthaicoachapi.dto.BulkValidateResponseDTO;
import fr.epsi.healthaicoachapi.dto.DataRecordDTO;
import fr.epsi.healthaicoachapi.dto.PaginatedResponseDTO;
import fr.epsi.healthaicoachapi.dto.SourcesByTypeDTO;
import fr.epsi.healthaicoachapi.dto.ValidationCountsDTO;
import fr.epsi.healthaicoachapi.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/validation")
@Tag(name = "Validation", description = "Workflow de validation des enregistrements")
@SecurityRequirement(name = "bearer-jwt")
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @GetMapping("/records")
    @Operation(summary = "Liste paginée avec filtres")
    public ResponseEntity<PaginatedResponseDTO<DataRecordDTO>> getRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {
        return ResponseEntity.ok(validationService.getRecords(page, pageSize, status, type, source, dateFrom, dateTo));
    }

    @GetMapping("/counts")
    @Operation(summary = "Compteurs globaux par statut")
    public ResponseEntity<ValidationCountsDTO> getCounts() {
        return ResponseEntity.ok(validationService.getCounts());
    }

    @GetMapping("/ids")
    @Operation(summary = "Tous les IDs correspondant aux filtres (pour select-all)")
    public ResponseEntity<List<String>> getFilteredIds(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {
        return ResponseEntity.ok(validationService.getFilteredIds(status, type, source, dateFrom, dateTo));
    }

    @GetMapping("/sample")
    @Operation(summary = "Échantillon aléatoire de records pour validation")
    public ResponseEntity<List<DataRecordDTO>> getSample(
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {
        return ResponseEntity.ok(validationService.getSample(size, status, type, source, dateFrom, dateTo));
    }

    @GetMapping("/sources")
    @Operation(summary = "Sources distinctes par type d'enregistrement")
    public ResponseEntity<SourcesByTypeDTO> getSources() {
        return ResponseEntity.ok(validationService.getSources());
    }

    @PostMapping("/records/bulk-validate")
    @Operation(summary = "Valider ou rejeter plusieurs records en une requête")
    public ResponseEntity<BulkValidateResponseDTO> bulkValidate(@RequestBody BulkValidateRequestDTO body) {
        return ResponseEntity.ok(validationService.bulkValidate(body.getIds(), body.getStatus()));
    }

    @PutMapping("/records/{id}")
    @Operation(summary = "Mettre à jour un enregistrement")
    public ResponseEntity<DataRecordDTO> updateRecord(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(validationService.updateRecord(id, updates));
    }

    @PostMapping("/records/{id}/validate")
    @Operation(summary = "Valider ou rejeter un enregistrement")
    public ResponseEntity<DataRecordDTO> validateRecord(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(validationService.validateRecord(id, status));
    }
}
