package com.application.elerna.controller;

import com.application.elerna.dto.request.SignInRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.TokenResponse;
import com.application.elerna.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseData<TokenResponse> logIn(@Valid @RequestBody SignInRequest request) {

        log.info("User login");

        return new ResponseData<>(HttpStatus.ACCEPTED, "User login. Get access token successully");
    }

    @PostMapping("/signup")
    public ResponseData<TokenResponse> signUp(@Valid @RequestBody SignUpRequest request) {

        log.info("Register new account");

        return new ResponseData<>(HttpStatus.CREATED, "Account is created", authenticationService.signUp(request));
    }

}
