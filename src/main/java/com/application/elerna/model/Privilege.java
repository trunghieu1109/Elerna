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
@Table(name="privileges")
public class Privilege extends AbstractEntity<Long> {

    @Column(name="resource_id")
    private Long resourceId;

    @Column(name="action_type")
    private String actionType;

    @Column(name="description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="resource_type")
    private ResourceType resourceType;

    @OneToMany(mappedBy = "privilege")
    private Set<RolePrivilege> rolePrivileges = new HashSet<>();

}
