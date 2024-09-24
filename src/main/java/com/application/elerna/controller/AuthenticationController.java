package com.application.elerna.controller;

import com.application.elerna.dto.request.ResetPasswordRequest;
import com.application.elerna.dto.request.SignInRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.TokenResponse;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.model.User;
import com.application.elerna.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseData<TokenResponse> logIn(@Valid @RequestBody SignInRequest request) {

        log.info("User login");

        return new ResponseData<>(HttpStatus.ACCEPTED, "User login. Get access token successully", authenticationService.authenticate(request));
    }

    @PostMapping("/signup")
    public ResponseData<TokenResponse> signUp(@Valid @RequestBody SignUpRequest request) {

        log.info("Register new account");

        return new ResponseData<>(HttpStatus.CREATED, "Account is created", authenticationService.signUp(request));
    }

    @PostMapping("/refresh")
    public ResponseData<TokenResponse> refresh(HttpServletRequest request) {
        log.info("Refresh access token");
        return new ResponseData<>(HttpStatus.CREATED, "Access Token is refreshed", authenticationService.refresh(request));
    }

    @DeleteMapping("/logout")
    public String logout(HttpServletRequest request) {

        authenticationService.logout(request);

        return "Logout successfully";
    }

    @PostMapping("/forgot-password")
    public ResponseData<TokenResponse> forgotPassword(@RequestBody @Email @NotBlank String email) {
        try {
            return new ResponseData<>(HttpStatus.ACCEPTED, "Forgot password request accepted", authenticationService.forgotPassword(email));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST, "Can't send email");
        }
    }

    @PostMapping("/confirm-reset")
    public ResponseData<String> confirm(@RequestBody String resetToken) {
        return new ResponseData<>(HttpStatus.ACCEPTED, authenticationService.confirm(resetToken));
    }

    @PostMapping("/reset-password")
    public ResponseData<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return new ResponseData<>(HttpStatus.ACCEPTED, authenticationService.resetPassword(request));
    }

}
