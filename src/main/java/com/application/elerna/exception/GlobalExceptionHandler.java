package com.application.elerna.exception;

import com.application.elerna.dto.response.ErrorResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidRequestData.class, ResourceNotFound.class, MessagingException.class, UnsupportedEncodingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidRequestData(Exception e, WebRequest webRequest) {
        log.error("Invalide request data: " + e.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.BAD_REQUEST.value())
                .cause(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .path(webRequest.getContextPath())
                .build();

    }

}
