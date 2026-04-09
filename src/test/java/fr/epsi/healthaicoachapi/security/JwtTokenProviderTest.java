package fr.epsi.healthaicoachapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    // Clé de 32 caractères minimum pour HS256
    private static final String SECRET = "my-super-secret-key-for-testing-1234";

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", SECRET);
    }

    private String generateToken(String subject, long expirationMs) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    @Test
    @DisplayName("isTokenValid - Should return true for a valid token")
    void testIsTokenValid_ValidToken() {
        String token = generateToken("user-auth-123", 60_000);

        assertTrue(jwtTokenProvider.isTokenValid(token));
    }

    @Test
    @DisplayName("isTokenValid - Should return false for an expired token")
    void testIsTokenValid_ExpiredToken() {
        String token = generateToken("user-auth-123", -1000); // déjà expiré

        assertFalse(jwtTokenProvider.isTokenValid(token));
    }

    @Test
    @DisplayName("isTokenValid - Should return false for a malformed token")
    void testIsTokenValid_MalformedToken() {
        assertFalse(jwtTokenProvider.isTokenValid("this.is.not.a.valid.jwt"));
    }

    @Test
    @DisplayName("isTokenValid - Should return false for an empty token")
    void testIsTokenValid_EmptyToken() {
        assertFalse(jwtTokenProvider.isTokenValid(""));
    }

    @Test
    @DisplayName("isTokenValid - Should return false for a token signed with a different key")
    void testIsTokenValid_WrongSigningKey() {
        SecretKey wrongKey = Keys.hmacShaKeyFor("completely-different-secret-key-5678".getBytes());
        String token = Jwts.builder()
                .subject("user-auth-123")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(wrongKey)
                .compact();

        assertFalse(jwtTokenProvider.isTokenValid(token));
    }

    @Test
    @DisplayName("getSubFromToken - Should return the subject from a valid token")
    void testGetSubFromToken_ReturnsSubject() {
        String token = generateToken("user-auth-abc", 60_000);

        String subject = jwtTokenProvider.getSubFromToken(token);

        assertEquals("user-auth-abc", subject);
    }

    @Test
    @DisplayName("getSubFromToken - Should throw exception for an invalid token")
    void testGetSubFromToken_InvalidToken() {
        assertThrows(Exception.class, () -> jwtTokenProvider.getSubFromToken("invalid.token.here"));
    }
}
