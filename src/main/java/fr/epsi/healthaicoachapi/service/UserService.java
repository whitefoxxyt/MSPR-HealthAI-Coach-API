package fr.epsi.healthaicoachapi.service;

import fr.epsi.healthaicoachapi.dto.UserDTO;
import fr.epsi.healthaicoachapi.entity.User;
import fr.epsi.healthaicoachapi.exception.ResourceNotFoundException;
import fr.epsi.healthaicoachapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return mapToDTO(user);
    }

    @Transactional
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        return mapToDTO(user);
    }

    @Transactional
    public UserDTO updateLastActivity(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        user.setLastActivity(LocalDateTime.now());
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    @Transactional
    public UserDTO updateUserProfile(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (userDTO.getAge() != null) {
            user.setAge(userDTO.getAge());
        }
        if (userDTO.getGender() != null) {
            user.setGender(userDTO.getGender());
        }
        if (userDTO.getWeightKg() != null) {
            user.setWeightKg(userDTO.getWeightKg());
        }
        if (userDTO.getHeightCm() != null) {
            user.setHeightCm(userDTO.getHeightCm());
        }
        if (userDTO.getObjective() != null) {
            user.setObjective(userDTO.getObjective());
        }

        User updated = userRepository.save(user);
        log.info("User profile updated: {}", userId);
        return mapToDTO(updated);
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getAccountUsername())
                .role(user.getRole())
                .isPremium(user.getIsPremium())
                .age(user.getAge())
                .gender(user.getGender())
                .weightKg(user.getWeightKg())
                .heightCm(user.getHeightCm())
                .objective(user.getObjective())
                .createdAt(user.getCreatedAt())
                .lastActivity(user.getLastActivity())
                .build();
    }
}

