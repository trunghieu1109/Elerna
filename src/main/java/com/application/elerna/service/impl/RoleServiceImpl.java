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

    @Override
    public Role createRole(String roleName, String resourceType, Long resouceId) {
        return Role.builder()
                .name(roleName.toUpperCase() + "_" + resourceType.toUpperCase() + "_" + resouceId.toString())
                .description(roleName + " of " + resourceType + ", id = " + resouceId)
                .privileges(new HashSet<>())
                .teams(new HashSet<>())
                .users(new HashSet<>())
                .build();
    }
}
