package com.application.elerna.controller;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.response.CourseRequestResponse;
import com.application.elerna.dto.response.CourseResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/add")
    public ResponseData<String> sendCourseRequest(@RequestBody AddCourseRequest request) {

        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.addCourseRequest(request));
    }

    @GetMapping("/upload-requests")
    public PageResponse<?> getCourseRequest(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseService.getAllCourseRequest(pageNo, pageSize);
    }

    @PostMapping("/approve")
    public ResponseData<String> approveCourseRequest(@RequestParam Long requestId) {

        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.approveCourseRequest(requestId));
    }

    @PostMapping("/reject")
    public ResponseData<String> rejectCourseRequest(@RequestParam Long requestId) {

        return new ResponseData<>(HttpStatus.ACCEPTED, courseService.rejectCourseRequest(requestId));
    }

//    @GetMapping("/list")
//    public PageResponse<?> getAllCourses(Integer pageNo, Integer pageSize) {
//
//        return courseService.getAllCourse(pageNo, pageSize);
//    }

    @GetMapping("/list")
    public PageResponse<?> getAllCoursesBySearch(Integer pageNo, Integer pageSize, String... searchBy) {

        return courseService.getAllCourseBySearch(pageNo, pageSize, searchBy);
    }

    @GetMapping("/details")
    public ResponseData<CourseResponse> getCourseDetail(Long courseId) {

        return new ResponseData<>(HttpStatus.OK, "Get course details, courseId: " + courseId, courseService.getCourseDetail(courseId));
    }

}
