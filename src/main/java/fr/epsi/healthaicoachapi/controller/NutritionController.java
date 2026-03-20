package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.entity.NutritionEntry;
import fr.epsi.healthaicoachapi.repository.NutritionEntryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutrition")
@Tag(name = "Nutrition", description = "API de suivi nutritionnel")
@SecurityRequirement(name = "bearer-jwt")
public class NutritionController {

    private static final Logger log = LoggerFactory.getLogger(NutritionController.class);

    private final NutritionEntryRepository nutritionEntryRepository;

    public NutritionController(NutritionEntryRepository nutritionEntryRepository) {
        this.nutritionEntryRepository = nutritionEntryRepository;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Liste les entrées nutritionnelles d'un utilisateur")
    public ResponseEntity<List<NutritionEntry>> getUserNutritionEntries(@PathVariable Long userId) {
        List<NutritionEntry> entries = nutritionEntryRepository.findByUserId(userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une entrée nutritionnelle par ID")
    public ResponseEntity<NutritionEntry> getNutritionEntryById(@PathVariable Long id) {
        return nutritionEntryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle entrée nutritionnelle")
    public ResponseEntity<NutritionEntry> createNutritionEntry(@RequestBody NutritionEntry entry) {
        try {
            NutritionEntry saved = nutritionEntryRepository.save(entry);
            log.info("Nutrition entry created for user: {}", entry.getUser().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Error creating nutrition entry: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une entrée nutritionnelle")
    public ResponseEntity<NutritionEntry> updateNutritionEntry(@PathVariable Long id, @RequestBody NutritionEntry entryDetails) {
        return nutritionEntryRepository.findById(id)
                .map(entry -> {
                    entry.setFoodName(entryDetails.getFoodName());
                    entry.setCategory(entryDetails.getCategory());
                    entry.setMealType(entryDetails.getMealType());
                    entry.setCalories(entryDetails.getCalories());
                    entry.setProteinG(entryDetails.getProteinG());
                    entry.setCarbsG(entryDetails.getCarbsG());
                    entry.setFatG(entryDetails.getFatG());
                    entry.setStatus(entryDetails.getStatus());
                    NutritionEntry updated = nutritionEntryRepository.save(entry);
                    log.info("Nutrition entry {} updated", id);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une entrée nutritionnelle")
    public ResponseEntity<Void> deleteNutritionEntry(@PathVariable Long id) {
        if (nutritionEntryRepository.existsById(id)) {
            nutritionEntryRepository.deleteById(id);
            log.info("Nutrition entry {} deleted", id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

