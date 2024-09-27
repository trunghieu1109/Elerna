package com.application.elerna.service;

import com.application.elerna.dto.request.AddCourseRequest;
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


}
