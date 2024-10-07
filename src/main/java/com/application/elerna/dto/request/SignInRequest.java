package com.application.elerna.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class SignInRequest implements Serializable {

    @NotBlank(message = "Username must not a blank")
    private String username;

    @NotBlank(message = "Username must not a blank")
    private String password;

    private String uuid;

}
