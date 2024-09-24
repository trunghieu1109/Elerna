package com.application.elerna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="contents")
public class Content extends AbstractEntity<Long> {

    @Column(name="name")
    private String name;

    @Column(name="path")
    private String path;

    @OneToOne(mappedBy = "content")
    private Lesson lesson;

    @OneToOne(mappedBy = "content")
    private Assignment assignment;

    @OneToOne(mappedBy = "content")
    private Contest contest;

    @OneToOne(mappedBy = "content")
    private AssignmentSubmission assignmentSubmission;

    @OneToOne(mappedBy = "content")
    private ContestSubmission contestSubmission;


}
