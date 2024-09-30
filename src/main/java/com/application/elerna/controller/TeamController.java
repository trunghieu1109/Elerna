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

    /**
     *
     * Create team
     *
     * @param request HttpServletRequest
     * @param teamRequest TeamRequest
     * @return ResponseData
     */
    @PostMapping("/create")
    public ResponseData<TeamResponse> createGroup(HttpServletRequest request, @Valid @RequestBody TeamRequest teamRequest) {

        return new ResponseData<>(HttpStatus.CREATED, "Create team successfully", teamService.createTeam(teamRequest));
    }

    /**
     *
     * Get team details with teamId
     *
     * @param teamId Long
     * @return ResponseData<TeamResponse>
     */
    @GetMapping("/details/{teamId}")
    public ResponseData<TeamResponse> getTeamDetails(@PathVariable("teamId") Long teamId) {

        return new ResponseData<>(HttpStatus.OK, "Get team details successfully", teamService.getTeamDetails(teamId));
    }

    /**
     *
     * Delete team with teamId
     *
     * @param teamId Long
     * @return ResponseData<String>
     */
    @DeleteMapping("/delete/{teamId}")
    public ResponseData<String> deleteTeam(@PathVariable("teamId") Long teamId) {
        return new ResponseData<>(HttpStatus.ACCEPTED, teamService.deleteTeam(teamId));
    }

    /**
     *
     * Get all teams by searching criteria
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String
     * @return PageResponse
     */
    @GetMapping("/list")
    public PageResponse<?> getAllTeamsBySearch(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false) String searchBy) {

        return teamService.getAllTeam(pageNo, pageSize, searchBy);
    }

    /**
     *
     * Get joined team list of a user
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String
     * @return PageResponse
     */
    @GetMapping("/joined")
    public PageResponse<?> getJoinedTeam(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false) String searchBy) {

        return teamService.getJoinedTeam(pageNo, pageSize, searchBy);
    }

    /**
     *
     * Users join team
     *
     * @param teamId Long
     * @return ResponseData<String>
     */
    @PostMapping("/join/{teamId}")
    public ResponseData<String> joinTeam(@PathVariable("teamId") Long teamId) {
        return new ResponseData<>(HttpStatus.OK, teamService.joinTeam(teamId));
    }

    /**
     *
     * Users out team
     *
     * @param teamId Long
     * @return ResponseData<String>
     */
    @PostMapping("/out/{teamId}")
    public ResponseData<String> outTeam(@PathVariable("teamId") Long teamId) {
        return new ResponseData<>(HttpStatus.OK, teamService.outTeam(teamId));
    }

}
