package com.application.elerna.service.impl;

import com.application.elerna.model.Role;
import com.application.elerna.repository.RoleRepository;
import com.application.elerna.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /**
     *
     * Create role
     *
     * @param roleName String
     * @param resourceType String
     * @param resourceId Long
     * @return Role
     */
    @Override
    public Role createRole(String roleName, String resourceType, Long resourceId) {
        return Role.builder()
                .name(roleName.toUpperCase() + "_" + resourceType.toUpperCase() + "_" + resourceId.toString())
                .description(roleName + " of " + resourceType + ", id = " + resourceId)
                .privileges(new HashSet<>())
                .teams(new HashSet<>())
                .users(new HashSet<>())
                .build();
    }

    /**
     *
     * Get role by name
     *
     * @param name String
     * @return Role
     */
    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     *
     * Save role to database
     *
     * @param role Role
     */
    public void saveRole(Role role) {
        roleRepository.save(role);
        roleRepository.flush();
    }


}
