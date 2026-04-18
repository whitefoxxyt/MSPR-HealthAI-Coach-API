package fr.epsi.healthaicoachapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Valide les JWT émis par le service Auth (Better-Auth).
 * Les tokens sont signés avec BETTER_AUTH_SECRET en HS512.
 * Payload attendu : { sub: userId, email: userEmail, name: userName, exp: ... }
 */
@Service
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final int MIN_SECRET_BYTES = 64;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    void validateSecret() {
        int length = jwtSecret != null ? jwtSecret.getBytes(StandardCharsets.UTF_8).length : 0;
        if (length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "app.jwt.secret must be at least " + MIN_SECRET_BYTES + " bytes for HS512 (got " + length + ")");
        }
    }

    private SecretKey getSigningKey() {
        return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
    }

    public String getEmailFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get("email");
    }

    public String getNameFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get("name");
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
