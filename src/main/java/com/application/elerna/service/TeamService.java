package com.application.elerna.service;

import com.application.elerna.dto.request.TeamRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.TeamResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface TeamService {

    TeamResponse createTeam(TeamRequest teamRequest);

    @PreAuthorize("hasPermission(#teamId, 'team', 'view')")
    TeamResponse getTeamDetails(Long teamId);

    @PreAuthorize("hasPermission(#teamId, 'team', 'delete')")
    String deleteTeam(Long teamId);

    PageResponse<?> getAllTeam(Integer pageNo, Integer pageSize, String searchBy);

    PageResponse<?> getJoinedTeam(Integer pageNo, Integer pageSize, String searchBy);

    String joinTeam(Long teamId);

    String outTeam(Long teamId);

    @PreAuthorize("hasPermission(#teamId, 'team', 'update')")
    PageResponse<?> getMemberList(Long teamId, Integer pageNo, Integer pageSize);
}
