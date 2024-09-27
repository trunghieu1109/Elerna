package com.application.elerna.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.sql.Time;

@Getter
public class UpdateCourseRequest implements Serializable {

    @NotNull(message="CourseId cant be null")
    private Long courseId;

    @NotBlank(message = "Name cant be blank")
    private String name;

    @NotBlank(message = "Major cant be blank")
    private String major;

    @NotBlank(message = "Language cant be blank")
    private String language;

    @NotBlank(message = "Description cant be blank")
    private String description;

    @NotNull(message = "Duration cant be null")
    private Time duration;

    @NotNull(message = "Rating cant be null")
    private Double rating;

    @NotNull(message = "Price cant be null")
    private Double price;



}
