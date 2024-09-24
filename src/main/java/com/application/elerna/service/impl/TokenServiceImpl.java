package com.application.elerna.service.impl;

import com.application.elerna.model.Token;
import com.application.elerna.repository.TokenRepository;
import com.application.elerna.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public Token getById(Long tokenId) {

        Optional<Token> token = tokenRepository.findById(tokenId);

        return token.get();
    }

    @Override
    public void deleteToken(Token token) {
        token.setStatus(false);
        saveToken(token);
    }

}
