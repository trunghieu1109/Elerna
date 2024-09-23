package com.application.elerna.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseData<T> {
    private final int status;
    private final String message;
    private T data;

    public ResponseData(HttpStatus code, String message) {
        this.status = code.value();
        this.message = message;
    }

    public ResponseData(HttpStatus code, String message, T data) {
        this.status = code.value();
        this.message = message;
        this.data = data;
    }

}