package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.BiometricEntryDTO;
import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import fr.epsi.healthaicoachapi.entity.User;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.exception.UnauthorizedAccessException;
import fr.epsi.healthaicoachapi.repository.BiometricEntryRepository;
import fr.epsi.healthaicoachapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BiometricService {

    private static final Logger log = LoggerFactory.getLogger(BiometricService.class);

    private final BiometricEntryRepository biometricEntryRepository;
    private final UserRepository userRepository;

    public BiometricService(BiometricEntryRepository biometricEntryRepository, UserRepository userRepository) {
        this.biometricEntryRepository = biometricEntryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<BiometricEntryDTO> getUserBiometrics(Long userId, String authUserId) {
        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!authenticatedUser.getId().equals(userId)) {
            throw new UnauthorizedAccessException("You can only access your own biometric data");
        }

        List<BiometricEntry> entries = biometricEntryRepository.findByUserId(userId);
        return entries.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BiometricEntryDTO getBiometricById(Long id, String authUserId) {
        BiometricEntry entry = biometricEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Biometric entry", id));

        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException();
        }

        return mapToDTO(entry);
    }

    @Transactional
    public BiometricEntryDTO createBiometric(BiometricEntryDTO dto, String authUserId) {
        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!authenticatedUser.getId().equals(dto.getUserId())) {
            throw new UnauthorizedAccessException("You can only create biometric entries for yourself");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.getUserId()));

        BiometricEntry entry = new BiometricEntry();
        entry.setUser(user);
        entry.setWeightKg(dto.getWeightKg());
        entry.setHeightCm(dto.getHeightCm());
        entry.setBmi(dto.getBmi());
        entry.setFatPercentage(dto.getFatPercentage());
        entry.setHeartRateRest(dto.getHeartRateRest());
        entry.setHeartRateAvg(dto.getHeartRateAvg());
        entry.setHeartRateMax(dto.getHeartRateMax());
        entry.setBloodPressure(dto.getBloodPressure());
        entry.setSource(dto.getSource());
        entry.setStatus(dto.getStatus() != null ? dto.getStatus() : "BRUT");

        BiometricEntry saved = biometricEntryRepository.save(entry);
        log.info("Biometric entry created for user: {}", user.getId());

        return mapToDTO(saved);
    }

    @Transactional
    public BiometricEntryDTO updateBiometric(Long id, BiometricEntryDTO dto, String authUserId) {
        BiometricEntry entry = biometricEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Biometric entry", id));

        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException();
        }

        if (dto.getWeightKg() != null) entry.setWeightKg(dto.getWeightKg());
        if (dto.getHeightCm() != null) entry.setHeightCm(dto.getHeightCm());
        if (dto.getBmi() != null) entry.setBmi(dto.getBmi());
        if (dto.getFatPercentage() != null) entry.setFatPercentage(dto.getFatPercentage());
        if (dto.getHeartRateRest() != null) entry.setHeartRateRest(dto.getHeartRateRest());
        if (dto.getHeartRateAvg() != null) entry.setHeartRateAvg(dto.getHeartRateAvg());
        if (dto.getHeartRateMax() != null) entry.setHeartRateMax(dto.getHeartRateMax());
        if (dto.getBloodPressure() != null) entry.setBloodPressure(dto.getBloodPressure());
        if (dto.getStatus() != null) entry.setStatus(dto.getStatus());

        BiometricEntry updated = biometricEntryRepository.save(entry);
        log.info("Biometric entry {} updated", id);

        return mapToDTO(updated);
    }

    @Transactional
    public void deleteBiometric(Long id, String authUserId) {
        BiometricEntry entry = biometricEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Biometric entry", id));

        User authenticatedUser = getUserByAuthUserId(authUserId);
        
        if (!entry.getUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException();
        }

        biometricEntryRepository.deleteById(id);
        log.info("Biometric entry {} deleted", id);
    }

    private BiometricEntryDTO mapToDTO(BiometricEntry entry) {
        return BiometricEntryDTO.builder()
                .id(entry.getId())
                .userId(entry.getUser().getId())
                .weightKg(entry.getWeightKg())
                .heightCm(entry.getHeightCm())
                .bmi(entry.getBmi())
                .fatPercentage(entry.getFatPercentage())
                .heartRateRest(entry.getHeartRateRest())
                .heartRateAvg(entry.getHeartRateAvg())
                .heartRateMax(entry.getHeartRateMax())
                .bloodPressure(entry.getBloodPressure())
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
