package com.application.elerna.service;

import com.application.elerna.dto.request.TeamRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface TeamService {

    public String createTeam(HttpServletRequest request, TeamRequest teamRequest);

}
