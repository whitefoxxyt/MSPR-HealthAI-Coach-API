package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.NutritionEntryDTO;
import fr.epsi.healthaicoachapi.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nutrition")
@Tag(name = "Nutrition", description = "API de suivi nutritionnel")
@SecurityRequirement(name = "bearer-jwt")
public class NutritionController {

    private final NutritionService nutritionService;

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping
    @Operation(summary = "Liste paginée des entrées nutritionnelles")
    public ResponseEntity<Page<NutritionEntryDTO>> listNutritionEntries(Pageable pageable) {
        return ResponseEntity.ok(nutritionService.listNutritionEntries(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une entrée nutritionnelle par ID")
    public ResponseEntity<NutritionEntryDTO> getNutritionEntryById(@PathVariable Long id) {
        return ResponseEntity.ok(nutritionService.getNutritionEntryById(id));
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle entrée nutritionnelle")
    public ResponseEntity<NutritionEntryDTO> createNutritionEntry(@Valid @RequestBody NutritionEntryDTO dto) {
        NutritionEntryDTO saved = nutritionService.createNutritionEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une entrée nutritionnelle")
    public ResponseEntity<NutritionEntryDTO> updateNutritionEntry(@PathVariable Long id, @Valid @RequestBody NutritionEntryDTO dto) {
        return ResponseEntity.ok(nutritionService.updateNutritionEntry(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une entrée nutritionnelle")
    public ResponseEntity<Void> deleteNutritionEntry(@PathVariable Long id) {
        nutritionService.deleteNutritionEntry(id);
        return ResponseEntity.noContent().build();
    }
}
