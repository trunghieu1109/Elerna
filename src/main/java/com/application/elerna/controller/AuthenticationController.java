package com.application.elerna.controller;

import com.application.elerna.dto.request.ResetPasswordRequest;
import com.application.elerna.dto.request.SignInRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.TokenResponse;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.model.User;
import com.application.elerna.service.AuthenticationService;
import com.application.elerna.utils.ResponseExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/")
@Slf4j
@Tag(name="Authentication Management", description = "These are functions that helps server manages authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     *
     * User login.
     *
     * @param request SignInRequest
     * @return ResponseData
     */
    @Operation(summary = "Login", description = "User logins",
            responses = { @ApiResponse(responseCode = "200", description = "Login successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.loginExample))
            )})
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
    @Operation(summary = "Sign up", description = "User signs up",
            responses = { @ApiResponse(responseCode = "200", description = "Sign up successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.signupExample))
            )})
    @PostMapping("/signup")
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
    @Operation(summary = "Refresh token", description = "Admin refreshes",
            responses = { @ApiResponse(responseCode = "200", description = "Refresh token successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.refreshTokenExample))
            )})
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
    @Operation(summary = "Log out", description = "User logs out",
            responses = { @ApiResponse(responseCode = "200", description = "Log out successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.logoutExample))
            )})
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
    @Operation(summary = "Send forgot password request", description = "User sends forgot password request",
            responses = { @ApiResponse(responseCode = "200", description = "Send forgot password request successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.sendForgotPasswordRequest))
            )})
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
    @Operation(summary = "Confirm request", description = "Admin sends confirm reset request",
            responses = { @ApiResponse(responseCode = "200", description = "Confirm request successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.confirmResetExample))
            )})
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
    @Operation(summary = "Reset password request", description = "Admin and user resets passsord",
            responses = { @ApiResponse(responseCode = "200", description = "Reset password successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.resetPasswordExample))
            )})
    @PostMapping("/reset-password")
    public ResponseData<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return new ResponseData<>(HttpStatus.ACCEPTED, authenticationService.resetPassword(request));
    }

}
