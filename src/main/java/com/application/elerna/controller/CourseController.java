package com.application.elerna.controller;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.request.UpdateCourseRequest;
import com.application.elerna.dto.response.CourseResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.model.User;
import com.application.elerna.service.CourseService;
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/course")
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
    @GetMapping("/upload-requests")
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
    @GetMapping("/list")
    public PageResponse<?> getAllCoursesBySearch(Integer pageNo, Integer pageSize, String... searchBy) {

        return courseService.getAllCourseBySearch(pageNo, pageSize, searchBy);
    }

    /**
     *
     * User gets course's details
     *
     * @param courseId Long
     * @return ResponseData
     */
    @GetMapping("/details")
    public ResponseData<CourseResponse> getCourseDetail(Long courseId) {

        return new ResponseData<>(HttpStatus.OK, "Get course details, courseId: " + courseId, courseService.getCourseDetail(courseId));
    }

    /**
     *
     * User registers course
     *
     * @param courseId Long
     * @return ResponseData<String>
     */
    @PostMapping("/register")
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
    @PostMapping("/register/group")
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
    @PostMapping("/unregister")
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
    @PostMapping("/unregister/group")
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
    @PostMapping("/send-register-request")
    public ResponseData<String> sendRegisterRequest(@RequestParam Long courseId) {

        return new ResponseData<>(HttpStatus.OK, "Get payment screen", courseService.sendRegisterRequest(courseId));
    }

}
