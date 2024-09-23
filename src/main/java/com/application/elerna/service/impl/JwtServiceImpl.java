package com.application.elerna.service.impl;

import com.application.elerna.service.JwtService;
import com.application.elerna.utils.TokenEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.application.elerna.utils.TokenEnum.ACCESS_TOKEN;
import static com.application.elerna.utils.TokenEnum.REFRESH_TOKEN;

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

    private Key getKey(TokenEnum type) {
        byte[] byteKey = null;

        if (ACCESS_TOKEN.equals(type)) {
            byteKey = Decoders.BASE64.decode(secretKey);
        } else {
            byteKey = Decoders.BASE64.decode(refreshKey);
        }

        return Keys.hmacShaKeyFor(byteKey);
    }
}
