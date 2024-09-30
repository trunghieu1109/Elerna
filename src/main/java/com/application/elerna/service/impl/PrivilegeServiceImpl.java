package com.application.elerna.service.impl;

import com.application.elerna.model.Privilege;
import com.application.elerna.repository.PrivilegeRepository;
import com.application.elerna.service.PrivilegeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    /**
     *
     * Create privilege
     *
     * @param type String
     * @param id Long
     * @param action String
     * @param description String
     * @return Privilege
     */
    @Override
    public Privilege createPrivilege(String type, Long id, String action, String description) {
        return Privilege.builder()
                .actionType(action)
                .resourceId(id)
                .resourceType(type)
                .description(description)
                .roles(new HashSet<>())
                .build();
    }

    /**
     *
     * Save privilege to database
     *
     * @param privilege Privilege
     */
    @Override
    public void savePrivilege(Privilege privilege) {
        privilegeRepository.save(privilege);
        privilegeRepository.flush();
    }

}
