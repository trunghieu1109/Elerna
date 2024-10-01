package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
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
    private Time duration;

    @Column(name="rating")
    private Double rating;

    @Column(name="language")
    private String language;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private Double price;

    @Column(name="status")
    private boolean status;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Lesson> lessons = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Contest> contests = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<AssignmentSubmission> assignmentSubmissions = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<ContestSubmission> contestSubmissions = new HashSet<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void addContest(Contest contest) {
        contests.add(contest);
    }

    public void addAssignmentSubmission(AssignmentSubmission assignmentSubmission) {
        assignmentSubmissions.add(assignmentSubmission);
    }

    public void addContestSubmission(ContestSubmission contestSubmission) {
        contestSubmissions.add(contestSubmission);
    }

}
