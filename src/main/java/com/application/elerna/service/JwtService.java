package com.application.elerna.service;

import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    public String generateAccessToken(String username);

    public String generateRefreshToken(String username);

}
