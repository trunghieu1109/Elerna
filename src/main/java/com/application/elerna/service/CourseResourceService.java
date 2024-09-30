package com.application.elerna.service;

import com.application.elerna.dto.response.CourseResourceResponse;
import com.application.elerna.dto.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;

@Service
public interface CourseResourceService {

    @PreAuthorize("hasPermission(#courseId, 'course', 'update')")
    String addLesson(String name, Long courseId, MultipartFile file);

    @PreAuthorize("hasPermission(#courseId, 'course', 'update')")
    String addAssignment(String name, Long courseId, Date startDate, Date endDate, MultipartFile file);

    @PreAuthorize("hasPermission(#courseId, 'course', 'update')")
    String addContest(String name, Long courseId, Date startDate, Date endDate, Time duration, MultipartFile file);

    @PreAuthorize("hasPermission(#courseId, 'course', 'view')")
    PageResponse<?> getAllResourceOfCourse(Long courseId, String resourceType, Integer pageNo, Integer pageSize);

    byte[] download(String path, String resourceType) throws IOException;

    @PreAuthorize("hasPermission(#courseId, 'course', 'view')")
    CourseResourceResponse getResourceDetail(Long resourceId, String resourceType);

    @PreAuthorize("hasPermission(#courseId, 'course', 'update')")
    String updateLesson(Long resourceId, String name, Long courseId, MultipartFile file);

    @PreAuthorize("hasPermission(#courseId, 'course', 'update')")
    String updateAssignment(Long resourceId, String name, Long courseId, Date startDate, Date endDate, MultipartFile file);

    @PreAuthorize("hasPermission(#courseId, 'course', 'update')")
    String updateContest(Long resourceId, String name, Long courseId, Date startDate, Date endDate, Time duration, MultipartFile file);

    String submit(String targetType, Long targetId, MultipartFile file);

    PageResponse<?> getAssignmentSubmissionListFromAssignment(Long assignmentId, Integer pageNo, Integer pageSize);

    PageResponse<?> getContestSubmissionListFromContest(Long ContestId, Integer pageNo, Integer pageSize);

    @PreAuthorize("hasPermission(#courseId, 'course', 'delete')")
    String deleteCourseResource(String resourceType, Long resourceId);

}
