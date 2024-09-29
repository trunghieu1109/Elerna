package com.application.elerna.service.impl;

import com.application.elerna.dto.response.CourseResourceResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.*;
import com.application.elerna.repository.*;
import com.application.elerna.service.CourseResourceService;
import com.application.elerna.service.PrivilegeService;
import com.application.elerna.service.RoleService;
import com.application.elerna.utils.CustomizedMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseResourceServiceImpl implements CourseResourceService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final ContentRepository contentRepository;
    private final AssignmentRepository assignmentRepository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final ContestSubmissionRepository contestSubmissionRepository;
    private final PrivilegeService privilegeService;
    private final RoleService roleService;

    @Value("${course.lessonFolder}")
    private String lessonFolder;

    @Value("${course.assignmentFolder}")
    private String assignmentFolder;

    @Value("${course.contestFolder}")
    private String contestFolder;

    @Value("${course.submissionFolder}")
    private String submissionFolder;

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

    @Override
    public PageResponse<?> getAllResourceOfCourse(Long courseId, String resourceType, Integer pageNo, Integer pageSize) {

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new InvalidRequestData("Course doesnot exist in database");
        }

        switch (resourceType) {
            case "lesson":
                return getLessonList(course.get(), pageNo, pageSize);
            case "assignment":
                return getAssignmentList(course.get(), pageNo, pageSize);
            case "contest":
                return getContestList(course.get(), pageNo, pageSize);
            case "assignment-submission":
                return getAssignmentSubmissionList(course.get(), pageNo, pageSize);
            case "contest-submission":
                return getContestSubmissionList(course.get(), pageNo, pageSize);
            default:
                return null;
        }

    }

    private PageResponse<?> getLessonList(Course course, Integer pageNo, Integer pageSize) {
        List<Lesson> lessons = course.getLessons().stream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(lessons.size() * 1.0 / pageSize))
                .data(lessons.stream().map(lesson -> getLessonDetail(lesson.getId())))
                .build();
    }

    private PageResponse<?> getAssignmentList(Course course, Integer pageNo, Integer pageSize) {
        List<Assignment> assignments = course.getAssignments().stream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(assignments.size() * 1.0 / pageSize))
                .data(assignments.stream().map(assignment -> getAssignmentDetail(assignment.getId())))
                .build();
    }

    private PageResponse<?> getContestList(Course course, Integer pageNo, Integer pageSize) {
        List<Contest> contests = course.getContests().stream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(contests.size() * 1.0 / pageSize))
                .data(contests.stream().map(contest -> getContestDetail(contest.getId())))
                .build();
    }

    private PageResponse<?> getAssignmentSubmissionList(Course course, Integer pageNo, Integer pageSize) {
        List<AssignmentSubmission> assignmentSubmissions = course.getAssignmentSubmissions().stream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(assignmentSubmissions.size() * 1.0 / pageSize))
                .data(assignmentSubmissions.stream().map(assignmentSubmission -> getAssignmentSubmissionDetail(assignmentSubmission.getId())))
                .build();
    }

    @Override
    public PageResponse<?> getAssignmentSubmissionListFromAssignment(Long assignmentId, Integer pageNo, Integer pageSize) {

        var currentAssignment = assignmentRepository.findById(assignmentId);

        if (currentAssignment.isEmpty()) {
            throw new ResourceNotFound("Assignment not found");
        }

        Assignment assignment = currentAssignment.get();

        List<AssignmentSubmission> assignmentSubmissions = assignment.getAssignmentSubmissions().stream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(assignmentSubmissions.size() * 1.0 / pageSize))
                .data(assignmentSubmissions.stream().map(assignmentSubmission -> getAssignmentSubmissionDetail(assignmentSubmission.getId())))
                .build();
    }

    @Override
    public PageResponse<?> getContestSubmissionListFromContest(Long contestId, Integer pageNo, Integer pageSize) {

        var currentContest = contestRepository.findById(contestId);

        if (currentContest.isEmpty()) {
            throw new ResourceNotFound("Contest not found");
        }

        Contest contest = currentContest.get();

        List<ContestSubmission> contestSubmissions = contest.getContestSubmissions().stream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(contestSubmissions.size() * 1.0 / pageSize))
                .data(contestSubmissions.stream().map(contestSubmission -> getContestSubmissionDetail(contestSubmission.getId())))
                .build();
    }

    @Override
    public String deleteCourseResource(String resourceType, Long resourceId) {

        switch (resourceType) {
            case "lesson":
                return deleteLesson(resourceId);
            case "assignment":
                return deleteAssignment(resourceId);
            case "contest":
                return deleteContest(resourceId);
            default:
                return "";
        }
    }

    private String deleteLesson(Long resourceId) {

        var currentLesson = lessonRepository.findById(resourceId);

        if (currentLesson.isEmpty()) {
            throw new ResourceNotFound("Lesson not found, id = " + resourceId);
        }

        Lesson lesson = currentLesson.get();

        Course course = lesson.getCourse();

        if (course == null) {
            throw new ResourceNotFound("Lesson not matched with any courses");
        }

        if (course.getLessons().contains(lesson)) {
            course.getLessons().remove(lesson);
        }

        lesson.setCourse(null);

        lessonRepository.save(lesson);
        courseRepository.save(course);

        return "Delete lesson from course successfully";
    }

    private String deleteAssignment(Long resourceId) {

        var currentAssignment = assignmentRepository.findById(resourceId);

        if (currentAssignment.isEmpty()) {
            throw new ResourceNotFound("Assignment not found, id = " + resourceId);
        }

        Assignment assignment = currentAssignment.get();

        Course course = assignment.getCourse();

        if (course == null) {
            throw new ResourceNotFound("Assignment not matched with any courses");
        }

        if (course.getAssignments().contains(assignment)) {
            course.getAssignments().remove(assignment);
        }

        assignment.setCourse(null);

        assignmentRepository.save(assignment);
        courseRepository.save(course);

        return "Delete assignment from course successfully";
    }

    private String deleteContest(Long resourceId) {

        var currentContest = contestRepository.findById(resourceId);

        if (currentContest.isEmpty()) {
            throw new ResourceNotFound("Contest not found, id = " + resourceId);
        }

        Contest contest = currentContest.get();

        Course course = contest.getCourse();

        if (course == null) {
            throw new ResourceNotFound("Contest not matched with any courses");
        }

        if (course.getContests().contains(contest)) {
            course.getContests().remove(contest);
        }

        contest.setCourse(null);

        contestRepository.save(contest);
        courseRepository.save(course);

        return "Delete contest from course successfully";
    }

//    pri

    private PageResponse<?> getContestSubmissionList(Course course, Integer pageNo, Integer pageSize) {
        List<ContestSubmission> contestSubmissions = course.getContestSubmissions().stream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(contestSubmissions.size() * 1.0 / pageSize))
                .data(contestSubmissions.stream().map(contestSubmission -> getContestSubmissionDetail(contestSubmission.getId())))
                .build();
    }

    @Override
    public byte[] download(String path, String resourceType) throws IOException {

        switch (resourceType) {
            case "lesson":
                return Files.readAllBytes(Path.of(lessonFolder + "\\" + path));
            case "assignment":
                return Files.readAllBytes(Path.of(assignmentFolder + "\\" + path));
            case "contest":
                return Files.readAllBytes(Path.of(contestFolder + "\\" + path));
            case "assignment-submission":
                return Files.readAllBytes(Path.of(submissionFolder + "\\" + path));
            case "contest-submission":
                return Files.readAllBytes(Path.of(submissionFolder + "\\" + path));
            default:
                return null;
        }

    }

    @Override
    public CourseResourceResponse getResourceDetail(Long resourceId, String resourceType) {
        switch (resourceType) {
            case "lesson":
                return getLessonDetail(resourceId);
            case "assignment":
                return getAssignmentDetail(resourceId);
            case "contest":
                return getContestDetail(resourceId);
            case "assignment-submission":
                return getAssignmentSubmissionDetail(resourceId);
            case "contest-submission":
                return getContestSubmissionDetail(resourceId);
            default:
                return null;
        }

    }

    @Override
    public String updateLesson(Long resourceId, String name, Long courseId, MultipartFile file) {
        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFound("Course doesnt exist in database, courseId: " + courseId);
        }

        var lesson = lessonRepository.findById(resourceId);

        if (lesson.isEmpty()) {
            throw new InvalidRequestData("Lesson was not existed");
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

            Lesson newLesson = lesson.get();
            newLesson.setName(name);
            newLesson.setContent(newContent);

            newContent.setLesson(newLesson);

            lessonRepository.save(newLesson);
            contentRepository.save(newContent);

//            course.get().addLesson(newLesson);
//            courseRepository.save(course.get());

            return "Update Lesson Successfully";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String updateAssignment(Long resourceId, String name, Long courseId, Date startDate, Date endDate, MultipartFile file) {
        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFound("Course doesnt exist in database, courseId: " + courseId);
        }

        var assignment = assignmentRepository.findById(resourceId);

        if (assignment.isEmpty()) {
            throw new InvalidRequestData("Assignment was not existed");
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

            Assignment newAssignment = assignment.get();

            newAssignment.setName(name);
            newAssignment.setStartDate(startDate);
            newAssignment.setEndDate(endDate);
            newAssignment.setContent(newContent);

            newContent.setAssignment(newAssignment);

            assignmentRepository.save(newAssignment);
            contentRepository.save(newContent);

//            course.get().addLesson(newLesson);
//            courseRepository.save(course.get());

            return "Update Assignment Successfully";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String updateContest(Long resourceId, String name, Long courseId, Date startDate, Date endDate, Time duration, MultipartFile file) {
        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFound("Course doesnt exist in database, courseId: " + courseId);
        }

        var contest = contestRepository.findById(resourceId);

        if (contest.isEmpty()) {
            throw new InvalidRequestData("Contest was not existed");
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

            Contest newContest = contest.get();

            newContest.setCourse(course.get());
            newContest.setName(name);
            newContest.setStartDate(startDate);
            newContest.setEndDate(endDate);
            newContest.setDuration(duration);
            newContest.setContent(newContent);

            newContent.setContest(newContest);

            contestRepository.save(newContest);
            contentRepository.save(newContent);

//            course.get().addLesson(newLesson);
//            courseRepository.save(course.get());

            return "Update Contest Successfully";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String submit(Long userId, String targetType, Long targetId, MultipartFile file) {

        var user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new ResourceNotFound("user not existed");
        }

        if (targetType.equals("assignment")) {
            submitAssignment(user.get(), targetId, file);
            return "Submit assigment successfully";
        } else {
            submitContest(user.get(), targetId, file);
            return "Submit contest successfully";
        }
    }

    private void submitAssignment(User user, Long targetId, MultipartFile file) {
        var assignment = assignmentRepository.findById(targetId);

        if (assignment.isEmpty()) {
            throw new ResourceNotFound("Assignment not existed");
        }

        Long subSize = (long) assignment.get().getAssignmentSubmissions().size();

        String submissionName = "Assignment" + targetId + "_Submission" + (subSize + 1) + "_" + file.getOriginalFilename();

        try {

            String fileName = submissionName;

            String filePath = submissionFolder + "\\" + fileName;

            log.info("filePath: " + filePath);

            File destFile = new File(filePath);

            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            file.transferTo(destFile);

            Optional<Content> content = contentRepository.findByName(submissionName);

            if (!content.isEmpty()) {
                throw new InvalidRequestData("Resource has been error, content is existed");
            }

            Content newContent = Content.builder()
                    .name(submissionName)
                    .path(filePath)
                    .build();

            contentRepository.save(newContent);

            AssignmentSubmission newAssignSub = AssignmentSubmission.builder()
                    .name(submissionName)
                    .assignment(assignment.get())
                    .user(user)
                    .course(assignment.get().getCourse())
                    .content(newContent)
                    .build();

            assignment.get().getCourse().addAssignmentSubmission(newAssignSub);
            assignment.get().addAssignmentSubmission(newAssignSub);
            user.addAssigmentSubmission(newAssignSub);

            newContent.setAssignmentSubmission(newAssignSub);

            assignmentSubmissionRepository.save(newAssignSub);
            contentRepository.save(newContent);

            var curAssSub = assignmentSubmissionRepository.findByName(submissionName);

            addSubmissionRole(user, curAssSub.get().getId());

            assignmentRepository.save(assignment.get());
            courseRepository.save(assignment.get().getCourse());
            userRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void submitContest(User user, Long targetId, MultipartFile file) {
        var contest = contestRepository.findById(targetId);

        if (contest.isEmpty()) {
            throw new ResourceNotFound("Contest not existed");
        }

        Long subSize = (long) contest.get().getContestSubmissions().size();

        String submissionName = "Contest" + targetId + "_Submission" + (subSize + 1) + "_" + file.getOriginalFilename();

        try {

            String fileName = submissionName;

            String filePath = submissionFolder + "\\" + fileName;

            log.info("filePath: " + filePath);

            File destFile = new File(filePath);

            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            file.transferTo(destFile);

            Optional<Content> content = contentRepository.findByName(submissionName);

            if (!content.isEmpty()) {
                throw new InvalidRequestData("Resource has been error, content is existed");
            }

            Content newContent = Content.builder()
                    .name(submissionName)
                    .path(filePath)
                    .build();

            contentRepository.save(newContent);

            ContestSubmission newContestSub = ContestSubmission.builder()
                    .name(submissionName)
                    .contest(contest.get())
                    .user(user)
                    .course(contest.get().getCourse())
                    .content(newContent)
                    .build();

            user.addContestSubmission(newContestSub);
            contest.get().addContestSubmission(newContestSub);
            contest.get().getCourse().addContestSubmission(newContestSub);

            newContent.setContestSubmission(newContestSub);

            contestSubmissionRepository.save(newContestSub);
            contentRepository.save(newContent);

            var curContestSub = contestSubmissionRepository.findByName(submissionName);

            addSubmissionRole(user, curContestSub.get().getId());

            contestRepository.save(contest.get());
            courseRepository.save(contest.get().getCourse());
            userRepository.save(user);
//            course.get().addLesson(newLesson);
//            courseRepository.save(course.get());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private CourseResourceResponse getLessonDetail(Long lessonId) {
        var currentLesson = lessonRepository.findById(lessonId);

        if (currentLesson.isEmpty()) {

            throw new ResourceNotFound("Lesson not found");
        }

        Lesson lesson = currentLesson.get();

        return CourseResourceResponse.builder()
                .name(lesson.getName())
                .resourceId(lesson.getId())
                .resourceType("Lesson")
                .courseId(lesson.getCourse().getId())
                .content(lesson.getContent().getPath())
                .build();
    }

    private CourseResourceResponse getAssignmentDetail(Long assignmentId) {
        var currentAssignment = assignmentRepository.findById(assignmentId);

        if (currentAssignment.isEmpty()) {

            throw new ResourceNotFound("Assignment not found");
        }

        Assignment assignment = currentAssignment.get();

        return CourseResourceResponse.builder()
                .name(assignment.getName())
                .resourceId(assignment.getId())
                .resourceType("Assignment")
                .courseId(assignment.getCourse().getId())
                .content(assignment.getContent().getPath())
                .startDate(assignment.getStartDate())
                .endDate(assignment.getEndDate())
                .build();
    }

    private CourseResourceResponse getContestDetail(Long contestId) {
        var currentContest = contestRepository.findById(contestId);

        if (currentContest.isEmpty()) {

            throw new ResourceNotFound("Contest not found");
        }

        Contest contest = currentContest.get();

        return CourseResourceResponse.builder()
                .name(contest.getName())
                .resourceId(contest.getId())
                .resourceType("Contest")
                .courseId(contest.getCourse().getId())
                .content(contest.getContent().getPath())
                .startDate(contest.getStartDate())
                .endDate(contest.getEndDate())
                .duration(contest.getDuration())
                .build();
    }

    private CourseResourceResponse getAssignmentSubmissionDetail(Long submissionId) {
        var currentSubmission = assignmentSubmissionRepository.findById(submissionId);

        if (currentSubmission.isEmpty()) {

            throw new ResourceNotFound("Assignment submission not found");
        }

        AssignmentSubmission assignmentSubmission = currentSubmission.get();

        return CourseResourceResponse.builder()
                .name(assignmentSubmission.getContent().getName())
                .resourceId(assignmentSubmission.getId())
                .resourceType("Assignment Submission")
                .courseId(assignmentSubmission.getCourse().getId())
                .content(assignmentSubmission.getContent().getPath())
                .build();
    }

    private CourseResourceResponse getContestSubmissionDetail(Long submissionId) {
        var currentSubmission = contestSubmissionRepository.findById(submissionId);

        if (currentSubmission.isEmpty()) {

            throw new ResourceNotFound("Contest submission not found");
        }

        ContestSubmission contestSubmission = currentSubmission.get();

        return CourseResourceResponse.builder()
                .name(contestSubmission.getContent().getName())
                .resourceId(contestSubmission.getId())
                .resourceType("Contest Submission")
                .courseId(contestSubmission.getCourse().getId())
                .content(contestSubmission.getContent().getPath())
                .build();
    }

    private MultipartFile convertPathToFile(String path) {

        return new CustomizedMultipartFile(path);
    }

    private void addSubmissionRole(User user, Long submissionId) {
        Privilege priUpdate = privilegeService.createPrivilege("submission", submissionId, "update", "Update course, id = " + submissionId);
        Privilege priDelete = privilegeService.createPrivilege("submission", submissionId, "delete", "Delete course, id = " + submissionId);
        Privilege priView = privilegeService.createPrivilege("submission", submissionId, "view", "View course, id = " + submissionId);

        Role roleAdmin = roleService.createRole("ADMIN", "submission", submissionId);

        roleAdmin.addPrivilege(priDelete);
        roleAdmin.addPrivilege(priView);
        roleAdmin.addPrivilege(priUpdate);

        priDelete.addRole(roleAdmin);
        priUpdate.addRole(roleAdmin);
        priView.addRole(roleAdmin);

        privilegeService.savePrivilege(priDelete);
        privilegeService.savePrivilege(priUpdate);
        privilegeService.savePrivilege(priView);

        roleService.saveRole(roleAdmin);

        user.addRole(roleAdmin);
        roleAdmin.addUser(user);

        roleService.saveRole(roleAdmin);
        userRepository.save(user);


    }
}
