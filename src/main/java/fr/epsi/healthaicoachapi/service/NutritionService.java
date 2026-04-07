package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.NutritionEntryDTO;
import fr.epsi.healthaicoachapi.entity.NutritionEntry;
import fr.epsi.healthaicoachapi.entity.User;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.exception.UnauthorizedAccessException;
import fr.epsi.healthaicoachapi.repository.NutritionEntryRepository;
import fr.epsi.healthaicoachapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutritionService {

    private static final Logger log = LoggerFactory.getLogger(NutritionService.class);

    private final NutritionEntryRepository nutritionEntryRepository;
    private final UserRepository userRepository;

    public NutritionService(NutritionEntryRepository nutritionEntryRepository, UserRepository userRepository) {
        this.nutritionEntryRepository = nutritionEntryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<NutritionEntryDTO> getUserNutritionEntries(Long userId, String authUserId) {
        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!authenticatedUser.getId().equals(userId)) {
            throw new UnauthorizedAccessException("You can only access your own nutrition data");
        }

        List<NutritionEntry> entries = nutritionEntryRepository.findByUserId(userId);
        return entries.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NutritionEntryDTO getNutritionEntryById(Long id, String authUserId) {
        NutritionEntry entry = nutritionEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition entry", id));

        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException();
        }

        return mapToDTO(entry);
    }

    @Transactional
    public NutritionEntryDTO createNutritionEntry(NutritionEntryDTO dto, String authUserId) {
        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!authenticatedUser.getId().equals(dto.getUserId())) {
            throw new UnauthorizedAccessException("You can only create nutrition entries for yourself");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.getUserId()));

        NutritionEntry entry = new NutritionEntry();
        entry.setUser(user);
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
        log.info("Nutrition entry created for user: {}", user.getId());

        return mapToDTO(saved);
    }

    @Transactional
    public NutritionEntryDTO updateNutritionEntry(Long id, NutritionEntryDTO dto, String authUserId) {
        NutritionEntry entry = nutritionEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition entry", id));

        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException();
        }

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
    public void deleteNutritionEntry(Long id, String authUserId) {
        NutritionEntry entry = nutritionEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition entry", id));

        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException();
        }

        nutritionEntryRepository.deleteById(id);
        log.info("Nutrition entry {} deleted", id);
    }

    private NutritionEntryDTO mapToDTO(NutritionEntry entry) {
        return NutritionEntryDTO.builder()
                .id(entry.getId())
                .userId(entry.getUser().getId())
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

    private User getUserByAuthUserId(String authUserId) {
        return userRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
