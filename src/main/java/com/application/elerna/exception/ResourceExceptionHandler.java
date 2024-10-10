package com.application.elerna.exception;

import com.application.elerna.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistedException.class)
    public ErrorResponse handleExistedResourceException(ResourceException ex) {

        log.error("{} has already existed, {} id: {}", ex.getResourceType(), ex.getResourceType(), ex.getResourceId());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.CONFLICT.value())
                .cause(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(ResourceNotReady.class)
    public ErrorResponse handleResourceNotReadyException(ResourceException ex) {
        log.error("{} is not ready, {} id: {}", ex.getResourceType(), ex.getResourceType(), ex.getResourceId());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.NOT_FOUND.value())
                .cause(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ErrorResponse handleResourceNotFound(ResourceException ex) {
        log.error("{} is not found, {} {}", ex.getResourceType(), ex.getResourceType(), ex.getResourceFeature());

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.NOT_FOUND.value())
                .cause(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path("")
                .build();

    }

}
