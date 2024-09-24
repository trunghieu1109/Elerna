package com.application.elerna.service;

import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    public UserDetailsService userDetailsService();

    public List<UserDetail> getAllUsers();

    public void saveUser(User user);

    public Optional<User> getByUserName(String username);

    public Optional<User> getByEmail(String email);

}
