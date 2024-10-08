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
import com.application.elerna.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final PrivilegeService privilegeService;
    private final RoleService roleService;
    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final UtilsRepository utilsRepository;
    private final UserService userService;

    /**
     *
     * Create team
     *
     * @param teamRequest TeamRequest
     * @return TeamResponse
     */
    @Override
    public TeamResponse createTeam(TeamRequest teamRequest) {

        // extract group name
        String teamName = teamRequest.getName();

        // check if existed team
        Optional<Team> foundTeam = teamRepository.findByName(teamName);

        if (foundTeam.isPresent()) {
            log.error("Team name has been existed in database");
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
        User user = userService.getUserFromAuthentication();

        saveTeam(team, user);

        return createTeamResponse(team);
    }

    /**
     *
     * Get team's details by teamId
     *
     * @param teamId Long
     * @return TeamResponse
     */
    @Override
    @Transactional(readOnly = true)
    public TeamResponse getTeamDetails(Long teamId) {

        Optional<Team> team = teamRepository.findById(teamId);

        if (team.isEmpty()) {
            throw new ResourceNotFound("Team not found");
        }

        if (!team.get().isActive()) {
            throw new InvalidRequestData("Team is inactive");
        }

        return createTeamResponse(team.get());
    }

    /**
     *
     * Delete team by teamId
     *
     * @param teamId Long
     * @return String
     */
    @Override
    public String deleteTeam(Long teamId) {

        // validate team
        var team = teamRepository.findById(teamId);

        if (team.isEmpty()) {
            throw new ResourceNotFound("Team not found");
        }

        if (!team.get().isActive()) {
            throw new InvalidRequestData("Team is inactive");
        }

        // delete team by setting active = false
        team.get().setActive(false);
        team.get().setName(team.get().getName() + "-Deleted");

        teamRepository.save(team.get());

        return "Delete team successfully";
    }

    /**
     *
     * Get all team's details
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String
     * @return PageResponse<TeamResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getAllTeam(Integer pageNo, Integer pageSize, String searchBy) {
        Page<Team> teams = utilsRepository.findTeamByName(pageNo, pageSize, searchBy);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .totalPages(teams.getTotalPages())
                .pageSize(pageSize)
                .pageNo(pageNo)
                .data(teams.stream().map(this::createTeamResponse))
                .build();
    }

    /**
     *
     * Get user's joined team
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getJoinedTeam(Integer pageNo, Integer pageSize, String searchBy) {

        // get user from authentication
        User user = userService.getUserFromAuthentication();
        System.out.println(user.getId());

        // get user's joined teams
        List<Team> teams = new ArrayList<>();

        for (Team team : user.getTeams()) {
            if (team.getName().toLowerCase().contains(searchBy.toLowerCase()) && team.isActive()) {
                teams.add(team);
            }
        }

        int size_ = teams.size();

        Page<Team> page = new PageImpl<>(teams.subList(Math.min(pageNo * pageSize, teams.size()), Math.min((pageNo + 1) * pageSize, teams.size())),
                PageRequest.of(pageNo, pageSize),
                (int) Math.ceil((double)size_ / pageSize));

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil((double)size_ / pageSize))
                .data(page.stream().map(this::createTeamResponse))
                .build();
    }

    /**
     *
     * User joins team
     *
     * @param teamId Long
     * @return String
     */
    @Override
    public String joinTeam(Long teamId) {

        // get user from authentication
        User user = userService.getUserFromAuthentication();

        // find team
        var team = teamRepository.findById(teamId);

        if (team.isEmpty() || !team.get().isActive()) {
            throw new InvalidRequestData("Team is not ready (inactive or not existed)");
        }

        Team team_ = team.get();

        // user joins team
        team_.addUser(user);
        user.addTeam(team_);

        // save team and user
        teamRepository.save(team_);
        userRepository.save(user);

        Role roleViewer = roleService.getRoleByName("VIEWER_TEAM_" + team_.getId());

        user.addRole(roleViewer);
        roleViewer.addUser(user);

        roleRepository.save(roleViewer);
        userRepository.save(user);

        return "User " + user.getId() + " joins team successfully";
    }

    /**
     *
     * User out team
     *
     * @param teamId Long
     * @return String
     */
    @Override
    public String outTeam(Long teamId) {

        // get user from authentication
        User user = userService.getUserFromAuthentication();

        // find team
        var currentTeam = teamRepository.findById(teamId);

        if (currentTeam.isEmpty() || !currentTeam.get().isActive()) {
            throw new InvalidRequestData("TeamId is invalid, not existed or inactive " + teamId);
        }

        Team team = currentTeam.get();

        // remove user from team
        if (!user.getTeams().contains(team)) {
            throw new ResourceNotFound("User not in teams");
        }

        if (!team.getUsers().contains(user)) {
            throw new ResourceNotFound("Teams does not contain user");
        }

        userRepository.removeUserTeam(user.getId(), teamId);

        // remove user's roles
        Set<Role> roles = user.getRoles();

        for (Role role : roles) {
            if (role.getName().contains("TEAM") && role.getName().contains("" + currentTeam.get().getId())) {
                userRepository.removeUserRole(user.getId(), role.getId());

            }
        }

        return "User has out of team";
    }

    /**
     *
     * Get team's member list
     *
     * @param teamId Long
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getMemberList(Long teamId, Integer pageNo, Integer pageSize) {

        var team = teamRepository.findById(teamId);

        if (team.isEmpty() || !team.get().isActive()) {
            throw new ResourceNotFound("Team not ready, id = " + team.get().getId());
        }

        List<User> users = team.get().getUsers().stream().toList();

        int totalPages = (int) Math.ceil(users.size() * 1.0 / pageSize);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .data(users.subList(Math.max(pageNo * pageSize, 0), Math.min((pageNo + 1) * pageSize, users.size())).stream().map(userService::createUserDetail))
                .build();
    }

    /**
     *
     * User saves team
     *
     * @param team Team
     * @param user User
     */
    public void saveTeam(Team team, User user) {
        // add user to team as the team leader
        user.addTeam(team);
        team.addUser(user);

        userRepository.save(user);
        teamRepository.save(team);

        var currentTeam = teamRepository.findByName(team.getName());

        if (currentTeam.isEmpty()) {
            throw new ResourceNotFound("Team not found");
        }

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

        roleRepository.save(roleEditor);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleViewer);

        userRepository.save(user);
    }

    /**
     *
     * Create team response from team details
     *
     * @param team Team
     * @return TeamResponse
     */
    private TeamResponse createTeamResponse(Team team) {
        return TeamResponse.builder()
                .name(team.getName())
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }
}
