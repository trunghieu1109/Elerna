package com.application.elerna.service.impl;

import com.application.elerna.dto.request.UserDetailRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.Privilege;
import com.application.elerna.model.Role;
import com.application.elerna.model.Team;
import com.application.elerna.model.User;
import com.application.elerna.repository.UserRepository;
import com.application.elerna.repository.UtilsRepository;
import com.application.elerna.service.MailService;
import com.application.elerna.service.PrivilegeService;
import com.application.elerna.service.RoleService;
import com.application.elerna.service.UserService;
import com.application.elerna.utils.CustomizedGrantedAuthority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public UserDetailsService userDetailsService() {
        return username ->
           userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound("Can't get user by username: " + username.toString()));
    }

    public PageResponse<?> getAllUsersBySort(Integer pageNo, Integer pageSize, String... sortBy) {

        Pageable pageable = null;

//        log.info("Get all users by sort");

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

        pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));

        Page<User> users = userRepository.findAll(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .status(HttpStatus.OK.value())
                .data(users.stream().map(user -> UserDetail.builder()
                        .address(user.getAddress())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .cardNumber(user.getCardNumber())
                        .dateOfBirth(user.getDateOfBirth())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .build()).toList())
                .build();

    }

    public PageResponse<?> getAllUsersBySearch(Integer pageNo, Integer pageSize, String... searchBy) {

        Page<User> users = utilsRepository.findUserBySearchCriteria(pageNo, pageSize, searchBy);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .status(HttpStatus.OK.value())
                .data(users.stream().map(user -> UserDetail.builder()
                        .address(user.getAddress())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .cardNumber(user.getCardNumber())
                        .dateOfBirth(user.getDateOfBirth())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .build()).toList())
                .build();

    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void addProfileRole(User user) {
        userRepository.save(user);

        var currentUser = getByUserName(user.getUsername());

        Privilege priView = privilegeService.createPrivilege("profile", currentUser.get().getId(), "view", "View profile, id = " + currentUser.get().getId());
        Privilege priUpdate = privilegeService.createPrivilege("profile", currentUser.get().getId(), "update", "Update profile, id = " + currentUser.get().getId());
        Privilege priDelete = privilegeService.createPrivilege("profile", currentUser.get().getId(), "delete", "Delete profile, id = " + currentUser.get().getId());

        Role roleAdmin = roleService.createRole("ADMIN", "profile", currentUser.get().getId());

        roleAdmin.addPrivilege(priView);
        roleAdmin.addPrivilege(priUpdate);
        roleAdmin.addPrivilege(priDelete);

        priView.addRole(roleAdmin);
        priDelete.addRole(roleAdmin);
        priUpdate.addRole(roleAdmin);

        user.addRole(roleAdmin);
        roleAdmin.addUser(user);

        privilegeService.savePrivilege(priView);
        privilegeService.savePrivilege(priDelete);
        privilegeService.savePrivilege(priUpdate);

        roleService.saveRole(roleAdmin);
    }

    public void addSystemAdminRole(User user) {

        userRepository.save(user);

        Role roleSysAdmin = roleService.createRole("SYSTEM_ADMIN", "*", -1L);
        user.addRole(roleSysAdmin);
        roleSysAdmin.addUser(user);

        roleService.saveRole(roleSysAdmin);
    }



    @Override
    public Optional<User> getByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetail getUserById(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("Cant get user by userId: " + userId));

        UserDetail userDetail = UserDetail.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .email(user.getEmail())
                .phone(user.getPhone())
                .cardNumber(user.getCardNumber())
                .dateOfBirth(user.getDateOfBirth())
                .username("Username is hidden")
                .password("Password is hidden")
                .build();

        return userDetail;
    }

    @Override
    public UserDetail updateUser(UserDetailRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFound("Cant get user by userId: " + request.getUserId()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setCardNumber(request.getCardNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        return UserDetail.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .email(user.getEmail())
                .phone(user.getPhone())
                .cardNumber(user.getCardNumber())
                .dateOfBirth(user.getDateOfBirth())
                .username("Username is hidden")
                .password("Password is hidden")
                .build();
    }

    @Override
    public String deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("Cant find user with userId: " + userId));

        user.setActive(false);
        user.setUsername(user.getUsername() + "-Deleted");

        userRepository.save(user);

        log.info("Delete user successfully, userId: " + userId);

        return "Delete user successfully";
    }

}
