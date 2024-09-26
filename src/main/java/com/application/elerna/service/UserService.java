package com.application.elerna.service;

import com.application.elerna.dto.request.UserDetailRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    public UserDetailsService userDetailsService();

    @PreAuthorize("hasPermission(-1, 'profile', 'all')")
    public PageResponse<?> getAllUsersBySort(Integer pageNo, Integer pageSize, String... sortBy);

    @PreAuthorize("hasPermission(-1, 'profile', 'all')")
    public PageResponse<?> getAllUsersBySearch(Integer pageNo, Integer pageSize, String... searchBy);

    public void saveUser(User user);

    public void addProfileRole(User user);

    public void addSystemAdminRole(User user);

    public Optional<User> getByUserName(String username);

    public Optional<User> getByEmail(String email);

    @PreAuthorize("hasPermission(#userId, 'profile', 'view')")
    public UserDetail getUserById(Long userId);

    @PreAuthorize("hasPermission(#request.getUserId(), 'profile', 'update')")
    public UserDetail updateUser(UserDetailRequest request);

    @PreAuthorize("hasPermission(#userId, 'profile', 'delete')")
    public String deleteUser(Long userId);

}
