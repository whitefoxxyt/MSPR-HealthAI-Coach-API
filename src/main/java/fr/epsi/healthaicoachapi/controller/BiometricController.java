package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.BiometricEntryDTO;
import fr.epsi.healthaicoachapi.service.BiometricService;
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
@RequestMapping("/biometrics")
@Tag(name = "Biometrics", description = "API de suivi biométrique")
@SecurityRequirement(name = "bearer-jwt")
public class BiometricController {

    private static final Logger log = LoggerFactory.getLogger(BiometricController.class);

    private final BiometricService biometricService;

    public BiometricController(BiometricService biometricService) {
        this.biometricService = biometricService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Liste les données biométriques d'un utilisateur")
    public ResponseEntity<List<BiometricEntryDTO>> getUserBiometrics(@PathVariable Long userId) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<BiometricEntryDTO> entries = biometricService.getUserBiometrics(userId, email);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une entrée biométrique par ID")
    public ResponseEntity<BiometricEntryDTO> getBiometricById(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BiometricEntryDTO entry = biometricService.getBiometricById(id, email);
        return ResponseEntity.ok(entry);
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle entrée biométrique")
    public ResponseEntity<BiometricEntryDTO> createBiometric(@Valid @RequestBody BiometricEntryDTO dto) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BiometricEntryDTO saved = biometricService.createBiometric(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une entrée biométrique")
    public ResponseEntity<BiometricEntryDTO> updateBiometric(@PathVariable Long id, @Valid @RequestBody BiometricEntryDTO dto) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BiometricEntryDTO updated = biometricService.updateBiometric(id, dto, email);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une entrée biométrique")
    public ResponseEntity<Void> deleteBiometric(@PathVariable Long id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        biometricService.deleteBiometric(id, email);
        return ResponseEntity.noContent().build();
    }
}

