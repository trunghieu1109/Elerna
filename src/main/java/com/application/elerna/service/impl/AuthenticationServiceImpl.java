package com.application.elerna.service.impl;

import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.TokenResponse;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.model.Token;
import com.application.elerna.model.User;
import com.application.elerna.service.AuthenticationService;
import com.application.elerna.service.JwtService;
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public TokenResponse signUp(SignUpRequest request) {

        // extract data
        String username = request.getUsername();

        // check is existed username
        List<UserDetail> users = userService.getAllUsers();
        for (UserDetail user : users) {
            if (username.equals(user.getUsername())) {
                throw new InvalidRequestData("Username had been existed in database");
            }
        }

        // create new user

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .cardNumber(request.getCardNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .build();

        // create new token

        log.info("Get access token");
        String accesssToken = jwtService.generateAccessToken(username);

        log.info("Get refresh token");
        String refreshToken = jwtService.generateRefreshToken(username);

        Token token = Token.builder()
                .accessToken(accesssToken)
                .refreshToken(refreshToken)
                .build();

        user.setToken(token);
        token.setUser(user);

        log.info("Save user");
        userService.saveUser(user);

        // add authentication to security context

        return TokenResponse.builder()
                .accessToken(accesssToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }
}
