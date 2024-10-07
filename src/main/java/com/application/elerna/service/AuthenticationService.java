package com.application.elerna.service;

import com.application.elerna.dto.request.ResetPasswordRequest;
import com.application.elerna.dto.request.SignInRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.TokenResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface AuthenticationService {

    TokenResponse signUp(SignUpRequest request);

    TokenResponse authenticate(SignInRequest request);

    TokenResponse refresh(HttpServletRequest request);

    void logout(HttpServletRequest request);

    String forgotPassword(String email) throws MessagingException, UnsupportedEncodingException;

    String confirm(String resetToken);

    String resetPassword(ResetPasswordRequest request);

}
