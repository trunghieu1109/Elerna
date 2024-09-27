package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="course_requests")
public class CourseRequest extends AbstractEntity<Long> {

    @Column(name="name")
    private String name;

    @Column(name="major")
    private String major;

    @Column(name="language")
    private String language;

    @Column(name="description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="proposer_id")
    private User user;

}
