package com.application.elerna.controller.v1;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.request.UpdateCourseRequest;
import com.application.elerna.dto.response.CourseResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.model.User;
import com.application.elerna.service.CourseService;
import com.application.elerna.service.UserService;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
@Tag(name="Course Management", description = "These functions allow user to manage their courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    /**
     *
     * User sends creating course request
     *
     * @param request AddCourseRequest
     * @return ResponseData<String>
     */
    @Operation(summary = "Send creating course request", description = "User sends creating course request",
            responses = { @ApiResponse(responseCode = "200", description = "Send creating course request successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.sendCreatingCourseRequest))
            )})
    @PostMapping("/add")
    public ResponseData<String> sendCourseRequest(@RequestBody AddCourseRequest request) {

        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.addCourseRequest(request));
    }

    /**
     *
     * Admin gets all course requests
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Operation(summary = "Get all creating course requests", description = "Admin gets all creating course requests",
            responses = { @ApiResponse(responseCode = "200", description = "Get all creating course requests successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getAllCreatingCourseRequestExample))
            )})
    @GetMapping("/creating-requests")
    public PageResponse<?> getCourseRequest(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseService.getAllCourseRequest(pageNo, pageSize);
    }

    /**
     *
     * Admin approves course requests
     *
     * @param requestId Long
     * @return ResponseData
     */
    @Operation(summary = "Approve creating course requests", description = "Admin approves creating course requests",
            responses = { @ApiResponse(responseCode = "200", description = "Approve creating course requests",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.approveRequestExample))
            )})
    @PostMapping("/approve")
    public ResponseData<String> approveCourseRequest(@RequestParam Long requestId) {

        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.approveCourseRequest(requestId));
    }

    /**
     *
     * Admin rejects course requests
     *
     * @param requestId Long
     * @return ResponseData
     */
    @Operation(summary = "Reject creating course requests", description = "Admin rejects creating course requests",
            responses = { @ApiResponse(responseCode = "200", description = "Reject creating course requests",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.rejectRequestExample))
            )})
    @PostMapping("/reject")
    public ResponseData<String> rejectCourseRequest(@RequestParam Long requestId) {

        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.rejectCourseRequest(requestId));
    }

    /**
     *
     * Get all courses by searching criteria
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String[]
     * @return PageResponse
     */
    @Operation(summary = "Get all courses by searching", description = "User gets all courses by searching",
            responses = { @ApiResponse(responseCode = "200", description = "Get all courses successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getAllCourseExample))
            )})
    @GetMapping("/list")
    public PageResponse<?> getAllCoursesBySearch(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false) String... searchBy) {

        return courseService.getAllCourseBySearch(pageNo, pageSize, searchBy);
    }

    /**
     *
     * User gets course's details
     *
     * @param courseId Long
     * @return ResponseData
     */
    @Operation(summary = "Get course's details", description = "User gets course's details",
            responses = { @ApiResponse(responseCode = "200", description = "Get course's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.courseDetailExample))
            )})
    @GetMapping("/details")
    public ResponseData<CourseResponse> getCourseDetail(@RequestParam Long courseId) {

        return new ResponseData<>(HttpStatus.OK, "Get course details, courseId: " + courseId, courseService.getCourseDetail(courseId));
    }

    /**
     *
     * User registers course
     *
     * @param courseId Long
     * @return ResponseData<String>
     */
    @Operation(summary = "Register course", description = "User registers course",
            responses = { @ApiResponse(responseCode = "200", description = "Register course successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.registerCourseExample))
            )})
    @PostMapping("/register/user")
    public ResponseData<String> registerCourse(@RequestParam Long courseId) {
        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.registerCourse(courseId));
    }

    /**
     *
     * Team registers course
     *
     * @param teamId Long
     * @param courseId Long
     * @return ResponseData<String>
     */
    @Operation(summary = "Team Register course", description = "Team registers course",
            responses = { @ApiResponse(responseCode = "200", description = "Team registers course successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.teamRegisterCourseExample))
            )})
    @PostMapping("/register/team")
    public ResponseData<String> registerTeamCourse(@RequestParam Long teamId, @RequestParam Long courseId) {
        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.registerTeamCourse(teamId, courseId));
    }

    /**
     *
     * User gets registered courses
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Operation(summary = "Get user's registered courses", description = "User gets his registered courses",
            responses = { @ApiResponse(responseCode = "200", description = "Get user's registered courses successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getUserRegisteredCourse))
            )})
    @GetMapping("/registered/user")
    public PageResponse<?> getAllRegisteredCourse(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseService.getAllRegisteredCourse(pageNo, pageSize);
    }

    /**
     *
     * Team gets all registered courses
     *
     * @param teamId Long
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Operation(summary = "Get team's registered courses", description = "Team gets his registered courses",
            responses = { @ApiResponse(responseCode = "200", description = "Get team's registered courses successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getTeamRegisteredCourse))
            )})
    @GetMapping("/registered/team")
    public PageResponse<?> getAllRegisteredCourseByTeam(@RequestParam Long teamId, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseService.getAllRegisteredCourseByTeam(teamId, pageNo, pageSize);
    }

    /**
     *
     * User update course details
     *
     * @param request UpdateCourseRequest
     * @return ResponseData<String>
     */
    @Operation(summary = "Update course's details", description = "User updates course's details",
            responses = { @ApiResponse(responseCode = "200", description = "Update course's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.updateCourseExample))
            )})
    @PostMapping("/update")
    public ResponseData<String> updateCourse(@RequestBody UpdateCourseRequest request) {
        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.updateCourse(request));
    }

    /**
     *
     * Get course's student list
     *
     * @param courseId Long
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Operation(summary = "Get course's student list", description = "Course's Admin gets student list",
            responses = { @ApiResponse(responseCode = "200", description = "Get course's student list successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getStudentListExample))
            )})
    @GetMapping("/student-list")
    public PageResponse<?> getAllStudentList(@RequestParam Long courseId, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseService.getAllStudentList(courseId, pageNo, pageSize);
    }

    /**
     *
     * User unregisters course
     *
     * @param courseId Long
     * @return ResponseData<String>
     */
    @Operation(summary = "User unregisters course", description = "User unregisters course",
            responses = { @ApiResponse(responseCode = "200", description = "Unregister course successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.updateCourseExample))
            )})
    @PostMapping("/unregister/user")
    public ResponseData<String> unregisterCourse(@RequestParam Long courseId) {

        User user = userService.getUserFromAuthentication();

        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.unregisterCourse(user.getId(), courseId));
    }

    /**
     *
     * Group unregisters course
     *
     * @param teamId Long
     * @param courseId Long
     * @return ResponseData<String>
     */
    @Operation(summary = "Team unregisters course", description = "Team unregisters course",
            responses = { @ApiResponse(responseCode = "200", description = "Unregister course successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.teamUnregisterCourseExample))
            )})
    @PostMapping("/unregister/team")
    public ResponseData<String> unregisterTeamCourse(@RequestParam Long teamId, @RequestParam Long courseId) {

        return new ResponseData<>(HttpStatus.OK, courseService.unregisterTeamCourse(teamId, courseId));
    }

    /**
     *
     * User deletes course
     *
     * @param courseId Long
     * @return ResponseData<String>
     */
    @Operation(summary = "Delete course", description = "Course's admin deletes course",
            responses = { @ApiResponse(responseCode = "200", description = "Delete course successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.deleteCourseExample))
            )})
    @PostMapping("/delete")
    public ResponseData<String> deleteCourse(@RequestParam Long courseId) {
        return new ResponseData<>(HttpStatus.OK, courseService.deleteCourse(courseId));
    }

    /**
     *
     * Send register course request
     *
     * @param courseId Long
     * @return ResponseData<String>
     */
    @Operation(summary = "Send registering course request", description = "User sends registering course request",
            responses = { @ApiResponse(responseCode = "200", description = "Send registering course request successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.sendRegisteringRequestExample))
            )})
    @PostMapping("/send-register-request")
    public ResponseData<String> sendRegisterRequest(@RequestParam Long courseId) {

        return new ResponseData<>(HttpStatus.OK, "Get payment screen", courseService.sendRegisterRequest(courseId));
    }

}
