package com.application.elerna.service;

import com.application.elerna.utils.TokenEnum;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
public interface JwtService {

    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    String generateSignupToken(String username);

    String generateResetToken(String username);

    String extractUsername(String token, TokenEnum type);

    boolean isValid(String token, TokenEnum type, UserDetails user);

    boolean isExpired(String token, TokenEnum type);

}
