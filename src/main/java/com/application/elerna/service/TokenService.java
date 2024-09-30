package com.application.elerna.service;

import com.application.elerna.model.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    void saveToken(Token token);

    Token getById(Long tokenId);

    void deleteToken(Token token);

}
