package com.application.elerna.controller;

import com.application.elerna.dto.response.CourseResourceResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.service.CourseResourceService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/course/resource")
@Tag(name="Course Resources Management", description = "These functions allows to manage course's resources, such as lesson, assignment, contest, v.v")
public class CourseResourceController {

    private final CourseResourceService courseResourceService;

    /**
     *
     * Add Lesson to Course
     *
     * @param name String
     * @param courseId Long
     * @param file MultipartFile
     * @return ResponseData<String>
     */
    @Operation(summary = "Add lesson", description = "User adds lesson to courses",
            responses = { @ApiResponse(responseCode = "200", description = "Add lesson successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.addLessonExample))
            )})
    @PostMapping(value="/add/lesson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> addLesson(@RequestParam("name") String name, @RequestParam("courseId") Long courseId, @RequestParam("files") MultipartFile file) {

//        log.info(request.getCourseId() + "");

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.addLesson(name, courseId, file));
    }

    /**
     *
     * Add assignment to course
     *
     * @param name String name
     * @param courseId Long
     * @param startDate Date
     * @param endDate Date
     * @param file MultipartFile
     * @return ResponseData<String>
     */
    @Operation(summary = "Add assignment", description = "User adds assignment to courses",
            responses = { @ApiResponse(responseCode = "200", description = "Add assignment successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.addAssignmentExample))
            )})
    @PostMapping(value="/add/assignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> addAssignment(@RequestParam("name") String name, @RequestParam("courseId") Long courseId,
                                              @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate,
                                              @RequestParam("files") MultipartFile file) {

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.addAssignment(name, courseId, startDate, endDate, file));
    }

    /**
     *
     * Add contest to course
     *
     * @param name String
     * @param courseId Long
     * @param startDate Date
     * @param endDate Date
     * @param file MultipartFile
     * @return String
     */
    @Operation(summary = "Add contest", description = "User adds contest to courses",
            responses = { @ApiResponse(responseCode = "200", description = "Add contest successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.addContestExample))
            )})
    @PostMapping(value="/add/contest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> addContest(@RequestParam("name") String name, @RequestParam("courseId") Long courseId,
                                           @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate,
                                           @RequestParam("files") MultipartFile file) {

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.addContest(name, courseId, startDate, endDate, new Time(System.currentTimeMillis()), file));
    }

    /**
     *
     * Get course's resources list
     *
     * @param courseId Long
     * @param resourceType String
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Operation(summary = "Get resource list", description = "User or admin get resource list (lesson, assignment, v.v)",
            responses = { @ApiResponse(responseCode = "200", description = "Get resource list successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getResourceListExample))
            )})
    @GetMapping(value = "/{resource_type}/list")
    public PageResponse<?> getAllResourceOfCourse(@RequestParam("courseId") Long courseId, @PathVariable("resource_type") String resourceType, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {

        log.info("get course resource");
        return courseResourceService.getAllResourceOfCourse(courseId, resourceType, pageNo, pageSize);
    }

    /**
     *
     * Download resource's content from server
     *
     * @param path String
     * @param resourceType String
     * @return ResponseDate<byte[]>
     */
    @Operation(summary = "Download resource", description = "User send request to get resource from server",
            responses = { @ApiResponse(responseCode = "200", description = "Download resource successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.downloadResourceExample))
            )})
    @GetMapping(value = "/download")
    public ResponseData<byte[]> download(@RequestParam String path, @RequestParam String resourceType) {

        try {
            return new ResponseData<>(HttpStatus.OK, "Download from path " + path, courseResourceService.download(path, resourceType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Get course's resource details
     *
     * @param resourceId Long
     * @param resourceType String
     * @return ResponseData<CourseResourceResponse>
     */
    @Operation(summary = "Get resource's details", description = "User gets resource's details",
            responses = { @ApiResponse(responseCode = "200", description = "Get resource's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getResourceDetailExample))
            )})
    @GetMapping(value = "/{resource_type}/details")
    public ResponseData<CourseResourceResponse> getResourceDetail(@RequestParam("resourceId") Long resourceId, @PathVariable("resource_type") String resourceType) {

        return new ResponseData<>(HttpStatus.OK, "Get " + resourceType + " details, lessonId " + resourceId, courseResourceService.getResourceDetail(resourceId, resourceType));
    }

    /**
     *
     * Update lesson details
     *
     * @param resourceId Long
     * @param name String
     * @param courseId Long
     * @param file MultipartFile
     * @return ResponseData<String>
     */
    @Operation(summary = "Update lesson's details", description = "User update lesson's details",
            responses = { @ApiResponse(responseCode = "200", description = "Update lesson's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.updateLessonExample))
            )})
    @PostMapping(value="/update/lesson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> updateLesson(@RequestParam("resourceId") Long resourceId, @RequestParam("name") String name,
                                             @RequestParam("courseId") Long courseId, @RequestParam(value = "files", required = false) MultipartFile file) {

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.updateLesson(resourceId, name, courseId, file));
    }

    /**
     *
     * Update assignment details
     *
     * @param resourceId Long
     * @param name String
     * @param courseId Long
     * @param startDate Date
     * @param endDate Date
     * @param file MultipartFile
     * @return ResponseData<String>
     */
    @Operation(summary = "Update assignment's details", description = "User update assignment's details",
            responses = { @ApiResponse(responseCode = "200", description = "Update assignment's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.updateAssignmentExample))
            )})
    @PostMapping(value="/update/assignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> updateAssignment(@RequestParam("resourceId") Long resourceId, @RequestParam("name") String name,
                                                 @RequestParam("courseId") Long courseId, @RequestParam("startDate") Date startDate,
                                                 @RequestParam("endDate") Date endDate, @RequestParam(value = "files", required = false) MultipartFile file) {

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.updateAssignment(resourceId, name, courseId, startDate, endDate, file));
    }

    /**
     *
     * Update contest details
     *
     * @param resourceId Long
     * @param name String
     * @param courseId Long
     * @param startDate Date
     * @param endDate Date
     * @param file MultipartFile
     * @return ResponseData<String>
     */
    @Operation(summary = "Update contest's details", description = "User update contest's details",
            responses = { @ApiResponse(responseCode = "200", description = "Update contest's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.updateContestExample))
            )})
    @PostMapping(value="/update/contest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> updateContest(@RequestParam("resourceId") Long resourceId, @RequestParam("name") String name,
                                              @RequestParam("courseId") Long courseId, @RequestParam("startDate") Date startDate,
                                              @RequestParam("endDate") Date endDate, @RequestParam(value = "files", required = false) MultipartFile file) {

        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.updateContest(resourceId, name, courseId, startDate, endDate, new Time(System.currentTimeMillis()), file));
    }

    /**
     *
     * Submit for assignment or contest
     *
     * @param targetType String
     * @param targetId Long
     * @param file MultipartFile
     * @return ResponseData<String>
     */
    @Operation(summary = "Submit for assignment or contest", description = "User submits for assignment or contest",
            responses = { @ApiResponse(responseCode = "200", description = "Submit for assignment or contest successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.submitResourceExample))
            )})
    @PostMapping(value = "/{target_type}/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<String> submit(@PathVariable("target_type") String targetType, @RequestParam("target_id") Long targetId, @RequestParam("file") MultipartFile file) {
        return new ResponseData<>(HttpStatus.CREATED, courseResourceService.submit(targetType, targetId, file));
    }

    /**
     *
     * Get assignment submission
     *
     * @param assignmentId Long
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Operation(summary = "Get assignment submission list", description = "User gets assignment submission list",
            responses = { @ApiResponse(responseCode = "200", description = "Get assignment submission list successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.assignmentSubmissionExample))
            )})
    @GetMapping(value="/assignment/submission/list")
    public PageResponse<?> getSubmissionFromAssignment(@RequestParam Long assignmentId, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseResourceService.getAssignmentSubmissionListFromAssignment(assignmentId, pageNo, pageSize);
    }

    /**
     *
     * Get contest submission
     *
     * @param contestId Long
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Operation(summary = "Get contest submission list", description = "User gets contest submission list",
            responses = { @ApiResponse(responseCode = "200", description = "Get contest submission list successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.contestSubmissionExample))
            )})
    @GetMapping(value="/contest/submission/list")
    public PageResponse<?> getSubmissionFromContest(@RequestParam Long contestId, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {

        return courseResourceService.getContestSubmissionListFromContest(contestId, pageNo, pageSize);
    }

    /**
     *
     * Delete course's resources
     *
     * @param resourceType String
     * @param resourceId Long
     * @return Response<String>
     */
    @Operation(summary = "Delete resource from server", description = "User delete resource from server",
            responses = { @ApiResponse(responseCode = "200", description = "Delete resource from server successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.deleteCourseExample))
            )})
    @PostMapping(value="/{resource_type}/delete")
    public ResponseData<String> deleteCourseResource(@PathVariable("resource_type") String resourceType, @RequestParam("resourceId") Long resourceId) {
        return new ResponseData<>(HttpStatus.ACCEPTED, courseResourceService.deleteCourseResource(resourceType, resourceId));
    }

}
