package com.application.elerna.model;

import jakarta.persistence.*;
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

    @OneToOne(mappedBy = "content", fetch = FetchType.EAGER)
    private Lesson lesson;

    @OneToOne(mappedBy = "content", fetch = FetchType.EAGER)
    private Assignment assignment;

    @OneToOne(mappedBy = "content", fetch = FetchType.EAGER)
    private Contest contest;

    @OneToOne(mappedBy = "content", fetch = FetchType.EAGER)
    private AssignmentSubmission assignmentSubmission;

    @OneToOne(mappedBy = "content", fetch = FetchType.EAGER)
    private ContestSubmission contestSubmission;


}
