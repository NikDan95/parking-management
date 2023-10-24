package com.example.parkingmanagement.security;


import com.example.parkingmanagement.exception.AuthorizationFailureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${com.example.jwtSecret}")
    private String jwtSecret;

    @Value("${com.example.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .claim("username", userPrincipal.getUsername())
                .claim("roles", userPrincipal.getAuthorities())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }

    public void validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parse(authToken);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new AuthorizationFailureException();
        }
    }
}
