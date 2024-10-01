package com.application.elerna.model;

import com.application.elerna.utils.CustomizedGrantedAuthority;
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

    @Column(name="system_role")
    private String systemRole;

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

    @Column(name="is_active")
    private boolean isActive;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Token token;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="users_courses",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="course_id")
    )
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<AssignmentSubmission> assignmentSubmissions = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<ContestSubmission> contestSubmissions = new HashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<CourseRequest> courseRequests = new HashSet<>();

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

    public void addAssignmentSubmission(AssignmentSubmission assignmentSubmission) {
        this.assignmentSubmissions.add(assignmentSubmission);
    }

    public void addContestSubmission(ContestSubmission contestSubmission) {
        this.contestSubmissions.add(contestSubmission);
    }

    public void addCourseRequest(CourseRequest courseRequest) {
        this.courseRequests.add(courseRequest);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<CustomizedGrantedAuthority> authoritySet = new HashSet<>();

        for (Role role : this.roles) {
            authoritySet.add(new CustomizedGrantedAuthority(role));
        }

        for (Team team : this.teams) {
            for (Role role : team.getRoles()) {
                authoritySet.add(new CustomizedGrantedAuthority(role));
            }
        }

        return authoritySet.stream().toList();


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
