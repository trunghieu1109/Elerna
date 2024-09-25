package com.application.elerna.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@NoArgsConstructor
public class UserDetailRequest implements Serializable {

    private Long userId;

    @NotBlank(message = "First name cant not be blank")
    private String firstName;

    @NotBlank(message = "Last name cant not be blank")
    private String lastName;

    @NotNull(message = "Date of birth must not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;

    @NotBlank(message = "Address is not blank")
    private String address;

    @NotBlank(message = "Phone is not blank")
    private String phone;

    @Email
    private String email;
    
    private String cardNumber;

}