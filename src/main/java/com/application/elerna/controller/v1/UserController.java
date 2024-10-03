package com.application.elerna.controller.v1;

import com.application.elerna.dto.request.UserDetailRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.UserDetail;
import com.application.elerna.service.UserService;
import com.application.elerna.utils.EndpointDescription;
import com.application.elerna.utils.ResponseExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User's Information Management Controller",
        description = "These are functions that allow to manage user's information")
public class UserController {

    private final UserService userService;

    /**
     *
     * Get all users by sorting criterias.
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param sortBy String[]
     * @return PageResponse
     */
    @Operation(summary = "Get user list by sorting", description = EndpointDescription.getUserListBySorting,
            responses = { @ApiResponse(responseCode = "200", description = "Get list by sorting successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getAllUserBySearchExample))
            )})
    @GetMapping("/profile/sort")
    public PageResponse<?> getAllUserBySort(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false) String... sortBy) {

        return userService.getAllUsersBySort(pageNo, pageSize, sortBy);
    }

    /**
     *
     * Get all users by searching criterias.
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String[]
     * @return PageResponse
     */
    @Operation(summary = "Get user list by searching", description = EndpointDescription.getUserListBySearching,
            responses = { @ApiResponse(responseCode = "200", description = "Get list by searching successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getAllUserBySortExample))
            )})
    @GetMapping("/profile/search")
    public PageResponse<?> getAllUserBySearch(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false) String... searchBy) {

        return userService.getAllUsersBySearch(pageNo, pageSize, searchBy);
    }

    /**
     *
     * Get User's details with userId
     *
     * @param userId Long
     * @return ResponseData
     */
    @Operation(summary = "Get user's details", description = EndpointDescription.getUserDetail,
            responses = { @ApiResponse(responseCode = "200", description = "Get user's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getUserDetailExample))
            )})
    @GetMapping("/profile")
    public ResponseData<UserDetail> getUser(@RequestParam Long userId) {
        return new ResponseData<>(HttpStatus.OK, "Get user's details with userId " + userId, userService.getUserById(userId));
    }

    /**
     *
     * Update user's details
     *
     * @param request UserDetailRequest
     * @return ResponseData
     */
    @Operation(summary = "Update user's details", description = EndpointDescription.updateUserDetailDescription,
            responses = { @ApiResponse(responseCode = "200", description = "Update user's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.updateUserExample))
            )})
    @PostMapping("/update")
    public ResponseData<UserDetail> updateUser(@RequestBody UserDetailRequest request) {
        return new ResponseData<>(HttpStatus.OK, "Update user", userService.updateUser(request));
    }

    /**
     *
     * Delete user
     *
     * @param userId Long
     * @return ResponseData
     */
    @Operation(summary = "Delete user's account", description = EndpointDescription.deleteUser,
            responses = { @ApiResponse(responseCode = "200", description = "Delete user's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.deleteUserExample))
            )})
    @DeleteMapping("/delete")
    public ResponseData<String> deleteUser(@RequestParam Long userId) {
        return new ResponseData<>(HttpStatus.ACCEPTED, userService.deleteUser(userId));
    }

    /**
     *
     * User gets role list
     *
     * @param userId Long
     * @return ResponseData<List<String>>
     */
    @Operation(summary = "Get all user's roles", description = EndpointDescription.getUserRole,
            responses = { @ApiResponse(responseCode = "200", description = "Get all user's roles successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getUserRoleExample))
            )})
    @GetMapping("/role")
    public ResponseData<List<String>> getUserRole(@RequestParam Long userId) {
        return new ResponseData<>(HttpStatus.OK, "Get user role successfully", userService.getUserRole(userId));
    }

}
