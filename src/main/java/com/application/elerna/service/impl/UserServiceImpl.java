package com.application.elerna.service.impl;

import com.application.elerna.dto.request.UserDetailRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.*;
import com.application.elerna.repository.BankAccountRepository;
import com.application.elerna.repository.UserRepository;
import com.application.elerna.repository.UtilsRepository;
import com.application.elerna.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UtilsRepository utilsRepository;
    private final PrivilegeService privilegeService;
    private final RoleService roleService;
    private final BankAccountRepository bankAccountRepository;

    /**
     *
     * UserDetailsService used for authentication
     *
     * @return UserDetailsService
     */
    public UserDetailsService userDetailsService() {
        return username ->
           userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound("Can't get user by username: " + username));
    }

    /**
     *
     * Get all users by sorting criteria
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param sortBy String[]
     * @return PageResponse
     */
    @Transactional(readOnly = true)
    public PageResponse<?> getAllUsersBySort(Integer pageNo, Integer pageSize, String... sortBy) {
        // extract sorting criteria and map to sort orders
        String strPattern = "(\\w+?)(:)(.*)";

        List<Sort.Order> orders = new ArrayList<>();

        if (sortBy != null) {
            for (String sortOrder : sortBy) {
                Pattern pattern = Pattern.compile(strPattern);

                Matcher matcher = pattern.matcher(sortOrder);

                if (matcher.find()) {
                    switch (matcher.group(3)) {
                        case "asc":
                            orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                            break;
                        case "desc":
                            orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                            break;
                        default:
                            log.error("Sort order is invalid");
                    }
                }
            }
        }

        // get all users by page request
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));

        Page<User> users = userRepository.findAll(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .status(HttpStatus.OK.value())
                .data(users.stream().map(this::createUserDetail).toList())
                .build();

    }

    /**
     *
     * Get all users by searching criteria.
     * Using Search Repository
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String[]
     * @return PageResponse
     */
    @Transactional(readOnly = true)
    public PageResponse<?> getAllUsersBySearch(Integer pageNo, Integer pageSize, String... searchBy) {

        Page<User> users = utilsRepository.findUserBySearchCriteria(pageNo, pageSize, searchBy);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .status(HttpStatus.OK.value())
                .data(users.stream().map(this::createUserDetail).toList())
                .build();

    }

    /**
     *
     * Save user to database
     *
     * @param user User
     */
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     *
     * Add normal user role in Profile.
     * Namely, View, Update, Delete Privilege
     * and Admin Role. By default, users has
     * Admin role with their profile
     *
     * @param user User
     */
    @Override
    public void addProfileRole(User user) {
        userRepository.save(user);

        var currentUser = getByUserName(user.getUsername());

        if (currentUser.isEmpty()) {
            throw new ResourceNotFound("User not found");
        }

        // create privileges
        Privilege priView = privilegeService.createPrivilege("profile", currentUser.get().getId(), "view", "View profile, id = " + currentUser.get().getId());
        Privilege priUpdate = privilegeService.createPrivilege("profile", currentUser.get().getId(), "update", "Update profile, id = " + currentUser.get().getId());
        Privilege priDelete = privilegeService.createPrivilege("profile", currentUser.get().getId(), "delete", "Delete profile, id = " + currentUser.get().getId());

        // create roles
        Role roleAdmin = roleService.createRole("ADMIN", "profile", currentUser.get().getId());

        // add relationship between roles and privileges
        roleAdmin.addPrivilege(priView);
        roleAdmin.addPrivilege(priUpdate);
        roleAdmin.addPrivilege(priDelete);

        priView.addRole(roleAdmin);
        priDelete.addRole(roleAdmin);
        priUpdate.addRole(roleAdmin);

        user.addRole(roleAdmin);
        roleAdmin.addUser(user);

        // save privileges and roles
        privilegeService.savePrivilege(priView);
        privilegeService.savePrivilege(priDelete);
        privilegeService.savePrivilege(priUpdate);

        roleService.saveRole(roleAdmin);
    }

    /**
     *
     * Add system admin role for user.
     * This role sets user with full privileges in
     * this system.
     *
     * @param user User
     */
    public void addSystemAdminRole(User user) {

        userRepository.save(user);

        Role roleSysAdmin = roleService.createRole("SYSTEM_ADMIN", "*", -1L);
        user.addRole(roleSysAdmin);
        roleSysAdmin.addUser(user);

        roleService.saveRole(roleSysAdmin);
    }

    /**
     *
     * Get User by Username
     *
     * @param username String
     * @return Optional<User>
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     *
     * Get User by email
     *
     * @param email String
     * @return Optional<User>
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     *
     * Get user by id
     *
     * @param userId Long
     * @return UserDetail
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetail getUserById(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("Cant get user by userId: " + userId));
        return createUserDetail(user);
    }

    /**
     *
     * Update user details
     *
     * @param request UserDetailRequest
     * @return UserDetail
     */
    @Override
    public UserDetail updateUser(UserDetailRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFound("Cant get user by userId: " + request.getUserId()));

        // update user with request details
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setCardNumber(request.getCardNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (!user.getCardNumber().equals(user.getBankAccount().getCardNumber())) {
            BankAccount account = user.getBankAccount();
            account.setCardNumber(request.getCardNumber());

            bankAccountRepository.save(account);
            user.setBankAccount(account);

        }

        // save user
        userRepository.save(user);

        return createUserDetail(user);
    }

    /**
     *
     * Delete user.
     *
     * @param userId Long
     * @return String
     */
    @Override
    public String deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("Cant find user with userId: " + userId));

        user.setActive(false);
        user.setUsername(user.getUsername() + "-Deleted");

        userRepository.save(user);

        log.info("Delete user successfully, userId: {}", userId);

        return "Delete user " + userId + " successfully";
    }

    /**
     *
     * Create UserDetail from User
     *
     * @param user User
     * @return UserDetail
     */
    @Override
    public UserDetail createUserDetail(User user) {
        return UserDetail.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .email(user.getEmail())
                .phone(user.getPhone())
                .cardNumber(user.getCardNumber())
                .dateOfBirth(user.getDateOfBirth())
                .cardHolder(user.getBankAccount().getCardHolder())
                .amount(user.getBankAccount().getAmount())
                .username("Username is hidden")
                .password("Password is hidden")
                .build();
    }

    /**
     *
     * Get user by extracting from Http Request
     *
     * @return User
     */
    @Override
    public User getUserFromAuthentication() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();


        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();

            System.out.println(username);

            User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound("User not found"));

            if (user.isActive()) {
                return user;
            } else {
                throw new InvalidRequestData("User is inactive");
            }
        } else {
            throw new InvalidRequestData("Principal in token is not UserDetails");
        }

    }

    /**
     *
     * Get all user's roles
     *
     * @param userId Long
     * @return List<String>
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> getUserRole(Long userId) {

        var user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new ResourceNotFound("User not found, id = " + userId);
        }

        Set<Role> roles = user.get().getRoles();

        Set<Team> teams = user.get().getTeams();

        for (Team team : teams) {
            roles.addAll(team.getRoles());
        }

        return roles.stream().map(role -> role.getName()).toList();
    }


}
