package com.application.elerna.service.impl;

import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.model.Token;
import com.application.elerna.service.JwtService;
import com.application.elerna.utils.TokenEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.application.elerna.utils.TokenEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expirationHour}")
    private long expirationHour;

    @Value("${jwt.expirationDay}")
    private long expirationDay;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.resetKey}")
    private String resetKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Override
    public String generateAccessToken(String username) {
        return generateAccessToken(new HashMap<String, Object>(), username);
    }

    private String generateAccessToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationHour))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(String username) {
        return generateRefreshToken(new HashMap<String, Object>(), username);
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationHour))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateResetToken(String username) {
        return generateResetToken(new HashMap<String, Object>(), username);
    }

    private String generateResetToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(getKey(RESET_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenEnum type) {
        byte[] byteKey = null;

        if (ACCESS_TOKEN.equals(type)) {
            byteKey = Decoders.BASE64.decode(secretKey);
        } else {
            if (REFRESH_TOKEN.equals(type)) {
                byteKey = Decoders.BASE64.decode(refreshKey);
            } else {
//                System.out.println(resetKey);
                byteKey = Decoders.BASE64.decode(resetKey);
            }
        }

        return Keys.hmacShaKeyFor(byteKey);
    }

    @Override
    public String extractUsername(String token, TokenEnum type) {
        String str = extractClaim(token, type, Claims::getSubject);
        return str;
    }

    @Override
    public boolean isValid(String token, TokenEnum type, UserDetails user) {

        String username = extractUsername(token, type);

        return username.equals(user.getUsername()) && !isExpired(token, type);
    }

    @Override
    public boolean isExpired(String token, TokenEnum type) {
        return extractExpiration(token, type).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String token, TokenEnum type) {
        return extractClaim(token, type, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, TokenEnum type, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenEnum type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }

}
