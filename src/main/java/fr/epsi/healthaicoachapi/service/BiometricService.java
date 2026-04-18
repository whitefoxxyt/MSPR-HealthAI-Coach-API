package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.BiometricEntryDTO;
import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.repository.BiometricEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BiometricService {

    private static final Logger log = LoggerFactory.getLogger(BiometricService.class);

    private final BiometricEntryRepository biometricEntryRepository;

    public BiometricService(BiometricEntryRepository biometricEntryRepository) {
        this.biometricEntryRepository = biometricEntryRepository;
    }

    @Transactional(readOnly = true)
    public Page<BiometricEntryDTO> listBiometrics(Pageable pageable) {
        return biometricEntryRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<BiometricEntryDTO> listAllBiometrics() {
        return biometricEntryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BiometricEntryDTO getBiometricById(Long id) {
        BiometricEntry entry = biometricEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Biometric entry", id));
        return mapToDTO(entry);
    }

    @Transactional
    public BiometricEntryDTO createBiometric(BiometricEntryDTO dto) {
        BiometricEntry entry = new BiometricEntry();
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
        log.info("Biometric entry {} created", saved.getId());
        return mapToDTO(saved);
    }

    @Transactional
    public BiometricEntryDTO updateBiometric(Long id, BiometricEntryDTO dto) {
        BiometricEntry entry = biometricEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Biometric entry", id));

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
    public void deleteBiometric(Long id) {
        if (!biometricEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Biometric entry", id);
        }
        biometricEntryRepository.deleteById(id);
        log.info("Biometric entry {} deleted", id);
    }

    private BiometricEntryDTO mapToDTO(BiometricEntry entry) {
        return BiometricEntryDTO.builder()
                .id(entry.getId())
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
}
