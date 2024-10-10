package com.application.elerna.service.impl;

import com.application.elerna.dto.request.ResetPasswordRequest;
import com.application.elerna.dto.request.SignInRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.TokenResponse;
import com.application.elerna.exception.*;
import com.application.elerna.model.BankAccount;
import com.application.elerna.model.Role;
import com.application.elerna.model.Token;
import com.application.elerna.model.User;
import com.application.elerna.service.*;
import com.application.elerna.utils.TokenEnum;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

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

    private final BankAccountService bankAccountService;

    @Value("${jwt.expirationHour}")
    private long expirationHour;

    @Value("${jwt.expirationDay}")
    private long expirationDay;

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
            throw new ResourceAlreadyExistedException("User", currentUser.get().getId());
        }

        log.info("Username {} not existed, ready for creating new user", username);

        BankAccount bankAccount = bankAccountService.getByCardnumber(request.getCardNumber());

        if (bankAccount != null) {
            throw new ResourceAlreadyExistedException("Bank Account", bankAccount.getId());
        }

        log.info("Card number {} not existed, ready for creating new bank account", request.getCardNumber());

        // create new user

        log.info("Creating new user");
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
                .tokens(new HashSet<>())
                .build();


        log.info("Save user and bank account");
        userService.saveUser(user);

        BankAccount bankAccount1 = bankAccountService.createBankAccount(user);
        bankAccountService.saveBankAccount(bankAccount1);

        user.setBankAccount(bankAccount1);
        userService.saveUser(user);

        // create new token
        log.info("Create access token");
        String accessToken = jwtService.generateAccessToken(username);

        log.info("Create refresh token");
        String refreshToken = jwtService.generateRefreshToken(username);

        String deviceId = UUID.randomUUID().toString();

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .uuid(deviceId)
                .accStatus(true)
                .refStatus(true)
                .accExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationHour))
                .refExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationDay))
                .build();

        log.info("User {} add token", user.getUsername());
        user.addToken(token);
        token.setUser(user);

        log.info("Add global role to user {}", user.getUsername());
        addGlobalRole(user);

        if (request.getSystemRole().equals("user")) {
            log.info("User {} add profile management role", user.getUsername());
            userService.addProfileRole(user);
        } else {
            if (request.getSystemRole().equals("admin")) {
                log.info("User {} add system admin role", user.getUsername());
                userService.addSystemAdminRole(user);
            }
        }

        log.info("Save user {}", user.getUsername());
        userService.saveUser(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .uuid(deviceId)
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
        String uuid = request.getUuid();

        // authenticate by authentication manager
        log.info("Authenticate username: {} and password: {}", username, password);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // Get user entity
        var user = userService.getByUserName(username).orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User with " + username + " haven't existed"));

        // check user is active
        if (!user.isActive()) {
            log.error("User {} is inactive", username);
            throw new DisabledException("User " + username + " is inactive");
        }

        log.info("User {} is activated, ready for login", username);

        // Generate new access token and refresh token
        log.info("Generating access and refresh token");
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        Token token = tokenService.getByUuid(uuid);

        if (token == null) {
            log.info("User {} are using new device. Create new uuid to login", user.getUsername());
            token = Token.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .uuid(UUID.randomUUID().toString())
                    .user(user)
                    .accStatus(true)
                    .refStatus(true)
                    .accExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationHour))
                    .refExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationDay))
                    .build();

            tokenService.saveToken(token);
        } else {
            log.info("Token uuid: {}", token.getUuid());

            if (!request.getUsername().equals(jwtService.extractUsername(token.getAccessToken(), TokenEnum.ACCESS_TOKEN))) {

                throw new BadCredentialsException("Device-Id " + uuid + " not matched to requested user");

            }

            token.setAccessToken(accessToken);
            token.setAccExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationHour));
            token.setRefreshToken(refreshToken);
            token.setRefExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationDay));
            token.setRefStatus(true);
            token.setAccStatus(true);

            tokenService.saveToken(token);
        }

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .resetToken("reset_token")
                .uuid(token.getUuid())
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
            throw new MalformedJwtException("Refresh Token is empty, refreshToken: " + refreshToken);
        }

        String accessToken = request.getHeader("Access-Token");

        if (accessToken == null || accessToken.isEmpty()) {
            throw new MalformedJwtException("Access Token is empty, accessToken: " + accessToken);
        }

        log.info("Received token: AccessToken: {}, RefreshToken: {}", accessToken, refreshToken);

        // extract username
        String username = jwtService.extractUsername(refreshToken, TokenEnum.REFRESH_TOKEN);
        if (username == null || username.isEmpty()) {
            throw new MalformedJwtException("Refresh token is not matched to any user, refreshToken: " + refreshToken);
        }

        String usernameAccess = jwtService.extractUsername(accessToken, TokenEnum.ACCESS_TOKEN);
        if (usernameAccess == null || usernameAccess.isEmpty()) {
            throw new MalformedJwtException("Access token is not matched to any user, accessToken: " + accessToken);
        }

        if (!username.equals(usernameAccess)) {
            throw new TokenMismatchException("AccessToken mismatched to RefreshToken", usernameAccess, username);
        }

        log.info("Access's User is matched to Refresh's User, namely {}", username);

        // get user details
        var userDetails = userService.getByUserName(username).orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Can't find user with username: " + username));

        // validate token
        if (!jwtService.isValid(refreshToken, TokenEnum.REFRESH_TOKEN, userDetails)) {
            throw new InvalidRequestData("Refresh: Username is not matched or token was expired");
        }

        log.info("Refresh token is valid");

        String deviceId = request.getHeader("Device-Id");

        // generate new access token
        String newAccessToken = jwtService.generateAccessToken(username);
        Token currentToken = tokenService.getByUuid(deviceId);

        if (currentToken == null) {

            throw new BadCredentialsException("Not existed suitable access token of " + username + "  for device-id: " + deviceId);

        }

        log.info("Found token with device-id: {}", deviceId);

        log.info("Save token to database");

        currentToken.setAccessToken(newAccessToken);
        currentToken.setAccExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expirationHour));
        currentToken.setAccStatus(true);
        tokenService.saveToken(currentToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .userId(userDetails.getId())
                .uuid(deviceId)
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

        log.info("Access Token: {}", accessToken);

        if (accessToken == null || accessToken.isEmpty() || !accessToken.startsWith("Bearer ")) {
            throw new MalformedJwtException("Access Token is empty, accessToken: " + accessToken);
        }

        accessToken = accessToken.substring("Bearer ".length());

        // extract username and verify token
        String username = jwtService.extractUsername(accessToken, TokenEnum.ACCESS_TOKEN);
        if (username == null || username.isEmpty()) {
            throw new MalformedJwtException("Access token is not matched to any user, accessToken: " + accessToken);
        }

        log.info("Access token is matched to {}", username);

        Optional<User> user = userService.getByUserName(username);

        if (user.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Can't find user with username: " + username);
        }

        if (!jwtService.isValid(accessToken, TokenEnum.ACCESS_TOKEN, user.get())) {
            throw new InvalidRequestData("Username is not matched or token was expired");
        }

        log.info("Access token is valid");

        String deviceId = request.getHeader("Device-Id");

        // delete token
        Token currentToken = tokenService.getByUuid(deviceId);

        if (currentToken == null) {

            throw new BadCredentialsException("Not existed suitable access token of " + username + "  for device-id: " + deviceId);
        }

        log.info("Delete token");
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
    public String forgotPassword(String email) throws MessagingException, UnsupportedEncodingException {

        // extract email
        var user = userService.getByEmail(email).orElseThrow(() -> new ResourceNotFound("User", "Email: " + email));

        if (!user.isActive()) {
            throw new DisabledException("User " + user.getUsername() + " is inactive");
        }

        log.info("Token is activated, matched with {}", user.getUsername());

        log.info("Generating reset token");

        // generate reset token
        String resetToken = jwtService.generateResetToken(user.getUsername());

        // send email
        log.info("Send confirmation email to {}", email);
        String url = String.format("curl -X 'POST' 'http://localhost/api/v1/auth/confirm-reset' -H 'accept: */*' -H 'Content-Type: application/json' -d '\"%s\"'", resetToken);

        mailService.sendEmail("hieukunno1109@gmail.com", "Confirm Reset Password", url, null);

        System.out.println(url);

        return "Send reset password request successfully";
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
            throw new MalformedJwtException("Reset Token is invalid");
        }

        log.info("Get reset token, token: " + resetToken);

        // extract username from token
        String username = jwtService.extractUsername(resetToken, TokenEnum.RESET_TOKEN);

        // check whether username existed or not
        var user = userService.getByUserName(username).orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Cant get user by username: " + username));
        if (!username.equals(user.getUsername())) {
            throw new MalformedJwtException("Reset token is not matched to any user, resetToken: " + resetToken);
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
            throw new MalformedJwtException("Reset Token is invalid");
        }

        // extract username
        String username = jwtService.extractUsername(resetToken, TokenEnum.RESET_TOKEN);

        var user = userService.getByUserName(username).orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Cant get user by username: " + username));
        if (!username.equals(user.getUsername())) {
            throw new MalformedJwtException("Reset token is not matched to any user, resetToken: " + resetToken);
        }

        // compare password and confirmation
        String password = request.getPassword();
        String confirmPassword  = request.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            throw new MalformedJwtException("Reset token is not matched to any user, resetToken: " + resetToken);
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
