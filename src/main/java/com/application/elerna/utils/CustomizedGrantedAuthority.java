package com.application.elerna.utils;

import com.application.elerna.model.Privilege;
import com.application.elerna.model.Role;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CustomizedGrantedAuthority implements GrantedAuthority {

    private Role role;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.role.getName();
    }

    public Role getRole() {
        return role;
    }
}
