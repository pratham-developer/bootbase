package com.pratham.bootbase.service;

import com.pratham.bootbase.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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

    public Long getUserIdFromAccessToken(String token){
        Claims claims = Jwts.parser().verifyWith(getAccessSecretKey()).build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
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
