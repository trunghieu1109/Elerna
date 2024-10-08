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
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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
    private final UserService userService;

    @Value("${course.lessonFolder}")
    private String lessonFolder;

    @Value("${course.assignmentFolder}")
    private String assignmentFolder;

    @Value("${course.contestFolder}")
    private String contestFolder;

    @Value("${course.submissionFolder}")
    private String submissionFolder;

    /**
     *
     * Add lesson to course
     *
     * @param name String
     * @param courseId Long
     * @param file MultipartFile
     * @return String
     */
    @Override
    public String addLesson(String name, Long courseId, MultipartFile file) {

        // get course by id
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new ResourceNotFound("Course does not exist in database, courseId: " + courseId);
        }

        // get lesson by name
        var lesson = lessonRepository.findByName(name);

        if (lesson.isPresent()) {
            throw new InvalidRequestData("Lesson was existed");
        }

        // get file and copy to the destination folder
        String fileName = file.getOriginalFilename();

        String filePath = null;

        if (lessonFolder.contains("\\")) {
            filePath = lessonFolder + "\\" + "Course" + courseId + "_" + fileName;
        } else {
            filePath = lessonFolder + "/" + "Course" + courseId + "_" + fileName;
        }

        CompletableFuture<Boolean> isSuccess = saveFile(filePath, file);

        // create new content
        Optional<Content> content = contentRepository.findByName(name);

        if (content.isPresent()) {
            throw new InvalidRequestData("Resource has been error, content is existed");
        }

        Content newContent = Content.builder()
                .name(fileName)
                .path(filePath)
                .build();

        contentRepository.save(newContent);

        // create new lesson
        Lesson newLesson = Lesson.builder()
                .course(course.get())
                .name(name)
                .content(newContent)
                .build();

        newContent.setLesson(newLesson);

        lessonRepository.save(newLesson);
        contentRepository.save(newContent);

        isSuccess.join();

        return "Upload Lesson Successfully, name: " + newLesson.getName();
    }

    /**
     *
     * Add assignment to course
     *
     * @param name String
     * @param courseId Long
     * @param startDate Date
     * @param endDate Date
     * @param file MultipartFile
     * @return String
     */
    @Override
    public String addAssignment(String name, Long courseId, Date startDate, Date endDate, MultipartFile file) {

        // find course
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new ResourceNotFound("Course does not exist in database, courseId: " + courseId);
        }

        // find assignment
        var assignment = assignmentRepository.findByName(name);

        if (assignment.isPresent()) {
            throw new InvalidRequestData("Assignment was existed");
        }

        // copy file to destination folder
        String fileName = file.getOriginalFilename();

        String filePath = null;

        if (assignmentFolder.contains("\\")) {
            filePath = assignmentFolder + "\\" + "Course" + courseId + "_" + fileName;
        } else {
            filePath = assignmentFolder + "/" + "Course" + courseId + "_" + fileName;
        }

        CompletableFuture<Boolean> isSuccess = saveFile(filePath, file);

        // create new content
        Optional<Content> content = contentRepository.findByName(name);

        if (content.isPresent()) {
            throw new InvalidRequestData("Resource has been error, content is existed");
        }

        Content newContent = Content.builder()
                .name(fileName)
                .path(filePath)
                .build();

        contentRepository.save(newContent);

        // create new assignment
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

        isSuccess.join();

        return "Upload Assignment Successfully, name: " + newAssignment.getName();
    }

    /**
     *
     * Add contest to course
     *
     * @param name String
     * @param courseId Long
     * @param startDate Date
     * @param endDate Date
     * @param duration Time
     * @param file MultipartFile
     * @return String
     */
    @Override
    public String addContest(String name, Long courseId, Date startDate, Date endDate, Time duration, MultipartFile file) {

        // find course
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new ResourceNotFound("Course does not exist in database, courseId: " + courseId);
        }

        // find contest
        var contest = contestRepository.findByName(name);

        if (contest.isPresent()) {
            throw new InvalidRequestData("Contest was existed");
        }

        // copy file to destination folder
        String fileName = file.getOriginalFilename();

        String filePath = null;

        if (contestFolder.contains("\\")) {
            filePath = contestFolder + "\\" + "Course" + courseId + "_" + fileName;
        } else {
            filePath = contestFolder + "/" + "Course" + courseId + "_" + fileName;
        }

        CompletableFuture<Boolean> isSuccess = saveFile(filePath, file);

        // create new content
        Optional<Content> content = contentRepository.findByName(name);

        if (content.isPresent()) {
            throw new InvalidRequestData("Resource has been error, content is existed");
        }

        Content newContent = Content.builder()
                .name(fileName)
                .path(filePath)
                .build();

        contentRepository.save(newContent);

        // create new contest
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

        isSuccess.join();

        return "Upload Contest Successfully, name: " + newContest.getName();
    }

    /**
     *
     * Get all course's resources
     *
     * @param courseId Long
     * @param resourceType String
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getAllResourceOfCourse(Long courseId, String resourceType, Integer pageNo, Integer pageSize) {

        // find course
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new InvalidRequestData("Course does not exist in database");
        }

        // get resource list
        return switch (resourceType) {
            case "lesson" -> getLessonList(course.get(), pageNo, pageSize);
            case "assignment" -> getAssignmentList(course.get(), pageNo, pageSize);
            case "contest" -> getContestList(course.get(), pageNo, pageSize);
            case "assignment-submission" -> getAssignmentSubmissionList(course.get(), pageNo, pageSize);
            case "contest-submission" -> getContestSubmissionList(course.get(), pageNo, pageSize);
            default -> null;
        };

    }

    /**
     *
     * Get lesson list
     *
     * @param course Course
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Transactional(readOnly = true)
    private PageResponse<?> getLessonList(Course course, Integer pageNo, Integer pageSize) {
        List<Lesson> lessons = course.getLessons().parallelStream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(lessons.size() * 1.0 / pageSize))
                .data(lessons.parallelStream().map(lesson -> createLessonResponse(lesson, lesson.getCourse().getId())))
                .build();
    }

    /**
     *
     * Get assignment list
     *
     * @param course Course
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Transactional(readOnly = true)
    private PageResponse<?> getAssignmentList(Course course, Integer pageNo, Integer pageSize) {
        List<Assignment> assignments = course.getAssignments().parallelStream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(assignments.size() * 1.0 / pageSize))
                .data(assignments.parallelStream().map(assignment -> createAssignmentResponse(assignment, assignment.getCourse().getId())))
                .build();
    }

    /**
     *
     * Get contest list
     *
     * @param course Course
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Transactional(readOnly = true)
    private PageResponse<?> getContestList(Course course, Integer pageNo, Integer pageSize) {
        List<Contest> contests = course.getContests().parallelStream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(contests.size() * 1.0 / pageSize))
                .data(contests.parallelStream().map(contest -> createContestResponse(contest, contest.getCourse().getId())))
                .build();
    }

    /**
     *
     * Get assignment submission list
     *
     * @param course Course
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Transactional(readOnly = true)
    private PageResponse<?> getAssignmentSubmissionList(Course course, Integer pageNo, Integer pageSize) {
        List<AssignmentSubmission> assignmentSubmissions = course.getAssignmentSubmissions().parallelStream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(assignmentSubmissions.size() * 1.0 / pageSize))
                .data(assignmentSubmissions.parallelStream().map(assignmentSubmission -> createAssignmentSubmissionResponse(assignmentSubmission, assignmentSubmission.getCourse().getId())))
                .build();
    }

    /**
     *
     * Get submission from assignment
     *
     * @param assignmentId Long
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Transactional(readOnly = true)
    @Override
    public PageResponse<?> getAssignmentSubmissionListFromAssignment(Long assignmentId, Integer pageNo, Integer pageSize) {

        // get assignment
        var currentAssignment = assignmentRepository.findById(assignmentId);

        if (currentAssignment.isEmpty()) {
            throw new ResourceNotFound("Assignment not found");
        }

        Assignment assignment = currentAssignment.get();

        // get submission list
        List<AssignmentSubmission> assignmentSubmissions = assignment.getAssignmentSubmissions().parallelStream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(assignmentSubmissions.size() * 1.0 / pageSize))
                .data(assignmentSubmissions.parallelStream().map(assignmentSubmission -> createAssignmentSubmissionResponse(assignmentSubmission, assignmentSubmission.getCourse().getId())))
                .build();
    }

    /**
     *
     * Get submission from contest
     *
     * @param contestId Long
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponses
     */
    @Transactional(readOnly = true)
    @Override
    public PageResponse<?> getContestSubmissionListFromContest(Long contestId, Integer pageNo, Integer pageSize) {

        // get contest
        var currentContest = contestRepository.findById(contestId);

        if (currentContest.isEmpty()) {
            throw new ResourceNotFound("Contest not found");
        }

        Contest contest = currentContest.get();

        // get submission from contest
        List<ContestSubmission> contestSubmissions = contest.getContestSubmissions().parallelStream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(contestSubmissions.size() * 1.0 / pageSize))
                .data(contestSubmissions.parallelStream().map(contestSubmission -> createContestSubmissionResponse(contestSubmission, contestSubmission.getCourse().getId())))
                .build();
    }

    /**
     *
     * Delete course resource
     *
     * @param resourceType String
     * @param resourceId Long
     * @return String
     */
    @Override
    public String deleteCourseResource(String resourceType, Long resourceId) {

        return switch (resourceType) {
            case "lesson" -> deleteLesson(resourceId);
            case "assignment" -> deleteAssignment(resourceId);
            case "contest" -> deleteContest(resourceId);
            default -> "";
        };
    }

    /**
     *
     * Delete lesson
     *
     * @param resourceId Long
     * @return String
     */
    private String deleteLesson(Long resourceId) {

        // find lesson
        var currentLesson = lessonRepository.findById(resourceId);

        if (currentLesson.isEmpty()) {
            throw new ResourceNotFound("Lesson not found, id = " + resourceId);
        }

        Lesson lesson = currentLesson.get();

        Course course = lesson.getCourse();

        if (course == null || !course.isStatus()) {
            throw new ResourceNotFound("Lesson not matched with any courses");
        }

        // delete lesson
        course.getLessons().remove(lesson);

        lesson.setCourse(null);

        // save course and lesson
        lessonRepository.save(lesson);
        courseRepository.save(course);

        return "Delete " + lesson.getName() + " from course successfully";
    }

    /**
     *
     * Delete assignment
     *
     * @param resourceId Long
     * @return String
     */
    private String deleteAssignment(Long resourceId) {

        // find assignment
        var currentAssignment = assignmentRepository.findById(resourceId);

        if (currentAssignment.isEmpty()) {
            throw new ResourceNotFound("Assignment not found, id = " + resourceId);
        }

        Assignment assignment = currentAssignment.get();

        Course course = assignment.getCourse();

        if (course == null || !course.isStatus()) {
            throw new ResourceNotFound("Assignment not matched with any courses");
        }

        // delete assignment
        course.getAssignments().remove(assignment);

        assignment.setCourse(null);

        // save assignment and course
        assignmentRepository.save(assignment);
        courseRepository.save(course);

        return "Delete " + assignment.getName() + " from course successfully";
    }

    /**
     *
     * Delete contest
     *
     * @param resourceId Long
     * @return String
     */
    private String deleteContest(Long resourceId) {

        // find contest
        var currentContest = contestRepository.findById(resourceId);

        if (currentContest.isEmpty()) {
            throw new ResourceNotFound("Contest not found, id = " + resourceId);
        }

        Contest contest = currentContest.get();

        Course course = contest.getCourse();

        if (course == null || !course.isStatus()) {
            throw new ResourceNotFound("Contest not matched with any courses");
        }

        // delete contest
        course.getContests().remove(contest);

        contest.setCourse(null);

        // save course and contest
        contestRepository.save(contest);
        courseRepository.save(course);

        return "Delete " + contest.getName() + " from course successfully";
    }

    /**
     *
     * Get contest submission list
     *
     * @param course Course
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Transactional(readOnly = true)
    private PageResponse<?> getContestSubmissionList(Course course, Integer pageNo, Integer pageSize) {
        List<ContestSubmission> contestSubmissions = course.getContestSubmissions().parallelStream().toList();

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(contestSubmissions.size() * 1.0 / pageSize))
                .data(contestSubmissions.parallelStream().map(contestSubmission -> createContestSubmissionResponse(contestSubmission, contestSubmission.getCourse().getId())))
                .build();
    }

    /**
     *
     * Download resource's content from server
     *
     * @param path String
     * @param resourceType String
     * @return byte[]
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] download(String path, String resourceType) throws IOException {

        return switch (resourceType) {
            case "lesson" -> Files.readAllBytes(Path.of(lessonFolder + "\\" + path));
            case "assignment" -> Files.readAllBytes(Path.of(assignmentFolder + "\\" + path));
            case "contest" -> Files.readAllBytes(Path.of(contestFolder + "\\" + path));
            case "assignment-submission" -> Files.readAllBytes(Path.of(submissionFolder + "\\" + path));
            case "contest-submission" -> Files.readAllBytes(Path.of(submissionFolder + "\\" + path));
            default -> null;
        };

    }

    /**
     *
     * Get course resource's details
     *
     * @param resourceId Long
     * @param resourceType String
     * @return CourseResourceResponse
     */
    @Override
    @Transactional(readOnly = true)
    public CourseResourceResponse getResourceDetail(Long resourceId, String resourceType) {
        return switch (resourceType) {
            case "lesson" -> getLessonDetail(resourceId);
            case "assignment" -> getAssignmentDetail(resourceId);
            case "contest" -> getContestDetail(resourceId);
            case "assignment-submission" -> getAssignmentSubmissionDetail(resourceId);
            case "contest-submission" -> getContestSubmissionDetail(resourceId);
            default -> null;
        };

    }

    /**
     *
     * Update lesson details
     *
     * @param resourceId Long
     * @param name String
     * @param courseId Long
     * @param file MultipartFile
     * @return String
     */
    @Override
    public String updateLesson(Long resourceId, String name, Long courseId, MultipartFile file) {
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new ResourceNotFound("Course does not exist in database, courseId: " + courseId);
        }

        var lesson = lessonRepository.findById(resourceId);

        if (lesson.isEmpty()) {
            throw new InvalidRequestData("Lesson was not existed");
        }

        Lesson newLesson = lesson.get();
        newLesson.setName(name);

        if (file != null) {
            String fileName = file.getOriginalFilename();

            String filePath = null;

            if (lessonFolder.contains("\\")) {
                filePath = lessonFolder + "\\" + "Course" + courseId + "_" + fileName;
            } else {
                filePath = lessonFolder + "/" + "Course" + courseId + "_" + fileName;
            }

            CompletableFuture<Boolean> isSuccess = saveFile(filePath, file);

            Optional<Content> content = contentRepository.findByName(name);

            if (content.isPresent()) {
                throw new InvalidRequestData("Resource has been error, content is existed");
            }

            Content newContent = Content.builder()
                    .name(fileName)
                    .path(filePath)
                    .build();

            contentRepository.save(newContent);

            newLesson.setContent(newContent);

            newContent.setLesson(newLesson);
            contentRepository.save(newContent);
            isSuccess.join();
        }

        lessonRepository.save(newLesson);

        return "Update " + newLesson.getName() + " Successfully";

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
     * @return String
     */
    @Override
    public String updateAssignment(Long resourceId, String name, Long courseId, Date startDate, Date endDate, MultipartFile file) {
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new ResourceNotFound("Course does not exist in database, courseId: " + courseId);
        }

        var assignment = assignmentRepository.findById(resourceId);

        if (assignment.isEmpty()) {
            throw new InvalidRequestData("Assignment was not existed");
        }

        Assignment newAssignment = assignment.get();

        newAssignment.setName(name);
        newAssignment.setStartDate(startDate);
        newAssignment.setEndDate(endDate);

        if (file != null ) {
            String fileName = file.getOriginalFilename();

            String filePath = null;

            if (assignmentFolder.contains("\\")) {
                filePath = assignmentFolder + "\\" + "Course" + courseId + "_" + fileName;
            } else {
                filePath = assignmentFolder + "/" + "Course" + courseId + "_" + fileName;
            }

            CompletableFuture<Boolean> isSuccess = saveFile(filePath, file);

            Optional<Content> content = contentRepository.findByName(name);

            if (content.isPresent()) {
                throw new InvalidRequestData("Resource has been error, content is existed");
            }

            Content newContent = Content.builder()
                    .name(fileName)
                    .path(filePath)
                    .build();

            contentRepository.save(newContent);


            newAssignment.setContent(newContent);

            newContent.setAssignment(newAssignment);
            contentRepository.save(newContent);
            isSuccess.join();
        }

        assignmentRepository.save(newAssignment);

        return "Update " + newAssignment.getName() + " Successfully";

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
     * @param duration Time
     * @param file MultipartFile
     * @return String
     */
    @Override
    public String updateContest(Long resourceId, String name, Long courseId, Date startDate, Date endDate, Time duration, MultipartFile file) {
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new ResourceNotFound("Course does not exist in database, courseId: " + courseId);
        }

        var contest = contestRepository.findById(resourceId);

        if (contest.isEmpty()) {
            throw new InvalidRequestData("Contest was not existed");
        }

        Contest newContest = contest.get();

        newContest.setCourse(course.get());
        newContest.setName(name);
        newContest.setStartDate(startDate);
        newContest.setEndDate(endDate);
        newContest.setDuration(duration);

        if (file != null) {
            String fileName = file.getOriginalFilename();

            String filePath = null;

            if (contestFolder.contains("\\")) {
                filePath = contestFolder + "\\" + "Course" + courseId + "_" + fileName;
            } else {
                filePath = contestFolder + "/" + "Course" + courseId + "_" + fileName;
            }

            CompletableFuture<Boolean> isSuccess = saveFile(filePath, file);

            Optional<Content> content = contentRepository.findByName(name);

            if (content.isPresent()) {
                throw new InvalidRequestData("Resource has been error, content is existed");
            }

            Content newContent = Content.builder()
                    .name(fileName)
                    .path(filePath)
                    .build();

            contentRepository.save(newContent);


            newContest.setContent(newContent);

            newContent.setContest(newContest);
            contentRepository.save(newContent);

            isSuccess.join();
        }

        contestRepository.save(newContest);

        return "Update " + newContest.getName() + " Successfully";

    }

    /**
     *
     * Submit file to server
     *
     * @param targetType String
     * @param targetId Long
     * @param file MultipartFile
     * @return String
     */
    @Override
    public String submit(String targetType, Long targetId, MultipartFile file) {

        User user = userService.getUserFromAuthentication();

        if (targetType.equals("assignment")) {
            submitAssignment(user, targetId, file);
            return "Submit assignment " + targetId + " successfully";
        } else {
            submitContest(user, targetId, file);
            return "Submit contest " + targetId + " successfully";
        }
    }

    /**
     *
     * Submit for assignment
     *
     * @param user User
     * @param targetId Long
     * @param file MultipartFile
     */
    private void submitAssignment(User user, Long targetId, MultipartFile file) {
        var assignment = assignmentRepository.findById(targetId);

        if (assignment.isEmpty()) {
            throw new ResourceNotFound("Assignment not existed");
        }

        long subSize = assignment.get().getAssignmentSubmissions().size();

        String submissionName = "Assignment" + targetId + "_Submission" + (subSize + 1) + "_" + file.getOriginalFilename();

        String filePath = null;

        if (submissionFolder.contains("\\")) {
            filePath = submissionFolder + "\\" + submissionName;
        } else {
            filePath = submissionFolder + "/" + submissionName;
        }

        CompletableFuture<Boolean> isSuccess = saveFile(filePath, file);

        Optional<Content> content = contentRepository.findByName(submissionName);

        if (content.isPresent()) {
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
        user.addAssignmentSubmission(newAssignSub);

        newContent.setAssignmentSubmission(newAssignSub);

        assignmentSubmissionRepository.save(newAssignSub);
        contentRepository.save(newContent);

        var curAssSub = assignmentSubmissionRepository.findByName(submissionName);

        if (curAssSub.isEmpty()) {
            throw new ResourceNotFound("Cant get assignment submission from database");
        }

        addSubmissionRole(user, curAssSub.get().getId());

        assignmentRepository.save(assignment.get());
        courseRepository.save(assignment.get().getCourse());
        userRepository.save(user);

        isSuccess.join();

    }

    /**
     *
     * Submit for contest
     *
     * @param user User
     * @param targetId Long
     * @param file MultipartFile
     */
    private void submitContest(User user, Long targetId, MultipartFile file) {
        var contest = contestRepository.findById(targetId);

        if (contest.isEmpty()) {
            throw new ResourceNotFound("Contest not existed");
        }

        long subSize = contest.get().getContestSubmissions().size();

        String submissionName = "Contest" + targetId + "_Submission" + (subSize + 1) + "_" + file.getOriginalFilename();


        String filePath = null;

        if (submissionFolder.contains("\\")) {
            filePath = submissionFolder + "\\" + submissionName;
        } else {
            filePath = submissionFolder + "/" + submissionName;
        }

        CompletableFuture<Boolean> isSuccess = saveFile(filePath, file);

        Optional<Content> content = contentRepository.findByName(submissionName);

        if (content.isPresent()) {
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

        if (curContestSub.isEmpty()) {
            throw new ResourceNotFound("Contest submission not found");
        }

        addSubmissionRole(user, curContestSub.get().getId());

        contestRepository.save(contest.get());
        courseRepository.save(contest.get().getCourse());
        userRepository.save(user);

        isSuccess.join();

    }

    /**
     *
     * Get lesson details
     *
     * @param lessonId Long
     * @return CourseResourceResponse
     */
    @Transactional(readOnly = true)
    private CourseResourceResponse getLessonDetail(Long lessonId) {
        var currentLesson = lessonRepository.findById(lessonId);

        if (currentLesson.isEmpty()) {

            throw new ResourceNotFound("Lesson not found");
        }

        Lesson lesson = currentLesson.get();

        return createLessonResponse(lesson, lesson.getCourse().getId());
    }

    /**
     *
     * Get assignment details
     *
     * @param assignmentId Long
     * @return CourseResourceResponse
     */
    @Transactional(readOnly = true)
    private CourseResourceResponse getAssignmentDetail(Long assignmentId) {
        var currentAssignment = assignmentRepository.findById(assignmentId);

        if (currentAssignment.isEmpty()) {

            throw new ResourceNotFound("Assignment not found");
        }

        Assignment assignment = currentAssignment.get();

        return createAssignmentResponse(assignment, assignment.getCourse().getId());
    }

    /**
     *
     * Get contest details
     *
     * @param contestId Long
     * @return CourseResourceResponse
     */
    @Transactional(readOnly = true)
    private CourseResourceResponse getContestDetail(Long contestId) {
        var currentContest = contestRepository.findById(contestId);

        if (currentContest.isEmpty()) {

            throw new ResourceNotFound("Contest not found");
        }

        Contest contest = currentContest.get();

        return createContestResponse(contest, contest.getCourse().getId());
    }

    /**
     *
     * Get assignment submission details
     *
     * @param submissionId Long
     * @return CourseResourceResponse
     */
    @Transactional(readOnly = true)
    private CourseResourceResponse getAssignmentSubmissionDetail(Long submissionId) {
        var currentSubmission = assignmentSubmissionRepository.findById(submissionId);

        if (currentSubmission.isEmpty()) {

            throw new ResourceNotFound("Assignment submission not found");
        }

        AssignmentSubmission assignmentSubmission = currentSubmission.get();

        return createAssignmentSubmissionResponse(assignmentSubmission, assignmentSubmission.getCourse().getId());
    }

    /**
     *
     * Get contest submission details
     *
     * @param submissionId Long
     * @return CourseResourceResponse
     */
    @Transactional(readOnly = true)
    private CourseResourceResponse getContestSubmissionDetail(Long submissionId) {
        var currentSubmission = contestSubmissionRepository.findById(submissionId);

        if (currentSubmission.isEmpty()) {

            throw new ResourceNotFound("Contest submission not found");
        }

        ContestSubmission contestSubmission = currentSubmission.get();

        return createContestSubmissionResponse(contestSubmission, contestSubmission.getCourse().getId());
    }

    /**
     *
     * Add submission role for user
     *
     * @param user User
     * @param submissionId Long
     */
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

    @Async
    private CompletableFuture<Boolean> saveFile(String filePath, MultipartFile file) {
        log.info("filePath: {}", filePath);

        File destFile = new File(filePath);

        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(true);

    }

    private CourseResourceResponse createLessonResponse(Lesson lesson, Long courseId) {
        return CourseResourceResponse.builder()
                .name(lesson.getName())
                .resourceId(lesson.getId())
                .resourceType("Lesson")
                .courseId(courseId)
                .content(lesson.getContent().getPath())
                .build();
    }

    private CourseResourceResponse createAssignmentResponse(Assignment assignment, Long courseId) {
        return CourseResourceResponse.builder()
                .name(assignment.getName())
                .resourceId(assignment.getId())
                .resourceType("Assignment")
                .courseId(courseId)
                .content(assignment.getContent().getPath())
                .startDate(assignment.getStartDate())
                .endDate(assignment.getEndDate())
                .build();
    }

    private CourseResourceResponse createContestResponse(Contest contest, Long courseId) {
        return CourseResourceResponse.builder()
                .name(contest.getName())
                .resourceId(contest.getId())
                .resourceType("Contest")
                .courseId(courseId)
                .content(contest.getContent().getPath())
                .startDate(contest.getStartDate())
                .endDate(contest.getEndDate())
                .duration(contest.getDuration())
                .build();
    }

    private CourseResourceResponse createAssignmentSubmissionResponse(AssignmentSubmission assignmentSubmission, Long courseId) {
        return CourseResourceResponse.builder()
                .name(assignmentSubmission.getContent().getName())
                .resourceId(assignmentSubmission.getId())
                .resourceType("Assignment Submission")
                .courseId(courseId)
                .content(assignmentSubmission.getContent().getPath())
                .build();
    }

    private CourseResourceResponse createContestSubmissionResponse(ContestSubmission contestSubmission, Long courseId) {
        return CourseResourceResponse.builder()
                .name(contestSubmission.getContent().getName())
                .resourceId(contestSubmission.getId())
                .resourceType("Contest Submission")
                .courseId(courseId)
                .content(contestSubmission.getContent().getPath())
                .build();
    }

}
