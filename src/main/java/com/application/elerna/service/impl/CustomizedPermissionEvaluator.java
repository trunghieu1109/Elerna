package com.application.elerna.service.impl;

import com.application.elerna.model.Privilege;
import com.application.elerna.model.Role;
import com.application.elerna.utils.CustomizedGrantedAuthority;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Service
public class CustomizedPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority instanceof CustomizedGrantedAuthority) {
                CustomizedGrantedAuthority customizedGrantedAuthority = (CustomizedGrantedAuthority) authority;
                Role role = customizedGrantedAuthority.getRole();

                if ("SYSTEM_ADMIN_*_-1".equals(role.getName())) {
                    return true;
                }

                Set<Privilege> privileges = role.getPrivileges();
                for (Privilege privilege : privileges) {
                    if (privilege.getResourceType().equals(targetType)
                            && privilege.getResourceId() == targetId
                            && privilege.getActionType().equals(permission)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
