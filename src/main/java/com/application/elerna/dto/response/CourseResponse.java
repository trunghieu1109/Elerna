package com.application.elerna.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@Setter
public class CourseResponse implements Serializable {

    private Long id;

    private String name;

    private String major;

    private Time duration;

    private Double rating;

    private String language;

    private String description;

    private Double price;

    private List<String> lessons = new ArrayList<>();

    private List<String> assignments = new ArrayList<>();

    private List<String> contests = new ArrayList<>();

    public void addLessonName(String name) {
        this.lessons.add(name);
    }

    public void addAssignmentName(String name) {
        this.assignments.add(name);
    }

    public void addContestName(String name) {
        this.contests.add(name);
    }

}
