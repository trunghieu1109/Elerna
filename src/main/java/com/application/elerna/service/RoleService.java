package com.application.elerna.service;

import com.application.elerna.model.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    public Role createRole(String roleName, String resourceType, Long resouceId);

}
