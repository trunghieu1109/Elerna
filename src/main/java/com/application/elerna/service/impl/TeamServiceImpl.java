package com.application.elerna.service.impl;

import com.application.elerna.dto.request.TeamRequest;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.model.Privilege;
import com.application.elerna.model.Role;
import com.application.elerna.model.Team;
import com.application.elerna.model.User;
import com.application.elerna.repository.PrivilegeRepository;
import com.application.elerna.repository.RoleRepository;
import com.application.elerna.repository.TeamRepository;
import com.application.elerna.repository.UserRepository;
import com.application.elerna.service.RoleService;
import com.application.elerna.service.TeamService;
import com.application.elerna.service.PrivilegeService;
import com.application.elerna.utils.TokenEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final JwtServiceImpl jwtService;
    private final UserRepository userRepository;
    private final PrivilegeService privilegeService;
    private final RoleService roleService;
    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;

    @Override
    public String createTeam(HttpServletRequest request, TeamRequest teamRequest) {

        // extract group name
        String teamName = teamRequest.getName();

        // check if existed team
        Optional<com.application.elerna.model.Team> foundTeam = teamRepository.findByName(teamName);

        if (!foundTeam.isEmpty()) {
            throw new InvalidRequestData("Team name has been existed in database. Please try other team names");
        }

        // create new team
        Team team = Team.builder()
                .name(teamRequest.getName())
                .users(new HashSet<>())
                .roles(new HashSet<>())
                .courses(new HashSet<>())
                .build();

        // get requesting user

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new InvalidRequestData("Invalid access token");
        }

        String username = jwtService.extractUsername(request.getHeader("Authorization").substring("Bearer ".length()), TokenEnum.ACCESS_TOKEN);
        User user = userRepository.findByUsername(username).get();

        saveTeam(team, user);

        return "Create group successfully";
    }

    public void saveTeam(Team team, User user) {
        // add user to team as the team leader
        user.addTeam(team);
        team.addUser(user);

        List<Team> teams = teamRepository.findAll();
        Long maxId = teams.isEmpty() ? 0 : teams.get(Math.max(teams.size() - 1, 0)).getId();

        userRepository.save(user);
        teamRepository.save(team);

        // create roles and privileges relating to team
        Privilege priView = privilegeService.createPrivilege("team", (long) (maxId + 1), "view", "View team, id = " + (maxId + 1));
        Privilege priUpdate = privilegeService.createPrivilege("team", (long) (maxId + 1), "update", "Update team, id = " + (maxId + 1));
        Privilege priDelete = privilegeService.createPrivilege("team", (long) (maxId + 1), "delete", "Delete team, id = " + (maxId + 1));

        Role roleAdmin = roleService.createRole("ADMIN", "team", (long) (maxId + 1));
        Role roleViewer = roleService.createRole("VIEWER", "team", (long) (maxId + 1));
        Role roleEditor = roleService.createRole("EDITOR", "team", (long) (maxId + 1));

        roleAdmin.addPrivilege(priView);
        roleAdmin.addPrivilege(priUpdate);
        roleAdmin.addPrivilege(priDelete);
        roleViewer.addPrivilege(priView);
        roleEditor.addPrivilege(priDelete);
        roleEditor.addPrivilege(priUpdate);

        priView.addRole(roleAdmin);
        priView.addRole(roleViewer);

        priDelete.addRole(roleAdmin);
        priDelete.addRole(roleEditor);

        priUpdate.addRole(roleAdmin);
        priUpdate.addRole(roleEditor);

        user.addRole(roleAdmin);
        roleAdmin.addUser(user);

        // save to repository
        privilegeRepository.save(priView);
        privilegeRepository.save(priDelete);
        privilegeRepository.save(priUpdate);

        roleRepository.save(roleEditor);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleViewer);

        userRepository.save(user);
    }
}
