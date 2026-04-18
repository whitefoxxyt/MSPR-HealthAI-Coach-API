package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.DietRecommendationDTO;
import fr.epsi.healthaicoachapi.dto.PaginatedResponseDTO;
import fr.epsi.healthaicoachapi.service.DietRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diet-recommendations")
@Tag(name = "Diet Recommendations", description = "Recommandations alimentaires issues du pipeline ETL")
@SecurityRequirement(name = "bearer-jwt")
public class DietRecommendationController {

    private final DietRecommendationService dietRecommendationService;

    public DietRecommendationController(DietRecommendationService dietRecommendationService) {
        this.dietRecommendationService = dietRecommendationService;
    }

    @GetMapping
    @Operation(summary = "Liste paginée des recommandations alimentaires")
    public ResponseEntity<PaginatedResponseDTO<DietRecommendationDTO>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<DietRecommendationDTO> result = dietRecommendationService.findAll(page, pageSize);
        return ResponseEntity.ok(new PaginatedResponseDTO<>(
                result.getContent(),
                (int) result.getTotalElements(),
                page,
                pageSize));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une recommandation alimentaire par ID")
    public ResponseEntity<DietRecommendationDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(dietRecommendationService.findById(id));
    }

    @GetMapping("/stats")
    @Operation(summary = "Statistiques agrégées des recommandations alimentaires")
    public ResponseEntity<DietRecommendationDTO.StatsDTO> getStats() {
        return ResponseEntity.ok(dietRecommendationService.getStats());
    }
}
