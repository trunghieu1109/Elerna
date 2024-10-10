package com.application.elerna.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class SignUpRequest implements Serializable {

    @NotBlank(message = "Username must not blank")
    private String username;

    @NotBlank(message = "Password must not blank")
    private String password;

    @NotBlank(message = "First name must not blank")
    private String firstName;

    @NotBlank(message = "Last name must not blank")
    private String lastName;

    @NotBlank(message = "System role must not blank")
    private String systemRole;

    @NotNull(message = "Date of birth must not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Email
    private String email;

    @NotNull(message = "Phone must not be null")
    private String phone;

    private String address;

    private String cardNumber;

}
