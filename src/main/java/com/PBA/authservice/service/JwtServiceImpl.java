package com.pba.authservice.service;

import com.pba.authservice.exceptions.AuthorizationException;
import com.pba.authservice.persistance.model.ActiveUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 hours

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Override
    public String generateAccessToken(ActiveUser user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user.getUid().toString());
    }

    @Override
    public boolean isTokenValid(String token, ActiveUser activeUser) {
        return activeUser.getUid().equals(this.extractUserUid(token)) && !this.isTokenExpired(token);
    }

    @Override
    public UUID extractUserUid(String token) {
        return UUID.fromString(this.extractClaim(token, Claims::getSubject));
    }

    @Override
    public UUID extractUserUidFromHeader(String authHeader) {
        String token = this.extractTokenFromHeader(authHeader);
        return this.extractUserUid(token);
    }

    @Override
    public String extractTokenFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AuthorizationException();
        }
        return header.substring(7);
    }

    private boolean isTokenExpired(String token) {
        return this.extractExpirationDate(token).before(new Date());
    }

    private Date extractExpirationDate(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch(Exception e) {
            throw new AuthorizationException();
        }
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

    }
}
