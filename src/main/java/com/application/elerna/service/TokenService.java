package com.application.elerna.service;

import com.application.elerna.model.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    public void saveToken(Token token);

    public Token getById(Long tokenId);

    public void deleteToken(Token token);

}
