package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.DataRecordDTO;
import fr.epsi.healthaicoachapi.dto.PaginatedResponseDTO;
import fr.epsi.healthaicoachapi.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Liste des enregistrements à valider")
    public ResponseEntity<PaginatedResponseDTO<DataRecordDTO>> getRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(validationService.getRecords(page, pageSize, status));
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
