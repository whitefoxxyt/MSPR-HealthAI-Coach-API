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
        return userRepository.findByEmail(email)
                .map(this::mapToDTO)
                .orElseGet(() -> provisionUser(email, null));
    }

    /**
     * Crée un profil minimal pour un utilisateur authentifié via Better-Auth
     * dont le compte n'existe pas encore dans la base healthai.
     * Le profil peut être complété ensuite via updateUserProfile().
     */
    @Transactional
    public UserDTO getOrProvisionUser(String email, String name) {
        return userRepository.findByEmail(email)
                .map(this::mapToDTO)
                .orElseGet(() -> provisionUser(email, name));
    }

    private UserDTO provisionUser(String email, String name) {
        String username = (name != null && !name.isBlank()) ? name : email.split("@")[0];
        User newUser = User.builder()
                .email(email)
                .username(username)
                .passwordHash("BETTER_AUTH_MANAGED")
                .role("USER")
                .build();
        log.info("Auto-provisioning profile for Better-Auth user: {}", email);
        return mapToDTO(userRepository.save(newUser));
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

