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

    @OneToMany(mappedBy = "role")
    private Set<RoleUserGroup> roleUserGroups = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<RolePrivilege> rolePrivileges = new HashSet<>();

}
