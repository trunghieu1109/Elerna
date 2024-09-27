package com.application.elerna.service.impl;

import com.application.elerna.dto.request.TeamRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.TeamResponse;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.Privilege;
import com.application.elerna.model.Role;
import com.application.elerna.model.Team;
import com.application.elerna.model.User;
import com.application.elerna.repository.*;
import com.application.elerna.service.RoleService;
import com.application.elerna.service.TeamService;
import com.application.elerna.service.PrivilegeService;
import com.application.elerna.utils.TokenEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final UtilsRepository utilsRepository;

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
                .isActive(true)
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

    @Override
    public TeamResponse getTeamDetails(Long teamId) {

        Optional<Team> team = teamRepository.findById(teamId);

        if (!team.get().isActive()) {
            throw new InvalidRequestData("Team is inactive");
        }

        return TeamResponse.builder()
                .name(team.get().getName())
                .createdAt(team.get().getCreatedAt())
                .updatedAt(team.get().getUpdatedAt())
                .build();
    }

    @Override
    public String deleteTeam(Long teamId) {

        var team = teamRepository.findById(teamId);

        if (!team.get().isActive()) {
            throw new InvalidRequestData("Team is inactive");
        }

        team.get().setActive(false);
        team.get().setName(team.get().getName() + "-Deleted");

        teamRepository.save(team.get());

        return "Delete team successfully";
    }

    @Override
    public PageResponse<?> getAllTeam(Integer pageNo, Integer pageSize, String searchBy) {
        Page<Team> teams = utilsRepository.findTeamByName(pageNo, pageSize, searchBy);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .totalPages(teams.getTotalPages())
                .pageSize(pageSize)
                .pageNo(pageNo)
                .data(teams.stream().map(team ->
                        TeamResponse.builder()
                                .name(team.getName())
                                .createdAt(team.getCreatedAt())
                                .updatedAt(team.getUpdatedAt())
                                .build()))
                .build();

    }

    @Override
    public PageResponse<?> getJoinedTeam(Long userId, Integer pageNo, Integer pageSize, String searchBy) {
        var user = userRepository.findById(userId);

        if (!user.get().isActive()) {
            throw new InvalidRequestData("User is inactive");
        }

        List<Team> teams = new ArrayList<>();

        for (Team team : user.get().getTeams()) {
            if (team.getName().toLowerCase().contains(searchBy.toLowerCase()) && team.isActive()) {
                teams.add(team);
            }
        }

        int size_ = teams.size();

        Page<Team> page = new PageImpl(teams.subList(Math.min(pageNo * pageSize, teams.size()), Math.min((pageNo + 1) * pageSize, teams.size())),
                PageRequest.of(pageNo, pageSize),
                (int) Math.ceil((double)size_ / pageSize));

//        log.info("" +  + " " + size_ + " " + pageSize);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil((double)size_ / pageSize))
                .data(page.stream().map(team ->
                        TeamResponse.builder()
                                .name(team.getName())
                                .createdAt(team.getCreatedAt())
                                .updatedAt(team.getUpdatedAt())
                        .build()))
                .build();
    }

    @Override
    public String joinTeam(Long userId, Long teamId) {

        var user = userRepository.findById(userId);

        if (user.isEmpty() || !user.get().isActive()) {
            throw new InvalidRequestData("User is not ready (inactive or not existed)");
        }

        var team = teamRepository.findById(teamId);

        if (team.isEmpty() || !team.get().isActive()) {
            throw new InvalidRequestData("Team is not ready (inactive or not existed)");
        }

        Team team_ = team.get();
        User user_ = user.get();

        team_.addUser(user_);
        user_.addTeam(team_);

        teamRepository.save(team_);
        userRepository.save(user_);

        for (Role role : team_.getRoles()) {
            if (role.getName().contains("VIEWER")) {
                user_.addRole(role);
                role.addUser(user_);

                roleRepository.save(role);
                userRepository.save(user_);
                log.info("add viewer role to user");
                break;
            }
        }

        return "User " + userId + " joins team successfully";
    }

    @Override
    public String outTeam(Long userId, Long teamId) {

        var currentUser = userRepository.findById(userId);

        if (currentUser == null || currentUser.isEmpty() || !currentUser.get().isActive()) {
            throw new InvalidRequestData("UserId is invalid, not existed or inactive");
        }

        var currentTeam = teamRepository.findById(teamId);

        if (currentTeam == null || currentTeam.isEmpty() || !currentTeam.get().isActive()) {
            throw new InvalidRequestData("TeamId is invalid, not existed or inactive " + teamId);
        }

        Team team = currentTeam.get();
        User user = currentUser.get();

        if (!user.getTeams().contains(team)) {
            throw new ResourceNotFound("User not in teams");
        }

        if (!team.getUsers().contains(user)) {
            throw new ResourceNotFound("Teams doesnot contain user");
        }

        user.getTeams().remove(team);
        team.getUsers().remove(user);

        userRepository.save(user);
        teamRepository.save(team);

//        Set<Role> roles = currentUser.get().getRoles();
//
//        for (Role role : roles) {
//            if (role.getName().contains("TEAM") && role.getName().contains("" + currentTeam.get().getId())) {
//                currentUser.get().getRoles().remove(role);
//                role.getUsers().remove(currentUser);
//
//                roleRepository.save(role);
//                userRepository.save(currentUser.get());
//
//            }
//        }



        return "User has out of team";
    }

    public void saveTeam(Team team, User user) {
        // add user to team as the team leader
        user.addTeam(team);
        team.addUser(user);

        userRepository.save(user);
        teamRepository.save(team);

        var currentTeam = teamRepository.findByName(team.getName());

        // create roles and privileges relating to team
        Privilege priView = privilegeService.createPrivilege("team", currentTeam.get().getId(), "view", "View team, id = " + currentTeam.get().getId());
        Privilege priUpdate = privilegeService.createPrivilege("team", currentTeam.get().getId(), "update", "Update team, id = " + currentTeam.get().getId());
        Privilege priDelete = privilegeService.createPrivilege("team", currentTeam.get().getId(), "delete", "Delete team, id = " + currentTeam.get().getId());

        Role roleAdmin = roleService.createRole("ADMIN", "team", currentTeam.get().getId());
        Role roleViewer = roleService.createRole("VIEWER", "team", currentTeam.get().getId());
        Role roleEditor = roleService.createRole("EDITOR", "team", currentTeam.get().getId());

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

        team.addRole(roleAdmin);
        roleAdmin.addTeam(team);
        team.addRole(roleViewer);
        roleViewer.addTeam(team);
        team.addRole(roleEditor);
        roleEditor.addTeam(team);

        teamRepository.save(team);

        roleRepository.save(roleEditor);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleViewer);

        userRepository.save(user);
    }
}
