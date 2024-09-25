package com.application.elerna.service.impl;

import com.application.elerna.dto.request.ResetPasswordRequest;
import com.application.elerna.dto.request.SignInRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.TokenResponse;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.Token;
import com.application.elerna.model.User;
import com.application.elerna.repository.TokenRepository;
import com.application.elerna.service.*;
import com.application.elerna.utils.TokenEnum;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final MailService mailService;

    @Override
    public TokenResponse signUp(SignUpRequest request) {

        // extract data
        String username = request.getUsername();

        // check is existed username
        Optional<User> currentUser = userService.getByUserName(username);
        if (!currentUser.isEmpty()) {
            throw new InvalidRequestData("Username had been existed in database");
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
                .status(true)
                .build();

        user.setToken(token);
        token.setUser(user);

        log.info("Save user");
        userService.saveUser(user);

        return TokenResponse.builder()
                .accessToken(accesssToken)
                .refreshToken(refreshToken)
                .resetToken("reset_token")
                .userId(user.getId())
                .build();
    }

    @Override
    public TokenResponse authenticate(SignInRequest request) {

        // extract username, password

        String username = request.getUsername();
        String password = request.getPassword();

        // authenticate by authentication manager
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // Get user entity
        var user = userService.getByUserName(username).orElseThrow(() -> new ResourceNotFound("Can't get user with username"));

        Token token = tokenService.getById(user.getToken().getId());
        log.info("Token id: " + token.getId());

        // Generate new access token and refresh token
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setStatus(true);

        tokenService.saveToken(token);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .resetToken("reset_token")
                .userId(user.getId())
                .build();
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {

        // extract refresh token
        String refreshToken = request.getHeader("Refresh-Token");

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidRequestData("Refresh: Refresh Token is invalid");
        }

        // extract username
        String username = jwtService.extractUsername(refreshToken, TokenEnum.REFRESH_TOKEN);
        if (username == null || username.isEmpty()) {
            throw new InvalidRequestData("Refresh: Refresh Token not mapped to any user");
        }

        // get user details
        var userDetails = userService.getByUserName(username).orElseThrow(() -> new ResourceNotFound("Can't find user with username: " + username));

        // validate token
        if (!jwtService.isValid(refreshToken, TokenEnum.REFRESH_TOKEN, userDetails)) {
            throw new InvalidRequestData("Refresh: Username is not matched or token was expired");
        }

        // generate new access token
        String accessToken = jwtService.generateAccessToken(username);
        Token currentToken = tokenService.getById(userDetails.getToken().getId());

        if (currentToken == null) {
            throw new ResourceNotFound("Can't get token by id");
        }

        currentToken.setAccessToken(accessToken);
        tokenService.saveToken(currentToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userDetails.getId())
                .resetToken("reset_token")
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");

        log.info("Log out: Access Token: " + accessToken);

        if (accessToken == null || accessToken.isEmpty() || !accessToken.startsWith("Bearer ")) {
            throw new InvalidRequestData("Logout: Access Token is invalid");
        }

        accessToken = accessToken.substring("Bearer ".length());

        String username = jwtService.extractUsername(accessToken, TokenEnum.ACCESS_TOKEN);
        if (username == null || username.isEmpty()) {
            throw new InvalidRequestData("Logout: Username is invalid");
        }

        Optional<User> user = userService.getByUserName(username);

        if (!jwtService.isValid(accessToken, TokenEnum.ACCESS_TOKEN, user.get())) {
            throw new InvalidRequestData("Logout: Username is not matched or Token is expired");
        }

        Token currentToken = tokenService.getById(user.get().getToken().getId());

        tokenService.deleteToken(currentToken);

    }

    @Override
    public TokenResponse forgotPassword(String email) throws MessagingException, UnsupportedEncodingException {
        var user = userService.getByEmail(email).orElseThrow(() -> new ResourceNotFound("Cant get user by email: " + email));

        String resetToken = jwtService.generateResetToken(user.getUsername());
        Token token = user.getToken();

        String url = String.format("curl -X 'POST' \\\n" +
                "  'http://localhost/auth/confirm-reset' \\\n" +
                "  -H 'accept: */*' \\\n" +
                "  -H 'Content-Type: application/json' \\\n" +
                "  -d '\"%s\"'", resetToken);

//        mailService.sendEmail("hieukunno1109@gmail.com", "Confirm Reset Password", url, null);

        System.out.println(url);

        return TokenResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .resetToken(resetToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public String confirm(String resetToken) {

//        log.info(resetToken);

        if (resetToken == null || resetToken.isEmpty()) {
            throw new InvalidRequestData("Reset Token is invalid");
        }

        String username = jwtService.extractUsername(resetToken, TokenEnum.RESET_TOKEN);

        var user = userService.getByUserName(username).orElseThrow(() -> new ResourceNotFound("Cant get user by username: " + username));
        if (!username.equals(user.getUsername())) {
            throw new InvalidRequestData("Reset token is not matched with user");
        }

        return "Accepted to reset password";

    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {

        String resetToken = request.getResetToken();

        if (resetToken == null || resetToken.isEmpty()) {
            throw new InvalidRequestData("Reset Token is invalid");
        }

        String username = jwtService.extractUsername(resetToken, TokenEnum.RESET_TOKEN);

        var user = userService.getByUserName(username).orElseThrow(() -> new ResourceNotFound("Cant get user by username: " + username));
        if (!username.equals(user.getUsername())) {
            throw new InvalidRequestData("Reset token is not matched with user");
        }

        String password = request.getPassword();
        String confirmPassword  = request.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            throw new InvalidRequestData("Password is not matched with confirm password");
        }

        user.setPassword(passwordEncoder.encode(password));

        userService.saveUser(user);

        return "Change password successfully";
    }
}
