package com.application.elerna.service;

import com.application.elerna.dto.request.UserDetailRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    UserDetailsService userDetailsService();

    @PreAuthorize("hasPermission(-1, 'profile', 'all')")
    PageResponse<?> getAllUsersBySort(Integer pageNo, Integer pageSize, String... sortBy);

    @PreAuthorize("hasPermission(-1, 'profile', 'all')")
    PageResponse<?> getAllUsersBySearch(Integer pageNo, Integer pageSize, String... searchBy);

    void saveUser(User user);

    void addProfileRole(User user);

    void addSystemAdminRole(User user);

    Optional<User> getByUserName(String username);

    Optional<User> getByEmail(String email);

    @PreAuthorize("hasPermission(#userId, 'profile', 'view')")
    UserDetail getUserById(Long userId);

    @PreAuthorize("hasPermission(#request.getUserId(), 'profile', 'update')")
    UserDetail updateUser(UserDetailRequest request);

    @PreAuthorize("hasPermission(#userId, 'profile', 'delete')")
    String deleteUser(Long userId);

    UserDetail createUserDetail(User user);

    User getUserFromAuthentication();

    List<String> getUserRole(Long userId);

    Long isExistedByUsername(Long username);

}
