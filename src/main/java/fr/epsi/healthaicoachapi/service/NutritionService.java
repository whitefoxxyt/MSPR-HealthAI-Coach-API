package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.NutritionEntryDTO;
import fr.epsi.healthaicoachapi.entity.NutritionEntry;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.repository.NutritionEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutritionService {

    private static final Logger log = LoggerFactory.getLogger(NutritionService.class);

    private final NutritionEntryRepository nutritionEntryRepository;

    public NutritionService(NutritionEntryRepository nutritionEntryRepository) {
        this.nutritionEntryRepository = nutritionEntryRepository;
    }

    @Transactional(readOnly = true)
    public Page<NutritionEntryDTO> listNutritionEntries(Pageable pageable) {
        return nutritionEntryRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<NutritionEntryDTO> listAllNutritionEntries() {
        return nutritionEntryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NutritionEntryDTO getNutritionEntryById(Long id) {
        NutritionEntry entry = nutritionEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition entry", id));
        return mapToDTO(entry);
    }

    @Transactional
    public NutritionEntryDTO createNutritionEntry(NutritionEntryDTO dto) {
        NutritionEntry entry = new NutritionEntry();
        entry.setFoodName(dto.getFoodName());
        entry.setCategory(dto.getCategory());
        entry.setMealType(dto.getMealType());
        entry.setCalories(dto.getCalories());
        entry.setCholesterolMg(dto.getCholesterolMg());
        entry.setProteinG(dto.getProteinG());
        entry.setCarbsG(dto.getCarbsG());
        entry.setFatG(dto.getFatG());
        entry.setFiberG(dto.getFiberG());
        entry.setSugarsG(dto.getSugarsG());
        entry.setSodiumMg(dto.getSodiumMg());
        entry.setWaterMl(dto.getWaterMl());
        entry.setSource(dto.getSource());
        entry.setStatus(dto.getStatus() != null ? dto.getStatus() : "BRUT");

        NutritionEntry saved = nutritionEntryRepository.save(entry);
        log.info("Nutrition entry {} created", saved.getId());
        return mapToDTO(saved);
    }

    @Transactional
    public NutritionEntryDTO updateNutritionEntry(Long id, NutritionEntryDTO dto) {
        NutritionEntry entry = nutritionEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition entry", id));

        if (dto.getFoodName() != null) entry.setFoodName(dto.getFoodName());
        if (dto.getCategory() != null) entry.setCategory(dto.getCategory());
        if (dto.getMealType() != null) entry.setMealType(dto.getMealType());
        if (dto.getCalories() != null) entry.setCalories(dto.getCalories());
        if (dto.getCholesterolMg() != null) entry.setCholesterolMg(dto.getCholesterolMg());
        if (dto.getProteinG() != null) entry.setProteinG(dto.getProteinG());
        if (dto.getCarbsG() != null) entry.setCarbsG(dto.getCarbsG());
        if (dto.getFatG() != null) entry.setFatG(dto.getFatG());
        if (dto.getFiberG() != null) entry.setFiberG(dto.getFiberG());
        if (dto.getSugarsG() != null) entry.setSugarsG(dto.getSugarsG());
        if (dto.getSodiumMg() != null) entry.setSodiumMg(dto.getSodiumMg());
        if (dto.getWaterMl() != null) entry.setWaterMl(dto.getWaterMl());
        if (dto.getStatus() != null) entry.setStatus(dto.getStatus());

        NutritionEntry updated = nutritionEntryRepository.save(entry);
        log.info("Nutrition entry {} updated", id);
        return mapToDTO(updated);
    }

    @Transactional
    public void deleteNutritionEntry(Long id) {
        if (!nutritionEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nutrition entry", id);
        }
        nutritionEntryRepository.deleteById(id);
        log.info("Nutrition entry {} deleted", id);
    }

    private NutritionEntryDTO mapToDTO(NutritionEntry entry) {
        return NutritionEntryDTO.builder()
                .id(entry.getId())
                .foodName(entry.getFoodName())
                .category(entry.getCategory())
                .mealType(entry.getMealType())
                .calories(entry.getCalories())
                .cholesterolMg(entry.getCholesterolMg())
                .proteinG(entry.getProteinG())
                .carbsG(entry.getCarbsG())
                .fatG(entry.getFatG())
                .fiberG(entry.getFiberG())
                .sugarsG(entry.getSugarsG())
                .sodiumMg(entry.getSodiumMg())
                .waterMl(entry.getWaterMl())
                .source(entry.getSource())
                .status(entry.getStatus())
                .createdAt(entry.getCreatedAt())
                .build();
    }
}
