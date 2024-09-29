package com.application.elerna.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class AddCourseRequest implements Serializable {

    @NotNull(message = "ProposerId cant be null")
    private Long proposerId;

    @NotBlank(message = "Name cant be blank")
    private String name;

    @NotBlank(message = "Major cant be blank")
    private String major;

    @NotBlank(message = "Language cant be blank")
    private String language;

    @NotBlank(message = "Description cant be blank")
    private String description;

}