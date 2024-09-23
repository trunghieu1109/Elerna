package com.application.elerna.controller;

import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.model.User;
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/all")
    public ResponseData<List<UserDetail>> getAllUser() {

        return new ResponseData<>(HttpStatus.OK, "Get all user successfully", userService.getAllUsers());
    }

}
