package com.application.elerna.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest implements Serializable {

    @NotBlank(message="name must not be blank")
    private String name;

}
