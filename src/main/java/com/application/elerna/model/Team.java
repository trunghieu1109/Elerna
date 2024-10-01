package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="teams")
public class Team extends AbstractEntity<Long> {

    @Column(name="name")
    private String name;

    @Column(name="is_active")
    private boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="teams_courses",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="coures_id")
    )
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="roles_users_teams",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="roles_users_teams",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private Set<User> users = new HashSet<>();

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

}
