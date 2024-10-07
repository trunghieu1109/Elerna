package com.application.elerna.service.impl;

import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.Token;
import com.application.elerna.repository.TokenRepository;
import com.application.elerna.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    /**
     *
     * Save token
     *
     * @param token Token
     */
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    /**
     *
     * Get token by id
     *
     * @param tokenId Long
     * @return Token
     */
    @Override
    @Transactional(readOnly = true)
    public Token getById(Long tokenId) {

        Optional<Token> token = tokenRepository.findById(tokenId);

        if (token.isPresent())
            return token.get();
        else {
            throw new ResourceNotFound("Cant not find suitable token");
        }
    }

    /**
     *
     * Delete token
     *
     * @param token Token
     */
    @Override
    public void deleteToken(Token token) {
        token.setAccStatus(false);
        token.setRefStatus(false);
        saveToken(token);
    }

    @Override
    public Token getByUuid(String uuid) {
        return tokenRepository.findByUuid(uuid);
    }

}
