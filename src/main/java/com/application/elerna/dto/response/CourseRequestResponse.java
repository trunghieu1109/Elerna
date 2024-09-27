package com.application.elerna.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestResponse {

    private Long id;

    private Long proposerId;

    private String name;

    private String major;

    private String language;

    private String description;

}
