package com.pratham.bootbase.service;

import com.pratham.bootbase.dto.AccessTokenClaims;
import com.pratham.bootbase.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.expiry}")
    private Long accessExpiry;

    @Value("${jwt.refresh.expiry}")
    private Long refreshExpiry;

    private SecretKey getAccessSecretKey(){
        return Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getRefreshSecretKey(){
        return Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    public AccessTokenClaims getClaimsFromAccessToken(String token){
        Claims claims = Jwts.parser().verifyWith(getAccessSecretKey()).build()
                .parseSignedClaims(token)
                .getPayload();

        return AccessTokenClaims.builder()
                .id(Long.valueOf(claims.getSubject()))
                .name(claims.get("name",String.class))
                .email(claims.get("email",String.class))
                .roles(claims.get("roles", List.class))
                .build();
    }

    public Long getUserIdFromRefreshToken(String token){
        Claims claims = Jwts.parser().verifyWith(getRefreshSecretKey()).build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }

    public String generateAccessToken(AppUser authUser) {
        return Jwts.builder()
                .subject(String.valueOf(authUser.getId()))
                .claim("name",authUser.getName())
                .claim("email",authUser.getEmail())
                .claim("roles",authUser.getRoles().stream().map(role -> role.name()).collect(Collectors.toList()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiry))
                .signWith(getAccessSecretKey())
                .compact();
    }

    public String generateRefreshToken(AppUser authUser) {
        return Jwts.builder()
                .subject(String.valueOf(authUser.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiry))
                .signWith(getRefreshSecretKey())
                .compact();
    }
}
