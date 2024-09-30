package com.application.elerna.service;

import com.application.elerna.model.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    Role createRole(String roleName, String resourceType, Long resourceId);

    Role getRoleByName(String name);

    void saveRole(Role role);

}
