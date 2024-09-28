package com.application.elerna.service.impl;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.request.AddLessonRequest;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.Assignment;
import com.application.elerna.model.Content;
import com.application.elerna.model.Contest;
import com.application.elerna.model.Lesson;
import com.application.elerna.repository.*;
import com.application.elerna.service.CourseResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseResourceServiceImpl implements CourseResourceService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final ContentRepository contentRepository;
    private final AssignmentRepository assignmentRepository;
    private final ContestRepository contestRepository;

    @Value("${course.lessonFolder}")
    private String lessonFolder;

    @Value("${course.assignmentFolder}")
    private String assignmentFolder;

    @Value("${course.contestFolder}")
    private String contestFolder;

    @Override
    public String addLesson(String name, Long courseId, MultipartFile file) {

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFound("Course doesnt exist in database, courseId: " + courseId);
        }

        var lesson = lessonRepository.findByName(name);

        if (!lesson.isEmpty()) {
            throw new InvalidRequestData("Lesson was existed");
        }

        try {

            String fileName = file.getOriginalFilename();

            String filePath = lessonFolder + "\\" + "Course" + courseId + "_" + fileName;

            log.info("filePath: " + filePath);

            File destFile = new File(filePath);

            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            file.transferTo(destFile);

            Optional<Content> content = contentRepository.findByName(name);

            if (!content.isEmpty()) {
                throw new InvalidRequestData("Resource has been error, content is existed");
            }

            Content newContent = Content.builder()
                    .name(fileName)
                    .path(filePath)
                    .build();

            contentRepository.save(newContent);

            Lesson newLesson = Lesson.builder()
                    .course(course.get())
                    .name(name)
                    .content(newContent)
                    .build();

            newContent.setLesson(newLesson);

            lessonRepository.save(newLesson);
            contentRepository.save(newContent);

//            course.get().addLesson(newLesson);
//            courseRepository.save(course.get());

            return "Upload Lesson Successfully";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addAssignment(String name, Long courseId, Date startDate, Date endDate, MultipartFile file) {

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFound("Course doesnt exist in database, courseId: " + courseId);
        }

        var assignment = assignmentRepository.findByName(name);

        if (!assignment.isEmpty()) {
            throw new InvalidRequestData("Assignment was existed");
        }

        try {

            String fileName = file.getOriginalFilename();

            String filePath = assignmentFolder + "\\" + "Course" + courseId + "_" + fileName;

            log.info("filePath: " + filePath);

            File destFile = new File(filePath);

            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            file.transferTo(destFile);

            Optional<Content> content = contentRepository.findByName(name);

            if (!content.isEmpty()) {
                throw new InvalidRequestData("Resource has been error, content is existed");
            }

            Content newContent = Content.builder()
                    .name(fileName)
                    .path(filePath)
                    .build();

            contentRepository.save(newContent);

            Assignment newAssignment = Assignment.builder()
                    .course(course.get())
                    .name(name)
                    .startDate(startDate)
                    .endDate(endDate)
                    .content(newContent)
                    .build();

            newContent.setAssignment(newAssignment);

            assignmentRepository.save(newAssignment);
            contentRepository.save(newContent);

//            course.get().addLesson(newLesson);
//            courseRepository.save(course.get());

            return "Upload Assignment Successfully";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addContest(String name, Long courseId, Date startDate, Date endDate, Time duration, MultipartFile file) {
        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFound("Course doesnt exist in database, courseId: " + courseId);
        }

        var contest = contestRepository.findByName(name);

        if (!contest.isEmpty()) {
            throw new InvalidRequestData("Contest was existed");
        }

        try {

            String fileName = file.getOriginalFilename();

            String filePath = contestFolder + "\\" + "Course" + courseId + "_" + fileName;

            log.info("filePath: " + filePath);

            File destFile = new File(filePath);

            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            file.transferTo(destFile);

            Optional<Content> content = contentRepository.findByName(name);

            if (!content.isEmpty()) {
                throw new InvalidRequestData("Resource has been error, content is existed");
            }

            Content newContent = Content.builder()
                    .name(fileName)
                    .path(filePath)
                    .build();

            contentRepository.save(newContent);

            Contest newContest = Contest.builder()
                    .course(course.get())
                    .name(name)
                    .startDate(startDate)
                    .endDate(endDate)
                    .duration(duration)
                    .content(newContent)
                    .build();

            newContent.setContest(newContest);

            contestRepository.save(newContest);
            contentRepository.save(newContent);

//            course.get().addLesson(newLesson);
//            courseRepository.save(course.get());

            return "Upload Contest Successfully";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
