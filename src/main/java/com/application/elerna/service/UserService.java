package com.application.elerna.service;

import com.application.elerna.dto.request.UserDetailRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    public UserDetailsService userDetailsService();

    public PageResponse<?> getAllUsersBySort(Integer pageNo, Integer pageSize, String... sortBy);

    public PageResponse<?> getAllUsersBySearch(Integer pageNo, Integer pageSize, String... searchBy);

    public void saveUser(User user);

    public Optional<User> getByUserName(String username);

    public Optional<User> getByEmail(String email);

    public UserDetail getUserById(Long userId);

    public UserDetail updateUser(UserDetailRequest request);

    public String deleteUser(Long userId);

}
