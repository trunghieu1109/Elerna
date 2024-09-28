package com.application.elerna.service;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.request.AddLessonRequest;
import com.application.elerna.dto.response.CourseResourceResponse;
import com.application.elerna.dto.response.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;

@Service
public interface CourseResourceService {

    public String addLesson(String name, Long courseId, MultipartFile file);

    public String addAssignment(String name, Long courseId, Date startDate, Date endDate, MultipartFile file);

    public String addContest(String name, Long courseId, Date startDate, Date endDate, Time duration, MultipartFile file);

    public PageResponse<?> getAllLessonOfCourse(Long courseId, Integer pageNo, Integer pageSize);

    public byte[] download(String path) throws IOException;

    public CourseResourceResponse getLessonDetail(Long resourceId);

}
