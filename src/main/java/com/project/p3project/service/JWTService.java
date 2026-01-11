package com.project.p3project.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JWTService {
    private final Key key;
    private final long expiration;

    //JWT Constructor
    public JWTService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    //JWT Token Generator
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //JWT Validator
    public String validateTokenAndGetEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // This is the key we created earlier
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // This extracts the email we put in the "subject"
    }
}