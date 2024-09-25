package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="courses")
public class Course extends AbstractEntity<Long> {

    @Column(name="name")
    private String name;

    @Column(name="major")
    private String major;

    @Column(name="duration")
    private Date duration;

    @Column(name="rating")
    private Double rating;

    @Column(name="language")
    private String language;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private Double price;

    @OneToMany(mappedBy = "course")
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<Lesson> lessons = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<Contest> contests = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<AssignmentSubmission> assignmentSubmissions = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<ContestSubmission> contestSubmissions = new HashSet<>();

}
