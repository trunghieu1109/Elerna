package com.application.elerna.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Date timestamp;

    private int status;

    private String message;

    private String cause;

    private String path;

}
