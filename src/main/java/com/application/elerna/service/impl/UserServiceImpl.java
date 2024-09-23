package com.application.elerna.service.impl;

import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.User;
import com.application.elerna.repository.UserRepository;
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound("Can't get user by username: " + username.toString()));
    }

    public List<UserDetail> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream().map(user -> UserDetail.builder()
                .address(user.getAddress())
                .email(user.getEmail())
                .phone(user.getPhone())
                .cardNumber(user.getCardNumber())
                .dateOfBirth(user.getDateOfBirth())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .build()).toList();

    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

}
