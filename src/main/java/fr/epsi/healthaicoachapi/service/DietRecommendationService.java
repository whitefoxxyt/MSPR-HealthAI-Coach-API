package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.DietRecommendationDTO;
import fr.epsi.healthaicoachapi.entity.DietRecommendation;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.repository.DietRecommendationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DietRecommendationService {

    private final DietRecommendationRepository repository;

    public DietRecommendationService(DietRecommendationRepository repository) {
        this.repository = repository;
    }

    public Page<DietRecommendationDTO> findAll(int page, int pageSize) {
        return repository.findAll(PageRequest.of(page - 1, pageSize, Sort.by("id")))
                .map(this::mapToDTO);
    }

    public DietRecommendationDTO findById(Long id) {
        DietRecommendation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DietRecommendation", id));
        return mapToDTO(entity);
    }

    public DietRecommendationDTO.StatsDTO getStats() {
        DietRecommendationDTO.StatsDTO stats = new DietRecommendationDTO.StatsDTO();
        stats.setTotalCount(repository.count());

        Double avgAdherence = repository.avgAdherence();
        Double avgImbalance = repository.avgNutrientImbalance();
        stats.setAvgAdherenceRate(avgAdherence != null ? round1(avgAdherence) : 0.0);
        stats.setAvgNutrientImbalanceScore(avgImbalance != null ? round1(avgImbalance) : 0.0);

        stats.setDiseaseTypeDistribution(toItemList(repository.countByDiseaseType()));
        stats.setSeverityDistribution(toItemList(repository.countBySeverity()));
        stats.setActivityLevelDistribution(toItemList(repository.countByPhysicalActivityLevel()));
        stats.setDietRecommendationDistribution(toItemList(repository.countByDietRecommendation()));

        return stats;
    }

    private List<DietRecommendationDTO.StatsDTO.Item> toItemList(List<Object[]> rows) {
        return rows.stream()
                .map(row -> new DietRecommendationDTO.StatsDTO.Item(
                        String.valueOf(row[0]),
                        ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    private DietRecommendationDTO mapToDTO(DietRecommendation e) {
        DietRecommendationDTO dto = new DietRecommendationDTO();
        dto.setId(e.getId());
        dto.setExternalPatientId(e.getExternalPatientId());
        dto.setAge(e.getAge());
        dto.setGender(e.getGender());
        dto.setWeightKg(e.getWeightKg());
        dto.setHeightCm(e.getHeightCm());
        dto.setBmi(e.getBmi());
        dto.setDiseaseType(e.getDiseaseType());
        dto.setSeverity(e.getSeverity());
        dto.setPhysicalActivityLevel(e.getPhysicalActivityLevel());
        dto.setDailyCaloricIntake(e.getDailyCaloricIntake());
        dto.setCholesterolMgDl(e.getCholesterolMgDl());
        dto.setBloodPressureMmhg(e.getBloodPressureMmhg());
        dto.setGlucoseMgDl(e.getGlucoseMgDl());
        dto.setDietaryRestrictions(e.getDietaryRestrictions());
        dto.setAllergies(e.getAllergies());
        dto.setPreferredCuisine(e.getPreferredCuisine());
        dto.setWeeklyExerciseHours(e.getWeeklyExerciseHours());
        dto.setAdherenceToDietPlan(e.getAdherenceToDietPlan());
        dto.setNutrientImbalanceScore(e.getNutrientImbalanceScore());
        dto.setDietRecommendation(e.getDietRecommendation());
        dto.setSource(e.getSource());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
