package com.application.elerna.dto.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@Slf4j
public class ResetPasswordRequest implements Serializable {
    private String password;
    private String confirmPassword;
    private String resetToken;
}
