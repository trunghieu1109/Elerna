package com.application.elerna.controller;

import com.application.elerna.dto.response.CourseResourceResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.service.CourseResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping(value = "/{resource_type}/list")
    public PageResponse<?> getAllResourceOfCourse(@RequestParam("courseId") Long courseId, @PathVariable("resource_type") String resourceType, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {

        log.info("get course resource");
        return courseResourceService.getAllResourceOfCourse(courseId, resourceType, pageNo, pageSize);
    }

    @GetMapping(value = "/download")
    public ResponseData<byte[]> download(@RequestParam String path, @RequestParam String resourceType) {

        try {
            return new ResponseData<>(HttpStatus.OK, "Download from path " + path, courseResourceService.download(path, resourceType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{resource_type}/details")
    public ResponseData<CourseResourceResponse> getResourceDetail(@RequestParam("resourceId") Long resourceId, @PathVariable("resource_type") String resourceType) {

        return new ResponseData<>(HttpStatus.OK, "Get " + resourceType + " details, lessonId " + resourceId, courseResourceService.getResourceDetail(resourceId, resourceType));
    }

    @PostMapping(value="/update/lesson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> updateLesson(@RequestParam("resourceId") Long resourceId, @RequestParam("name") String name,
                                             @RequestParam("courseId") Long courseId, @RequestParam(value = "files", required = false) MultipartFile file) {

//        log.info(request.getCourseId() + "");

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.updateLesson(resourceId, name, courseId, file));
    }

    @PostMapping(value="/update/assignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> updateAssignment(@RequestParam("resourceId") Long resourceId, @RequestParam("name") String name,
                                                 @RequestParam("courseId") Long courseId, @RequestParam("startDate") Date startDate,
                                                 @RequestParam("endDate") Date endDate, @RequestParam(value = "files", required = false) MultipartFile file) {

//        log.info(request.getCourseId() + "");

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.updateAssignment(resourceId, name, courseId, startDate, endDate, file));
    }

    @PostMapping(value="/update/contest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> updateContest(@RequestParam("resourceId") Long resourceId, @RequestParam("name") String name,
                                              @RequestParam("courseId") Long courseId, @RequestParam("startDate") Date startDate,
                                              @RequestParam("endDate") Date endDate, @RequestParam(value = "files", required = false) MultipartFile file) {

//        log.info(request.getCourseId() + "");

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.updateContest(resourceId, name, courseId, startDate, endDate, new Time(System.currentTimeMillis()), file));
    }

    @PostMapping(value = "/{target_type}/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> submit(@RequestParam("userId") Long userId, @PathVariable("target_type") String targetType, @RequestParam("target_id") Long targetId, @RequestParam("file") MultipartFile file) {
        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.submit(userId, targetType, targetId, file));
    }

    @GetMapping(value="/assignment/submision/list")
    public PageResponse<?> getSubmissionFromAssignment(@RequestParam Long assignmentId, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseResourceService.getAssignmentSubmissionListFromAssignment(assignmentId, pageNo, pageSize);
    }

    @GetMapping(value="/contest/submision/list")
    public PageResponse<?> getSubmissionFromContest(@RequestParam Long contestId, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseResourceService.getContestSubmissionListFromContest(contestId, pageNo, pageSize);
    }

    @PostMapping(value="/{resource_type}/delete")
    public ResponseData<String> deleteCourseResource(@PathVariable("resource_type") String resourceType, @RequestParam("resourceId") Long resourceId) {
        return new ResponseData<>(HttpStatus.ACCEPTED, courseResourceService.deleteCourseResource(resourceType, resourceId));
    }

}
