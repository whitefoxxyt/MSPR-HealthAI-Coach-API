package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.NutritionEntryDTO;
import fr.epsi.healthaicoachapi.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutrition")
@Tag(name = "Nutrition", description = "API de suivi nutritionnel")
@SecurityRequirement(name = "bearer-jwt")
public class NutritionController {

    private static final Logger log = LoggerFactory.getLogger(NutritionController.class);

    private final NutritionService nutritionService;

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Liste les entrées nutritionnelles d'un utilisateur")
    public ResponseEntity<List<NutritionEntryDTO>> getUserNutritionEntries(@PathVariable Long userId) {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NutritionEntryDTO> entries = nutritionService.getUserNutritionEntries(userId, authUserId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une entrée nutritionnelle par ID")
    public ResponseEntity<NutritionEntryDTO> getNutritionEntryById(@PathVariable Long id) {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        NutritionEntryDTO entry = nutritionService.getNutritionEntryById(id, authUserId);
        return ResponseEntity.ok(entry);
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle entrée nutritionnelle")
    public ResponseEntity<NutritionEntryDTO> createNutritionEntry(@Valid @RequestBody NutritionEntryDTO dto) {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        NutritionEntryDTO saved = nutritionService.createNutritionEntry(dto, authUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une entrée nutritionnelle")
    public ResponseEntity<NutritionEntryDTO> updateNutritionEntry(@PathVariable Long id, @Valid @RequestBody NutritionEntryDTO dto) {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        NutritionEntryDTO updated = nutritionService.updateNutritionEntry(id, dto, authUserId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une entrée nutritionnelle")
    public ResponseEntity<Void> deleteNutritionEntry(@PathVariable Long id) {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        nutritionService.deleteNutritionEntry(id, authUserId);
        return ResponseEntity.noContent().build();
    }
}

