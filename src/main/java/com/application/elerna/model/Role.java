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
@Table(name="roles")
public class Role extends AbstractEntity<Long> {

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<Team> teams = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="roles_privileges",
            joinColumns = @JoinColumn(name="role_id"),
            inverseJoinColumns = @JoinColumn(name="privilege_id")
    )
    private Set<Privilege> privileges = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="roles_users_teams",
            joinColumns = @JoinColumn(name="role_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private Set<User> users = new HashSet<>();

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addPrivilege(Privilege privilege) {
        this.privileges.add(privilege);
    }

}
