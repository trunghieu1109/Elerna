package com.application.elerna.controller;

import com.application.elerna.dto.request.AddLessonRequest;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.service.CourseResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.util.Date;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/course/resource")
public class CourseResourceController {

    private final CourseResourceService courseResourceService;

    @PostMapping(value="/add/lesson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> addLesson(@RequestParam("name") String name, @RequestParam("courseId") Long courseId, @RequestParam("files") MultipartFile file) {

//        log.info(request.getCourseId() + "");

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.addLesson(name, courseId, file));
    }

    @PostMapping(value="/add/assignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> addAssignment(@RequestParam("name") String name, @RequestParam("courseId") Long courseId,
                                              @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate,
                                              @RequestParam("files") MultipartFile file) {

//        log.info(request.getCourseId() + "");

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.addAssignment(name, courseId, startDate, endDate, file));
    }

    @PostMapping(value="/add/contest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> addContest(@RequestParam("name") String name, @RequestParam("courseId") Long courseId,
                                           @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate,
                                           @RequestParam("files") MultipartFile file) {

//        log.info(request.getCourseId() + "");

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.addContest(name, courseId, startDate, endDate, new Time(System.currentTimeMillis()), file));
    }

}
