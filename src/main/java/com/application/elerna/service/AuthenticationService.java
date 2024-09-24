package com.application.elerna.service;

import com.application.elerna.dto.request.ResetPasswordRequest;
import com.application.elerna.dto.request.SignInRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface AuthenticationService {

    public TokenResponse signUp(SignUpRequest request);

    public TokenResponse authenticate(SignInRequest request);

    public TokenResponse refresh(HttpServletRequest request);

    public void logout(HttpServletRequest request);

    public TokenResponse forgotPassword(String email);

    public String confirm(String resetToken);

    public String resetPassword(ResetPasswordRequest request);

}
