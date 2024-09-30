package com.application.elerna.service;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.request.UpdateCourseRequest;
import com.application.elerna.dto.response.CourseResponse;
import com.application.elerna.dto.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface CourseService {

    String addCourseRequest(AddCourseRequest request);

    @PreAuthorize("hasPermission(-1, 'course', 'all')")
    PageResponse<?> getAllCourseRequest(Integer pageNo, Integer pageSize);

    @PreAuthorize("hasPermission(-1, 'course', 'all')")
    String approveCourseRequest(Long requestId);

    @PreAuthorize("hasPermission(-1, 'course', 'all')")
    String rejectCourseRequest(Long requestId);

    PageResponse<?> getAllCourseBySearch(Integer pageNo, Integer pageSize, String... searchBy);

    CourseResponse getCourseDetail(Long courseId);

    String registerCourse(Long courseId);

    PageResponse<?> getAllRegisteredCourse(Integer pageNo, Integer pageSize);

    @PreAuthorize("hasPermission(#request.courseId, 'course', 'update')")
    String updateCourse(UpdateCourseRequest request);

    @PreAuthorize("hasPermission(-1, 'course', 'all')")
    PageResponse<?> getAllStudentList(Long courseId, Integer pageNo, Integer pageSize);

    String registerTeamCourse(Long teamId, Long courseId);

    String unregisterCourse(Long userId, Long courseId);

    String unregisterTeamCourse(Long teamId, Long courseId);

    @PreAuthorize("hasPermission(#courseId, 'course', 'delete')")
    String deleteCourse(Long courseId);

    String sendRegisterRequest(Long courseId);
}
