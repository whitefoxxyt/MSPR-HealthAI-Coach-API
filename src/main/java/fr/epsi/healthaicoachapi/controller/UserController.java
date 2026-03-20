package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.dto.UserDTO;
import fr.epsi.healthaicoachapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "API de gestion des utilisateurs")
@SecurityRequirement(name = "bearer-jwt")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Récupère le profil de l'utilisateur connecté")
    public ResponseEntity<UserDTO> getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Récupère les informations d'un utilisateur par ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Met à jour le profil de l'utilisateur")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        UserDTO updated = userService.updateUserProfile(userId, userDTO);
        log.info("User {} profile updated", userId);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/me/activity")
    @Operation(summary = "Met à jour la dernière activité de l'utilisateur")
    public ResponseEntity<UserDTO> updateLastActivity() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO updated = userService.updateLastActivity(email);
        return ResponseEntity.ok(updated);
    }
}

