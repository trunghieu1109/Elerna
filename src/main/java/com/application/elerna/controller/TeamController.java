package com.application.elerna.controller;

import com.application.elerna.dto.request.TeamRequest;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.service.TeamService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
