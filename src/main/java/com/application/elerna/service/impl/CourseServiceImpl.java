package com.application.elerna.service.impl;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.response.CourseRequestResponse;
import com.application.elerna.dto.response.CourseResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.*;
import com.application.elerna.repository.CourseRepository;
import com.application.elerna.repository.CourseRequestRepository;
import com.application.elerna.repository.UserRepository;
import com.application.elerna.repository.UtilsRepository;
import com.application.elerna.service.CourseService;
import com.application.elerna.service.PrivilegeService;
import com.application.elerna.service.RoleService;
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRequestRepository courseRequestRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final PrivilegeService privilegeService;
    private final RoleService roleService;
    private final UtilsRepository utilsRepository;

    @Override
    public String addCourseRequest(AddCourseRequest request) {

        var user = userRepository.findById(request.getProposerId());

        if (user.isEmpty() || !user.get().isActive()) {
            log.error("ProposerId is invalid");
            throw new InvalidRequestData("ProposerId is invalid");
        }

        log.info("Create new course request");
        CourseRequest courseRequest = CourseRequest.builder()
                .description(request.getDescription())
                .major(request.getMajor())
                .language(request.getLanguage())
                .name(request.getName())
                .user(user.get())
                .build();

        log.info("Add course request to user");
        user.get().addCourseRequest(courseRequest);

        log.info("Save course request to database");
        courseRequestRepository.save(courseRequest);
        userRepository.save(user.get());

        return "Add new course request to database";
    }

    @Override
    public PageResponse<?> getAllCourseRequest(Integer pageNo, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<CourseRequest> courseRequests = courseRequestRepository.findAll(pageable);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(courseRequests.getTotalPages())
                .data(courseRequests.stream().map(request ->
                        CourseRequestResponse.builder()
                                .id(request.getId())
                                .name(request.getName())
                                .major(request.getMajor())
                                .language(request.getLanguage())
                                .description(request.getDescription())
                                .proposerId(request.getUser().getId())
                                .build()))
                .build();
    }

    @Override
    public String approveCourseRequest(Long requestId) {

        log.info("Get course request, requestId " + requestId);
        var courseRequest = courseRequestRepository.findById(requestId);

        if (courseRequest.isEmpty()) {
            log.error("Cant not find course request with requestId " + requestId);
            throw new ResourceNotFound("Cant not find course request with requestId: " + requestId);
        }

        CourseRequest request = courseRequest.get();

        var course = courseRepository.findByName(request.getName());

        if (!course.isEmpty()) {
//            rejectCourseRequest(requestId);
            throw new InvalidRequestData("Course name is existed in database");
        }

        log.info("Create new course");
        createCourse(request);

        log.info("Delete course request from database");
        deleteCourseRequest(request);

        return "Approved course request " + requestId;
    }

    @Override
    public String rejectCourseRequest(Long requestId) {

        var courseRequest = courseRequestRepository.findById(requestId);

        CourseRequest request = courseRequest.get();

        User user = request.getUser();

        if (!user.isActive()) {
            throw new InvalidRequestData("Course request error! It belongs to an inactive user");
        }

        log.info("Remove request from user");
        if (user.getCourseRequests().contains(request)) {
            user.getCourseRequests().remove(request);
        } else {
            log.error("Invalid User - Course request creation");
            throw new InvalidRequestData("Invalid User - Course Request creation");
        }

        log.info("Delete course request");
        courseRequestRepository.delete(request);

        log.info("Update user");
        userRepository.save(user);

        return "Reject course request, requestId " + requestId;
    }

    @Override
    public PageResponse<?> getAllCourseBySearch(Integer pageNo, Integer pageSize, String... searchBy) {

        Page<Course> courses = utilsRepository.findCourseBySearchCriteria(pageNo, pageSize, searchBy);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(courses.getTotalPages())
                .data(courses.stream().map(course ->
                        CourseResponse.builder()
                                .name(course.getName())
                                .duration(course.getDuration())
                                .price(course.getPrice())
                                .rating(course.getRating())
                                .major(course.getMajor())
                                .id(course.getId())
                                .description(course.getDescription())
                                .language(course.getLanguage())
                                .assignments(course.getAssignments().stream().map(assign->assign.getName()).toList())
                                .lessons(course.getAssignments().stream().map(lesson->lesson.getName()).toList())
                                .contests(course.getContests().stream().map(contest -> contest.getName()).toList())
                                .build()))
                .build();
    }

    @Override
    public CourseResponse getCourseDetail(Long courseId) {

        var course_ = courseRepository.findById(courseId);

        if (course_.isEmpty()) {
            throw new ResourceNotFound("Course does not exist, courseId: " + courseId);
        }

        Course course = course_.get();

        return CourseResponse.builder()
                .name(course.getName())
                .duration(course.getDuration())
                .price(course.getPrice())
                .rating(course.getRating())
                .major(course.getMajor())
                .id(course.getId())
                .description(course.getDescription())
                .language(course.getLanguage())
                .assignments(course.getAssignments().stream().map(assign->assign.getName()).toList())
                .lessons(course.getAssignments().stream().map(lesson->lesson.getName()).toList())
                .contests(course.getContests().stream().map(contest -> contest.getName()).toList())
                .build();
    }

    private void deleteCourseRequest(CourseRequest courseRequest) {
        User user = courseRequest.getUser();

//        if (!user.isActive()) {
//            throw new InvalidRequestData("Course request error! It belongs to an inactive user");
//        }

        user.getCourseRequests().remove(courseRequest);

        userRepository.save(user);
        courseRequestRepository.delete(courseRequest);
    }

    private void createCourse(CourseRequest request) {
        Course course = Course.builder()
                .name(request.getName())
                .description(request.getDescription())
                .language(request.getLanguage())
                .major(request.getMajor())
                .duration(new Time(System.currentTimeMillis()))
                .rating(5.0)
                .price(0.0)
                .assignments(new HashSet<>())
                .users(new HashSet<>())
                .teams(new HashSet<>())
                .lessons(new HashSet<>())
                .contests(new HashSet<>())
                .assignmentSubmissions(new HashSet<>())
                .contestSubmissions(new HashSet<>())
                .transactions(new HashSet<>())
                .build();

        User user = request.getUser();

        if (!user.isActive()) {
            throw new InvalidRequestData("Course request error! It belongs to an inactive user");
        }

        course.addUser(user);
        user.addCourse(course);

        courseRepository.save(course);
        userRepository.save(user);

        Optional<Course> currentCourse = courseRepository.findByName(course.getName());

        Privilege priUpdate = privilegeService.createPrivilege("course", currentCourse.get().getId(), "update", "Update course, id = " + currentCourse.get().getId());
        Privilege priDelete = privilegeService.createPrivilege("course", currentCourse.get().getId(), "delete", "Delete course, id = " + currentCourse.get().getId());
        Privilege priView = privilegeService.createPrivilege("course", currentCourse.get().getId(), "view", "View course, id = " + currentCourse.get().getId());

        Role roleViewer = roleService.createRole("VIEWER", "course", currentCourse.get().getId());
        Role roleEditor = roleService.createRole("EDITOR", "course", currentCourse.get().getId());
        Role roleAdmin = roleService.createRole("ADMIN", "course", currentCourse.get().getId());

        roleAdmin.addPrivilege(priDelete);
        roleAdmin.addPrivilege(priView);
        roleAdmin.addPrivilege(priUpdate);

        roleEditor.addPrivilege(priUpdate);
        roleEditor.addPrivilege(priDelete);

        roleViewer.addPrivilege(priView);

        priDelete.addRole(roleAdmin);
        priDelete.addRole(roleEditor);

        priUpdate.addRole(roleAdmin);
        priUpdate.addRole(roleEditor);

        priView.addRole(roleAdmin);
        priView.addRole(roleViewer);

        privilegeService.savePrivilege(priDelete);
        privilegeService.savePrivilege(priUpdate);
        privilegeService.savePrivilege(priView);

        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleEditor);
        roleService.saveRole(roleViewer);

        user.addRole(roleAdmin);
        roleAdmin.addUser(user);

        roleService.saveRole(roleAdmin);
        userRepository.save(user);

    }

}
