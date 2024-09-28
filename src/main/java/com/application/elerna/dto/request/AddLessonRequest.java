package com.application.elerna.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
public class AddLessonRequest implements Serializable {

    @NotBlank(message = "Lesson name cant be blank")
    private String name;

    @NotNull(message = "Course Id cant be null")
    private String courseId;

    @NotNull(message = "Lesson content cant be null")
    private MultipartFile[] contents;

}
