package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
