package com.application.elerna.service.impl;

import com.application.elerna.dto.request.ResetPasswordRequest;
import com.application.elerna.dto.request.SignInRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.TokenResponse;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.BankAccount;
import com.application.elerna.model.Role;
import com.application.elerna.model.Token;
import com.application.elerna.model.User;
import com.application.elerna.repository.BankAccountRepository;
import com.application.elerna.service.*;
import com.application.elerna.utils.TokenEnum;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
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
    private final RoleService roleService;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;

    /**
     * Users sign up to server. They send a request with
     * register information and then receive access and
     * refresh token
     *
     * @param request SignUpRequest
     * @return TokenResponse
     */
    @Override
    public TokenResponse signUp(SignUpRequest request) {

        // extract data
        String username = request.getUsername();

        // check is existed username
        Optional<User> currentUser = userService.getByUserName(username);
        if (currentUser.isPresent()) {
            log.error("Username had been existed in database");
            throw new InvalidRequestData("Username had been existed in database");
        }

        var bankAccount = bankAccountRepository.findByCardNumber(request.getCardNumber());

        if (bankAccount.isPresent()) {
            throw new InvalidRequestData("Bank Account was matched with other user");
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
                .systemRole(request.getSystemRole())
                .isActive(true)
                .assignmentSubmissions(new HashSet<>())
                .teams(new HashSet<>())
                .roles(new HashSet<>())
                .contestSubmissions(new HashSet<>())
                .courses(new HashSet<>())
                .transactions(new HashSet<>())
                .courseRequests(new HashSet<>())
                .build();

        userService.saveUser(user);

        BankAccount bankAccount1 = bankAccountService.createBankAccount(user);
        bankAccountRepository.save(bankAccount1);

        user.setBankAccount(bankAccount1);
        userService.saveUser(user);

        // create new token
        log.info("Get access token");
        String accessToken = jwtService.generateAccessToken(username);

        log.info("Get refresh token");
        String refreshToken = jwtService.generateRefreshToken(username);

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(true)
                .build();

        log.info("User add token");
        user.setToken(token);
        token.setUser(user);

        log.info("Add global role to user");
        addGlobalRole(user);

        if (request.getSystemRole().equals("user")) {
            log.info("Add profile management role");
            userService.addProfileRole(user);
        } else {
            if (request.getSystemRole().equals("admin")) {
                log.info("Add system admin role");
                userService.addSystemAdminRole(user);
            }
        }

        log.info("Save user");
        userService.saveUser(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .resetToken("reset_token")
                .userId(user.getId())
                .build();
    }

    /**
     *
     * User login to sever. They send username, password
     * and server will check authentication.
     *
     * @param request SignInRequest
     * @return TokenResponse
     */
    @Override
    public TokenResponse authenticate(SignInRequest request) {

        // extract username, password
        String username = request.getUsername();
        String password = request.getPassword();

        // authenticate by authentication manager
        log.info("Authenticate user with username and password");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // Get user entity
        var user = userService.getByUserName(username).orElseThrow(() -> new ResourceNotFound("Can't get user with username"));

        // check user is active
        if (!user.isActive()) {
            log.error("User is in active");
            throw new InvalidRequestData("User is inactive");
        }

        Token token = tokenService.getById(user.getToken().getId());
        log.info("Token id: {}", token.getId());

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

    /**
     *
     * User request new access token while sending refresh
     * token. Server verify refresh token and provide new
     * access token
     *
     * @param request SignInRequest
     * @return TokenResponse
     */
    @Override
    public TokenResponse refresh(HttpServletRequest request) {

        // extract refresh token from header
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

    /**
     *
     * User logout.
     *
     * @param request HttpServletRequest
     */
    @Override
    public void logout(HttpServletRequest request) {

        // extract token from header
        String accessToken = request.getHeader("Authorization");

        log.info("Log out: Access Token: {}", accessToken);

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new InvalidRequestData("Logout: Access Token is invalid");
        }

        accessToken = accessToken.substring("Bearer ".length());

        // extract username and verify token
        String username = jwtService.extractUsername(accessToken, TokenEnum.ACCESS_TOKEN);
        if (username == null || username.isEmpty()) {
            throw new InvalidRequestData("Logout: Username is invalid");
        }

        Optional<User> user = userService.getByUserName(username);

        if (user.isEmpty()) {
            throw new ResourceNotFound("User not found, username: " + username);
        }

        if (!jwtService.isValid(accessToken, TokenEnum.ACCESS_TOKEN, user.get())) {
            throw new InvalidRequestData("Logout: Username is not matched or Token is expired");
        }

        // delete token
        Token currentToken = tokenService.getById(user.get().getToken().getId());

        tokenService.deleteToken(currentToken);

    }

    /**
     *
     * User send email to request resetting password.
     * Server check and send a confirmation email to user.
     *
     * @param email String
     * @return TokenResponse
     */
    @Override
    public TokenResponse forgotPassword(String email) throws MessagingException, UnsupportedEncodingException {

        // extract email
        var user = userService.getByEmail(email).orElseThrow(() -> new ResourceNotFound("Cant get user by email: " + email));

        if (!user.isActive()) {
            log.error("User is inactive");
            throw new InvalidRequestData("Email is matched with inactive user");
        }

        // generate reset token
        String resetToken = jwtService.generateResetToken(user.getUsername());
        Token token = user.getToken();

        // send email
        String url = String.format("curl -X 'POST' 'http://localhost/auth/confirm-reset' -H 'accept: */*' -H 'Content-Type: application/json' -d '\"%s\"'", resetToken);

//        mailService.sendEmail(email, "Confirm Reset Password", url, null);

        System.out.println(url);

        return TokenResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .resetToken(resetToken)
                .userId(user.getId())
                .build();
    }

    /**
     *
     * Server receive reset token and check whether
     * it matched with user in database or not
     *
     * @param resetToken String
     * @return String
     */
    @Override
    public String confirm(String resetToken) {

//        log.info(resetToken);

        if (resetToken == null || resetToken.isEmpty()) {
            throw new InvalidRequestData("Reset Token is invalid");
        }

        // extract username from token
        String username = jwtService.extractUsername(resetToken, TokenEnum.RESET_TOKEN);

        // check whether username existed or not
        var user = userService.getByUserName(username).orElseThrow(() -> new ResourceNotFound("Cant get user by username: " + username));
        if (!username.equals(user.getUsername())) {
            throw new InvalidRequestData("Reset token is not matched with user");
        }

        return "Accepted to reset password";

    }

    /**
     *
     * User send ResetPasswordRequest including password,
     * confirmation password and reset token. Then server
     * verify and change password.
     *
     * @param request ResetPasswordRequest
     * @return String
     */
    @Override
    public String resetPassword(ResetPasswordRequest request) {

        // validate reset token
        String resetToken = request.getResetToken();

        if (resetToken == null || resetToken.isEmpty()) {
            throw new InvalidRequestData("Reset Token is invalid");
        }

        // extract username
        String username = jwtService.extractUsername(resetToken, TokenEnum.RESET_TOKEN);

        var user = userService.getByUserName(username).orElseThrow(() -> new ResourceNotFound("Cant get user by username: " + username));
        if (!username.equals(user.getUsername())) {
            throw new InvalidRequestData("Reset token is not matched with user");
        }

        // compare password and confirmation
        String password = request.getPassword();
        String confirmPassword  = request.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            throw new InvalidRequestData("Password is not matched with confirm password");
        }

        // change password
        user.setPassword(passwordEncoder.encode(password));

        userService.saveUser(user);

        return "Change password successfully";
    }

    /**
     *
     * Add global role for user
     *
     * @param user User
     */
    private void addGlobalRole(User user) {
        Role global_team_add = roleService.getRoleByName("GLOBAL_TEAM_ADD");
        Role global_team_view = roleService.getRoleByName("GLOBAL_TEAM_VIEW");
        Role global_course_add = roleService.getRoleByName("GLOBAL_COURSE_ADD");
        Role global_course_view = roleService.getRoleByName("GLOBAL_COURSE_VIEW");
        Role global_transaction_add = roleService.getRoleByName("GLOBAL_TRANSACTION_ADD");

        user.addRole(global_course_add);
        global_course_add.addUser(user);

        user.addRole(global_transaction_add);
        global_transaction_add.addUser(user);

        user.addRole(global_team_add);
        global_team_add.addUser(user);

        user.addRole(global_course_view);
        global_course_view.addUser(user);

        user.addRole(global_team_view);
        global_team_view.addUser(user);
    }
}
