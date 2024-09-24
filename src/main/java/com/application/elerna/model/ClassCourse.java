package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="classes_courses")
public class ClassCourse extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="class_id")
    private Class class_;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    private Course course;

}
