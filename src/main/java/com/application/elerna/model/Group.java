package com.application.elerna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="groups")
public class Group extends AbstractEntity<Integer> {

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "group")
    private Set<RoleUserGroup> roleUserGroups = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<GroupCourse> groupCourses = new HashSet<>();

}
