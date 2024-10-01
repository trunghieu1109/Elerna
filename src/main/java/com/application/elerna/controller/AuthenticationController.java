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

    /**
     *
     * User login.
     *
     * @param request SignInRequest
     * @return ResponseData
     */
    @PostMapping("/login")
    public ResponseData<TokenResponse> logIn(@Valid @RequestBody SignInRequest request) {

        log.info("User login");

        return new ResponseData<>(HttpStatus.ACCEPTED, "User login. Get access token successully", authenticationService.authenticate(request));
    }

    /**
     *
     * User signup.
     *
     * @param request SignUpRequest
     * @return ResponseData
     */
    @PostMapping("/signup")
    @ResponseBody
    public ResponseData<TokenResponse> signUp(@Valid @RequestBody SignUpRequest request) {

        log.info("Register new account");

        return new ResponseData<>(HttpStatus.CREATED, "Account is created", authenticationService.signUp(request));
    }

    /**
     *
     * Refresh access token
     *
     * @param request HttpServletRequest
     * @return ResponseData
     */
    @PostMapping("/refresh")
    public ResponseData<TokenResponse> refresh(HttpServletRequest request) {
        log.info("Refresh access token");
        return new ResponseData<>(HttpStatus.CREATED, "Access Token is refreshed", authenticationService.refresh(request));
    }

    /**
     *
     * User logout
     *
     * @param request HttpServletRequest
     * @return String
     */
    @DeleteMapping("/logout")
    public String logout(HttpServletRequest request) {

        authenticationService.logout(request);

        return "Logout successfully";
    }

    /**
     *
     * User send email to request resetting
     * password and then server send confirmation email
     *
     * @param email String
     * @return ResponseData
     */
    @PostMapping("/forgot-password")
    public ResponseData<TokenResponse> forgotPassword(@RequestBody @Email @NotBlank String email) {
        try {
            return new ResponseData<>(HttpStatus.ACCEPTED, "Forgot password request accepted", authenticationService.forgotPassword(email));
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     *
     * Server check reset token with database
     *
     * @param resetToken String
     * @return ResponseData
     */
    @PostMapping("/confirm-reset")
    public ResponseData<String> confirm(@RequestBody String resetToken) {
        return new ResponseData<>(HttpStatus.ACCEPTED, authenticationService.confirm(resetToken));
    }

    /**
     *
     * Server compare password with
     * confirmation and change password.
     *
     * @param request ResetPasswordRequest
     * @return ResponseData
     */
    @PostMapping("/reset-password")
    public ResponseData<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return new ResponseData<>(HttpStatus.ACCEPTED, authenticationService.resetPassword(request));
    }

}
