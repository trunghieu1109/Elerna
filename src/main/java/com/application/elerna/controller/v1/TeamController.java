package com.application.elerna.controller.v1;

import com.application.elerna.dto.request.TeamRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.TeamResponse;
import com.application.elerna.service.TeamService;
import com.application.elerna.utils.ResponseExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
@Tag(name="Team Management", description = "These functions allows user to manage teams")
public class TeamController {

    private final TeamService teamService;

    /**
     *
     * Create team
     *
     * @param teamRequest TeamRequest
     * @return ResponseData
     */
    @Operation(summary = "Create team", description = "User creates team",
            responses = { @ApiResponse(responseCode = "200", description = "Create team successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.createTeamExample))
            )})
    @PostMapping("/create")
    public ResponseData<TeamResponse> createTeam(@Valid @RequestBody TeamRequest teamRequest) {

        return new ResponseData<>(HttpStatus.CREATED, "Create team successfully", teamService.createTeam(teamRequest));
    }

    /**
     *
     * Get team details with teamId
     *
     * @param teamId Long
     * @return ResponseData<TeamResponse>
     */
    @Operation(summary = "Get team's details", description = "User or admin gets team's details",
            responses = { @ApiResponse(responseCode = "200", description = "Get team's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getTeamDetailExamples))
            )})
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
    @Operation(summary = "Delete team", description = "User or admin delete team",
            responses = { @ApiResponse(responseCode = "200", description = "Delete team successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.deleteTeamExample))
            )})
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
    @Operation(summary = "Get all team list by searching", description = "Admin gets all team list by searching",
            responses = { @ApiResponse(responseCode = "200", description = "Get team list by searching successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getAllTeamListBySearchExample))
            )})
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
    @Operation(summary = "Get user's joined teams", description = "User gets all joined teams",
            responses = { @ApiResponse(responseCode = "200", description = "Get all joined teams successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getJoinedTeamsExample))
            )})
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
    @Operation(summary = "Join team", description = "User joins team",
            responses = { @ApiResponse(responseCode = "200", description = "Join team successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.joinTeamExample))
            )})
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
    @Operation(summary = "Out team", description = "User outs team",
            responses = { @ApiResponse(responseCode = "200", description = "Out team successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.outTeamExample))
            )})
    @PostMapping("/out/{teamId}")
    public ResponseData<String> outTeam(@PathVariable("teamId") Long teamId) {
        return new ResponseData<>(HttpStatus.OK, teamService.outTeam(teamId));
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
    @Operation(summary = "Get all team's member list", description = "User or admin gets all team's member list",
            responses = { @ApiResponse(responseCode = "200", description = "Get team's member list successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getMemberListExample))
            )})
    @GetMapping("/member/{teamId}")
    public PageResponse<?> getMemberLists(@PathVariable("teamId") Long teamId, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return teamService.getMemberList(teamId, pageNo, pageSize);
    }

}
