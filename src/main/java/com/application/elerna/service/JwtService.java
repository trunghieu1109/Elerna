package com.application.elerna.service;

import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.model.Token;
import com.application.elerna.utils.TokenEnum;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
public interface JwtService {

    public String generateAccessToken(String username);

    public String generateRefreshToken(String username);

    public String generateResetToken(String username);

    public String extractUsername(String token, TokenEnum type);

    public boolean isValid(String token, TokenEnum type, UserDetails user);

    public boolean isExpired(String token, TokenEnum type);

}
