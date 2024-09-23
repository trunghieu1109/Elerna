package com.application.elerna.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class UserDetail implements Serializable {

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String username;

    private String password;

    private String address;

    private String phone;

    private String email;

    private String cardNumber;

}
