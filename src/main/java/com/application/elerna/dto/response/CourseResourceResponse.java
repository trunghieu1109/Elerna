package com.application.elerna.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@Builder
public class CourseResourceResponse implements Serializable {

    private Long resourceId;

    private String resourceType;

    private String name;

    private Long courseId;

    private Date startDate;

    private Date endDate;

    private Date duration;

    private String content;

}
