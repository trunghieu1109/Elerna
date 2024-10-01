package com.application.elerna.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountLogResponse {

    private Long messageId;

    private String messageType;

    private String message;

    private Date createdAt;

    private Date updatedAt;

}
