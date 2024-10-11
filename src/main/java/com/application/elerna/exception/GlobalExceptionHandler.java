package com.application.elerna.exception;

import com.application.elerna.dto.response.ErrorResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidRequestData.class, ResourceNotFound.class, MessagingException.class, UnsupportedEncodingException.class})
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleInvalidRequestData(Exception e, WebRequest webRequest) {
        log.error("Invalid request data: {}", e.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.BAD_REQUEST.value())
                .cause(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .path(webRequest.getContextPath())
                .build();

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        log.error("Invalid Method Arguments, cause: {}", errors.toString());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .cause(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
                .message(errors.toString())
                .path("")
                .build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleNoSuchException(Exception ex) {
        log.error("No such item in collections, message: {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.NOT_FOUND.value())
                .cause(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(InvalidAccountRemaining.class)
    public ErrorResponse handleInvalidAccountRemaining(Exception ex) {
        log.error("Account remaining is not enough for new transaction, message: {}", ex.getMessage());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.BAD_REQUEST.value())
                .cause(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

//    @ExceptionHandler(MessagingException.class)
//    public ErrorResponse handleMessageException(Exception ex) {
//        log.error("Message exception, detail: {}", ex.getMessage());
//
//        return ErrorResponse.builder()
//                .timestamp(new Date(System.currentTimeMillis()))
//                .status(HttpStatus.BAD_REQUEST.value())
//                .cause(HttpStatus.BAD_REQUEST.getReasonPhrase())
//                .message(ex.getMessage())
//                .path("")
//                .build();
//    }



}
