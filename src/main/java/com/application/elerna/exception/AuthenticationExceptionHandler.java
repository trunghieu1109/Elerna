package com.application.elerna.exception;

import com.application.elerna.dto.response.ErrorResponse;
import com.application.elerna.dto.response.ResponseData;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class AuthenticationExceptionHandler {

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ErrorResponse handleCredentialNotFoundException(AuthenticationCredentialsNotFoundException ex) {

        log.error("Authentication Credentials Not Found, {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .cause(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();

    }

    @ExceptionHandler(MalformedJwtException.class)
    public ErrorResponse handleMalformedJwtException(MalformedJwtException ex) {
        log.error("Malformed Jwt Exception, namely {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .cause(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(TokenMismatchException.class)
    public ErrorResponse handleTokenMismatchException(TokenMismatchException ex) {
        log.error("AccessToken's user is not matched to RefreshToken's user, access's user {}, refresh's user {}", ex.getAccessUsername(), ex.getRefreshUserName());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .cause(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();

    }

    @ExceptionHandler(SignatureException.class)
    public ErrorResponse handleSignatureException(SignatureException ex) {
        log.error("Invalid Signature {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .cause(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ErrorResponse handleUnsupportedJwtException(UnsupportedJwtException ex) {
        log.error("Unsupported Jwt Exception {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .cause(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(DisabledException.class)
    public ErrorResponse handleDisableException(AuthenticationException ex) {
        log.error("Authentication Info is disable, message: {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .cause(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleBadCredentialsException(AuthenticationException ex) {
        log.error("Bad request, message: {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.BAD_REQUEST.value())
                .cause(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ErrorResponse handleExpiredJwtException(ExpiredJwtException ex) {
        log.error("Token has been expired, message: {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .cause(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(InvalidPrincipalException.class)
    public ErrorResponse handleInvalidPrincipalException(Exception ex) {
        log.info("Authentication principal is invalid");

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .cause(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }
}
