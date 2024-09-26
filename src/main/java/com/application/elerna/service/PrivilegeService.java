package com.application.elerna.service;

import com.application.elerna.model.Privilege;
import org.springframework.stereotype.Service;

@Service
public interface PrivilegeService {

    Privilege createPrivilege(String type, Long id, String action, String description);

    void savePrivilege(Privilege privilege);

}
