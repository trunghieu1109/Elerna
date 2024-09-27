package com.application.elerna.service;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.request.UpdateCourseRequest;
import com.application.elerna.dto.response.CourseResponse;
import com.application.elerna.dto.response.PageResponse;
import org.springframework.stereotype.Service;

@Service
public interface CourseService {

    public String addCourseRequest(AddCourseRequest request);

    public PageResponse<?> getAllCourseRequest(Integer pageNo, Integer pageSize);

    public String approveCourseRequest(Long requestId);

    public String rejectCourseRequest(Long requestId);

    public PageResponse<?> getAllCourseBySearch(Integer pageNo, Integer pageSize, String... searchBy);

    public CourseResponse getCourseDetail(Long courseId);

    public String registerCourse(Long userId, Long courseId);

    public PageResponse<?> getAllRegisteredCourse(Long userId, Integer pageNo, Integer pageSize);

    public String updateCourse(UpdateCourseRequest request);

    public PageResponse<?> getAllStudentList(Long courseId, Integer pageNo, Integer pageSize);


}
