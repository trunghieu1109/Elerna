package com.application.elerna.controller;

import com.application.elerna.dto.request.TeamRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.TeamResponse;
import com.application.elerna.service.TeamService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/group")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/create")
    public ResponseData<String> createGroup(HttpServletRequest request, @Valid @RequestBody TeamRequest teamRequest) {

        return new ResponseData<>(HttpStatus.CREATED, teamService.createTeam(request, teamRequest));
    }

    @GetMapping("/details/{teamId}")
    public ResponseData<TeamResponse> getTeamDetails(@PathVariable("teamId") Long teamId) {

//        log.info("TeamId: " + teamId);

        return new ResponseData<>(HttpStatus.OK, "Get team details successfully", teamService.getTeamDetails(teamId));
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseData<String> deleteTeam(@PathVariable("teamId") Long teamId) {
        return new ResponseData<>(HttpStatus.ACCEPTED, teamService.deleteTeam(teamId));
    }

    @GetMapping("/list")
    public PageResponse<?> getAllTeamsBySearch(Integer pageNo, Integer pageSize, String searchBy) {

        return teamService.getAllTeam(pageNo, pageSize, searchBy);
    }


    @GetMapping("/joined")
    public PageResponse<?> getJoinedTeam(Long userId, Integer pageNo, Integer pageSize, String searchBy) {

        return teamService.getJoinedTeam(userId, pageNo, pageSize, searchBy);
    }

    @PostMapping("/join/{teamId}")
    public ResponseData<String> joinTeam(Long userId, @PathVariable("teamId") Long teamId) {
        return new ResponseData<>(HttpStatus.OK, teamService.joinTeam(userId, teamId));
    }

    @PostMapping("/out/{teamId}")
    public ResponseData<String> outTeam(Long userId, @PathVariable("teamId") Long teamId) {
        return new ResponseData<>(HttpStatus.OK, teamService.outTeam(userId, teamId));
    }

}
