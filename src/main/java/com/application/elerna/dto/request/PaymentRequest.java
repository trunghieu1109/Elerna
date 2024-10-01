package com.application.elerna.dto.request;

import lombok.Getter;

@Getter
public class PaymentRequest {

    private Long courseId;
    private String paymentMethod;
    private String description;

}
