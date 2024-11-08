package com.example.Tokk.Components;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private SecretKey secretKey;

    public JwtTokenProvider() {
        // Генерация безопасного ключа для HS256
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        log.info("SecretKey: {}", secretKey.toString());
    }
    private final long validityInMilliseconds = 3600000; // 1 час

    public String createToken(String email, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", "ROLE_" + role)  // Обратите внимание на добавление ROLE_
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token", e);
            return false;
        }
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).
                parseClaimsJws(token).
                getBody().getSubject();
    }
}
