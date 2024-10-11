package com.application.elerna.service.impl;

import com.application.elerna.model.User;
import com.application.elerna.service.JwtService;
import com.application.elerna.utils.TokenEnum;
import io.jsonwebtoken.*;
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

    @Value("${jwt.expirationMinute}")
    private long expirationMinute;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.resetKey}")
    private String resetKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.signupKey}")
    private String signupKey;

    /**
     *
     * Generate access token
     *
     * @param username String
     * @return String
     */
    @Override
    public String generateAccessToken(String username) {
        return generateAccessToken(new HashMap<String, Object>(), username);
    }

    /**
     *
     * Generate access token specifically
     *
     * @param claims Map<String, Object>
     * @param username String
     * @return String
     */
    private String generateAccessToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationHour))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     *
     * Generate refresh token
     *
     * @param username String
     * @return String
     */
    @Override
    public String generateRefreshToken(String username) {
        return generateRefreshToken(new HashMap<String, Object>(), username);
    }

    /**
     *
     * Generate refresh token specifically
     *
     * @param claims Map<String, Object>
     * @param username String
     * @return String
     */
    private String generateRefreshToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationDay))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     *
     * Generate reset token
     *
     * @param username String
     * @return String
     */
    @Override
    public String generateResetToken(String username) {
        return generateResetToken(new HashMap<String, Object>(), username);
    }

    /**
     *
     * Generate reset token specifically
     *
     * @param claims Map<String, Object>
     * @param username String
     * @return String
     */
    private String generateResetToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(getKey(RESET_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     *
     * Generate refresh token
     *
     * @param username String
     * @return String
     */
    @Override
    public String generateSignupToken(String username) {
        return generateSignupToken(new HashMap<String, Object>(), username);
    }

    /**
     *
     * Generate refresh token specifically
     *
     * @param claims Map<String, Object>
     * @param username String
     * @return String
     */
    private String generateSignupToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expirationMinute))
                .signWith(getKey(SIGNUP_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     *
     * Get key from secretKey
     *
     * @param type TokenEnum
     * @return Key
     */
    private Key getKey(TokenEnum type) {
        byte[] byteKey;

        if (ACCESS_TOKEN.equals(type)) {
            byteKey = Decoders.BASE64.decode(secretKey);
        } else {
            if (REFRESH_TOKEN.equals(type)) {
                byteKey = Decoders.BASE64.decode(refreshKey);
            } if (RESET_TOKEN.equals(type)) {
                byteKey = Decoders.BASE64.decode(resetKey);
            } else {
                byteKey = Decoders.BASE64.decode(signupKey);
            }
        }

        return Keys.hmacShaKeyFor(byteKey);
    }

    /**
     *
     * Extract username from token
     *
     * @param token String
     * @param type TokenEnum
     * @return String
     */
    @Override
    public String extractUsername(String token, TokenEnum type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    /**
     *
     * Check toke whether is valid or not
     *
     * @param token String
     * @param type TokenEnum
     * @param user UserDetails
     * @return boolean
     */
    @Override
    public boolean isValid(String token, TokenEnum type, UserDetails user) {

        String username = extractUsername(token, type);

        if (user instanceof User) {
            if (!((User) user).isActive()) {
                return false;
            }
        }

//        log.info(username + ", " + user.getUsername());

        if (!username.equals(user.getUsername())) {
            throw new MalformedJwtException("Token not match to " + user.getUsername());
        }

        if (isExpired(token, type)) {
            throw new ExpiredJwtException(extractAllHeader(token, type), extractAllClaims(token, type), type.toString() + " has been expired");
        }

        return username.equals(user.getUsername()) && !isExpired(token, type);
    }

    /**
     *
     * Check whether token is expired or not
     *
     * @param token String
     * @param type TokenEnum
     * @return boolean
     */
    @Override
    public boolean isExpired(String token, TokenEnum type) {
        return extractExpiration(token, type).before(new Date(System.currentTimeMillis()));
    }

    /**
     *
     * Get token expiration
     *
     * @param token String
     * @param type TokenEnum
     * @return Date
     */
    private Date extractExpiration(String token, TokenEnum type) {
        return extractClaim(token, type, Claims::getExpiration);
    }

    /**
     *
     * Extract information from claims
     *
     * @param token String
     * @param type TokenEnum
     * @param claimResolver Function<Claims, T>
     * @return T
     * @param <T> T
     */
    private <T> T extractClaim(String token, TokenEnum type, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimResolver.apply(claims);
    }

    /**
     *
     * Extract claims from token
     *
     * @param token String
     * @param type TokenEnum
     * @return Claims
     */
    private Claims extractAllClaims(String token, TokenEnum type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }

    private JwsHeader extractAllHeader(String token, TokenEnum type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getHeader();
    }

}
