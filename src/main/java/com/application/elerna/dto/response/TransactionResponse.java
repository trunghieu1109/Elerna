package com.application.elerna.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private Long userId;

    private String cardHolder;

    private String email;

    private String phone;

    private Long courseId;

    private String description;

    private Double price;

    private String paymentMethod;

    private String cardNumber;

    private Date createAt;

    private Date updateAt;

}
