package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.BiometricEntryDTO;
import fr.epsi.healthaicoachapi.service.BiometricService;
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
@RequestMapping("/biometrics")
@Tag(name = "Biometrics", description = "API de suivi biométrique")
@SecurityRequirement(name = "bearer-jwt")
public class BiometricController {

    private final BiometricService biometricService;

    public BiometricController(BiometricService biometricService) {
        this.biometricService = biometricService;
    }

    @GetMapping
    @Operation(summary = "Liste paginée des entrées biométriques")
    public ResponseEntity<Page<BiometricEntryDTO>> listBiometrics(Pageable pageable) {
        return ResponseEntity.ok(biometricService.listBiometrics(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une entrée biométrique par ID")
    public ResponseEntity<BiometricEntryDTO> getBiometricById(@PathVariable Long id) {
        return ResponseEntity.ok(biometricService.getBiometricById(id));
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle entrée biométrique")
    public ResponseEntity<BiometricEntryDTO> createBiometric(@Valid @RequestBody BiometricEntryDTO dto) {
        BiometricEntryDTO saved = biometricService.createBiometric(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une entrée biométrique")
    public ResponseEntity<BiometricEntryDTO> updateBiometric(@PathVariable Long id, @RequestBody BiometricEntryDTO dto) {
        return ResponseEntity.ok(biometricService.updateBiometric(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une entrée biométrique")
    public ResponseEntity<Void> deleteBiometric(@PathVariable Long id) {
        biometricService.deleteBiometric(id);
        return ResponseEntity.noContent().build();
    }
}
