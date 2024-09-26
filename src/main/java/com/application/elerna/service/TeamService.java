package com.application.elerna.service;

import com.application.elerna.dto.request.TeamRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.TeamResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface TeamService {

    public String createTeam(HttpServletRequest request, TeamRequest teamRequest);

    @PreAuthorize("hasPermission(#teamId, 'team', 'view')")
    public TeamResponse getTeamDetails(Long teamId);

    @PreAuthorize("hasPermission(#teamId, 'team', 'delete')")
    public String deleteTeam(Long teamId);

    public PageResponse<?> getAllTeam(Integer pageNo, Integer pageSize, String searchBy);

//    @PreAuthorize("hasPermission(#teamId, 'team', 'view')")
    public PageResponse<?> getJoinedTeam(Long userId, Integer pageNo, Integer pageSize, String searchBy);

    public String joinTeam(Long userId, Long teamId);
}
