package com.application.elerna.controller;

import com.application.elerna.dto.request.UserDetailRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.model.User;
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/sort")
    public PageResponse<?> getAllUserBySort(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false) String... sortBy) {

        return userService.getAllUsersBySort(pageNo, pageSize, sortBy);
    }

    @GetMapping("/profile/search")
    public PageResponse<?> getAllUserBySearch(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false) String... searchBy) {

        return userService.getAllUsersBySearch(pageNo, pageSize, searchBy);
    }

    @GetMapping("/profile")
    public ResponseData<UserDetail> getUser(@RequestParam Long userId) {
        return new ResponseData<>(HttpStatus.OK, "Get user's details with userId " + userId, userService.getUserById(userId));
    }

    @PostMapping("/update")
    public ResponseData<UserDetail> updateUser(@RequestBody UserDetailRequest request) {
        return new ResponseData<>(HttpStatus.OK, "Update user", userService.updateUser(request));
    }

    @DeleteMapping("/delete")
    public ResponseData<String> deleteUser(@RequestParam Long userId) {
        return new ResponseData<>(HttpStatus.ACCEPTED, userService.deleteUser(userId));
    }

}
