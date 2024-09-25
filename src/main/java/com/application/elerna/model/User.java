package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User extends AbstractEntity<Long> implements UserDetails {

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name="email")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="phone")
    private String phone;

    @Column(name="address")
    private String address;

    @Column(name="card_number")
    private String cardNumber;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Token token;

    @ManyToMany(mappedBy = "users")
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name="users_courses",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="course_id")
    )
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<AssignmentSubmission> assignmentSubmissions = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ContestSubmission> contestSubmissions = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Role> roles = new HashSet<>();

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void addAssigmentSubmission(AssignmentSubmission assignmentSubmission) {
        this.assignmentSubmissions.add(assignmentSubmission);
    }

    public void addContestSubmission(ContestSubmission contestSubmission) {
        this.contestSubmissions.add(contestSubmission);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
