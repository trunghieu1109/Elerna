package com.application.elerna.service;

import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    public TokenResponse signUp(SignUpRequest request);

}
