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
@Table(name="classes")
public class Class extends AbstractEntity<Integer> {

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "class_")
    private Set<RoleUserClass> roleUserClass = new HashSet<>();

}
