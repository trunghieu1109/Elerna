package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="assignments")
public class Assignment extends AbstractEntity<Long> {

    @Column(name="name")
    private String name;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    private Course course;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="content_id")
    private Content content;

    @OneToMany(mappedBy = "assignment", fetch = FetchType.EAGER)
    private Set<AssignmentSubmission> assignmentSubmissions = new HashSet<>();

    public void addAssignmentSubmission(AssignmentSubmission assignmentSubmission) {
        assignmentSubmissions.add(assignmentSubmission);
    }

}
