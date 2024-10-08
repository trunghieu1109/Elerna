package com.application.elerna.service.impl;

import com.application.elerna.dto.request.AddCourseRequest;
import com.application.elerna.dto.request.UpdateCourseRequest;
import com.application.elerna.dto.response.CourseRequestResponse;
import com.application.elerna.dto.response.CourseResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.*;
import com.application.elerna.repository.*;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final TeamRepository teamRepository;

    /**
     *
     * User send creating course request to database
     *
     * @param request AddCourseRequest
     * @return String
     */
    @Override
    public String addCourseRequest(AddCourseRequest request) {

        // extract user from authentication
        User user = userService.getUserFromAuthentication();

        // create course request
        log.info("Create new course request");
        CourseRequest courseRequest = CourseRequest.builder()
                .description(request.getDescription())
                .major(request.getMajor())
                .language(request.getLanguage())
                .name(request.getName())
                .user(user)
                .build();

        // save course and user to database
        log.info("Add course request to user");
        user.addCourseRequest(courseRequest);

        log.info("Save course request to database");
        courseRequestRepository.save(courseRequest);
        userRepository.save(user);

        return "Add new course request to database";
    }


    /**
     *
     * Get all course requests by admin
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getAllCourseRequest(Integer pageNo, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<CourseRequest> courseRequests = courseRequestRepository.findAll(pageable);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(courseRequests.getTotalPages())
                .data(courseRequests.getContent().parallelStream().map(this::createCourseRequestResponse))
                .build();
    }

    /**
     *
     * Admin approves creating course requests
     *
     * @param requestId Long
     * @return String
     */
    
    @Override
    public String approveCourseRequest(Long requestId) {

        // get course request
        log.info("Get course request, requestId {}", requestId);
        var courseRequest = courseRequestRepository.findById(requestId);

        if (courseRequest.isEmpty()) {
            log.error("Cant not find course request with requestId {}", requestId);
            throw new ResourceNotFound("Cant not find course request with requestId: " + requestId);
        }

        // check whether course name is existed
        CourseRequest request = courseRequest.get();

        var course = courseRepository.findByName(request.getName());

        if (course.isPresent()) {
//            rejectCourseRequest(requestId);
            throw new InvalidRequestData("Course name is existed in database");
        }

        // create course
        log.info("Create new course");
        CompletableFuture<Boolean> isCreatedFuture = createCourse(request);

        // delete course request
        log.info("Delete course request from database");
        CompletableFuture<Boolean> isDeletedFuture = deleteCourseRequest(request);

        try {
            if (isCreatedFuture.get() && isDeletedFuture.get()) {
                return "Approved course request " + requestId + ", create course " + request.getName();
            } else {
                return "Failed to approve request";
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Admin rejects creating course requests
     *
     * @param requestId Long
     * @return String
     */
    @Override
    public String rejectCourseRequest(Long requestId) {

        var courseRequest = courseRequestRepository.findById(requestId);

        if (courseRequest.isEmpty()) {
            throw new ResourceNotFound("Course request is not existed");
        }

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

    /**
     *
     * Get all courses by searching criteria
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String[]
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getAllCourseBySearch(Integer pageNo, Integer pageSize, String... searchBy) {

        Page<Course> courses = utilsRepository.findCourseBySearchCriteria(pageNo, pageSize, searchBy);

//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//
//        Page<Course> courses = courseRepository.findAll(pageable);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(courses.getTotalPages())
                .data(courses.getContent().parallelStream().map(this::createCourseResponses))
                .build();
    }

    /**
     *
     * Get course's details
     *
     * @param courseId Long
     * @return CourseResponse
     */
    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseDetail(Long courseId) {

        var course_ = courseRepository.findById(courseId);

        if (course_.isEmpty() || !course_.get().isStatus()) {
            throw new ResourceNotFound("Course does not ready, courseId: " + courseId);
        }

        Course course = course_.get();

        return createCourseResponses(course);
    }

    /**
     *
     * Create course response from course's details
     *
     * @param course Course
     * @return CourseResponse
     */
    private CourseResponse createCourseResponses(Course course) {
        return CourseResponse.builder()
                .name(course.getName())
                .duration(course.getDuration())
                .price(course.getPrice())
                .rating(course.getRating())
                .major(course.getMajor())
                .id(course.getId())
                .description(course.getDescription())
                .language(course.getLanguage())
                .assignments(course.getAssignments().parallelStream().map(Assignment::getName).toList())
                .lessons(course.getLessons().parallelStream().map(Lesson::getName).toList())
                .contests(course.getContests().parallelStream().map(Contest::getName).toList())
                .build();
    }

    /**
     *
     * User registers course
     *
     * @param courseId Long
     * @return String
     */
    @Override
    public String registerCourse(Long courseId) {

        User user = userService.getUserFromAuthentication();

        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new InvalidRequestData("Course not existed");
        }

        user.addCourse(course.get());
        course.get().addUser(user);

        courseRepository.save(course.get());
        userRepository.save(user);

        var role = roleRepository.findByName("STUDENT_COURSE_" + course.get().getId());

        if (role == null) {
            throw new InvalidRequestData("Role not existed");
        }

        role.addUser(user);
        user.addRole(role);

        roleRepository.save(role);
        userRepository.save(user);

        return "User " + user.getId() + " Registered Course " + courseId;
    }

    /**
     *
     * Team registers course
     *
     * @param teamId Long
     * @param courseId Long
     * @return String
     */
    public String registerTeamCourse(Long teamId, Long courseId) {

        var team = teamRepository.findById(teamId);

        if (team.isEmpty() || !team.get().isActive()) {
            throw new InvalidRequestData("Team not existed or invalid");
        }

        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new InvalidRequestData("Course not existed");
        }

        team.get().addCourse(course.get());
        course.get().addTeam(team.get());

        courseRepository.save(course.get());
        teamRepository.save(team.get());

        var role = roleRepository.findByName("STUDENT_COURSE_" + course.get().getId());

        if (role == null) {
            throw new InvalidRequestData("Role not existed");
        }

        role.addTeam(team.get());
        team.get().addRole(role);

        roleRepository.save(role);
        teamRepository.save(team.get());

        return "Team " + teamId + " Registered Course " + courseId;
    }

    /**
     *
     * User unregisters course
     *
     * @param userId Long
     * @param courseId Long
     * @return String
     */
    @Override
    public String unregisterCourse(Long userId, Long courseId) {

        // get user from userId
        var user = userRepository.findById(userId);

        if (user.isEmpty() || !user.get().isActive()) {
            throw new InvalidRequestData("User not existed or invalid");
        }

        // get course from courseId
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new InvalidRequestData("Course not existed");
        }

        // verify user containing course
        User userStd = user.get();
        Course courseStd = course.get();

        if (!userStd.getCourses().contains(courseStd)) {
            throw new InvalidRequestData("User has not joined course before");
        }

        if (!courseStd.getUsers().contains(userStd)) {
            throw new InvalidRequestData("User has not joined course before");
        }

        // remove user-course relationship
        userStd.getCourses().remove(courseStd);
        courseStd.getUsers().remove(userStd);

        userRepository.save(userStd);
        courseRepository.save(courseStd);

        log.info("Remove user role");

        // remove course's roles
        Set<Role> roles = userStd.getRoles();

        for (Role role : roles) {
            if (role.getName().toLowerCase().contains("course")
                    && role.getName().toLowerCase().contains("" + courseStd.getId())) {
                userRepository.removeUserRole(userId, role.getId());

            }
        }

        return "User " + userId + " unregistered course " + courseId;
    }

    /**
     *
     * Team unregisters course
     *
     * @param teamId Long
     * @param courseId Long
     * @return String
     */
    @Override
    public String unregisterTeamCourse(Long teamId, Long courseId) {

        // get team from teamId
        var team = teamRepository.findById(teamId);

        if (team.isEmpty() || !team.get().isActive()) {
            throw new InvalidRequestData("Team not existed or invalid");
        }

        // get course from courseId
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new InvalidRequestData("Course not existed");
        }

        // check if team contains course
        Team teamStd = team.get();
        Course courseStd = course.get();

        if (!teamStd.getCourses().contains(courseStd)) {
            throw new InvalidRequestData("Team has not joined course before");
        }

        if (!courseStd.getTeams().contains(teamStd)) {
            throw new InvalidRequestData("Team has not joined course before");
        }

        // remove team-course relationship
        teamStd.getCourses().remove(courseStd);
        courseStd.getTeams().remove(teamStd);

        teamRepository.save(teamStd);
        courseRepository.save(courseStd);

        log.info("Remove team role");

        // remove team's course-related roles
        Set<Role> roles = teamStd.getRoles();

        for (Role role : roles) {

            if (role.getName().toLowerCase().contains("course")
                    && role.getName().toLowerCase().contains("" + courseStd.getId())) {
                teamRepository.removeTeamRole(teamId, role.getId());

            }
        }

        return "Team " + teamId + " unregistered course " + courseId;
    }

    /**
     *
     * Delete course
     *
     * @param courseId Long
     * @return String
     */
    @Override
    public String deleteCourse(Long courseId) {

        // get course from courseId
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new ResourceNotFound("Course is empty or is deleted");
        }

        // delete all user-course relationship
        Set<User> users = course.get().getUsers();

        for (User user : users) {
            Set<Role> roles = user.getRoles();

            for (Role role : roles) {
                if (role.getName().contains("COURSE") && role.getName().contains("" + course.get().getId())) {
                    unregisterCourse(user.getId(), courseId);
                    break;
                }
            }

        }

        Set<Team> teams = course.get().getTeams();

        for (Team team : teams) {
            Set<Role> roles = team.getRoles();

            for (Role role : roles) {
                if (role.getName().contains("COURSE") && role.getName().contains("" + course.get().getId())) {
                    unregisterTeamCourse(team.getId(), courseId);
                    break;
                }
            }

        }

        course.get().setStatus(false);
        courseRepository.save(course.get());

        return "Delete Courses";
    }

    /**
     *
     * Send register course request
     *
     * @param courseId Long
     * @return String
     */
    @Override
    public String sendRegisterRequest(Long courseId) {

        User user = userService.getUserFromAuthentication();

        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new InvalidRequestData("Course not existed");
        }

        return "https://localhost:80/course/register/" + courseId;
    }

    /**
     *
     * get all user's registered courses
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getAllRegisteredCourse(Integer pageNo, Integer pageSize) {

        User user = userService.getUserFromAuthentication();

        List<Course> currentCourses = new ArrayList<>(user.getCourses().stream().toList());

        List<Course> courses = new ArrayList<>();

        for (Course course : currentCourses) {
            if (course.isStatus()) {
                courses.add(course);
            }
        }

        courses.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(courses.size() * 1.0 / pageSize))
                .data(courses.subList(Math.max(pageNo * pageSize, 0),
                        Math.min((pageNo + 1) * pageSize, courses.size()))
                        .parallelStream().map(this::createCourseResponses))
                .build();
    }

    @Override
    public PageResponse<?> getAllRegisteredCourseByTeam(Long teamId, Integer pageNo, Integer pageSize) {

        var team = teamRepository.findById(teamId);

        if (team.isEmpty()) {
            throw new ResourceNotFound("Team not found, teamId: " + teamId);
        }

        List<Course> currentCourses = new ArrayList<>(team.get().getCourses().stream().toList());

        List<Course> courses = new ArrayList<>();

        for (Course course : currentCourses) {
            if (course.isStatus()) {
                courses.add(course);
            }
        }

        courses.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(courses.size() * 1.0 / pageSize))
                .data(courses.subList(Math.max(pageNo * pageSize, 0),
                                Math.min((pageNo + 1) * pageSize, courses.size()))
                        .parallelStream().map(this::createCourseResponses))
                .build();
    }

    /**
     *
     * User updates course's details
     *
     * @param request UpdateCourseRequest
     * @return String
     */
    @Override
    public String updateCourse(UpdateCourseRequest request) {

        // get current course
        Long courseId = request.getCourseId();

        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new ResourceNotFound("Course does not exist in database");
        }

        Course currentCourse = course.get();

        // update course's details
        currentCourse.setName(request.getName());
        currentCourse.setMajor(request.getMajor());
        currentCourse.setLanguage(request.getLanguage());
        currentCourse.setDescription(request.getDescription());
        currentCourse.setDuration(request.getDuration());
        currentCourse.setRating(request.getRating());
        currentCourse.setPrice(request.getPrice());

        courseRepository.save(currentCourse);

        return "Update course " + courseId + " successfully";
    }

    /**
     *
     * Get all course's student list
     *
     * @param courseId Long
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getAllStudentList(Long courseId, Integer pageNo, Integer pageSize) {

        // get course from courseId
        var course = courseRepository.findById(courseId);

        if (course.isEmpty() || !course.get().isStatus()) {
            throw new InvalidRequestData("Course is not existed");
        }

        List<User> users = new ArrayList<>(course.get().getUsers().stream().toList());

        // sort student list by username
        users.sort((o1, o2) -> o1.getUsername().compareTo(o2.getUsername()));

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(users.size() * 1.0 / pageSize))
                .data(users.subList(Math.max(pageNo * pageSize, 0),
                                Math.min((pageNo + 1) * pageSize, users.size()))
                        .stream().map(userService::createUserDetail))
                .build();
    }

    /**
     *
     * Delete course request
     *
     * @param courseRequest CourseRequest
     */
    @Async
    private CompletableFuture<Boolean> deleteCourseRequest(CourseRequest courseRequest) {
        User user = courseRequest.getUser();

        user.getCourseRequests().remove(courseRequest);

        userRepository.save(user);
        courseRequestRepository.delete(courseRequest);

        return CompletableFuture.completedFuture(true);
    }

    /**
     *
     * Create course from course request
     *
     * @param request CourseRequest
     */
    @Async
    private CompletableFuture<Boolean> createCourse(CourseRequest request) {
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
                .status(true)
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

        if (currentCourse.isEmpty() || !course.isStatus()) {
            throw new ResourceNotFound("Cant get course with name: " + course.getName());
        }

        Privilege priUpdate = privilegeService.createPrivilege("course", currentCourse.get().getId(), "update", "Update course, id = " + currentCourse.get().getId());
        Privilege priDelete = privilegeService.createPrivilege("course", currentCourse.get().getId(), "delete", "Delete course, id = " + currentCourse.get().getId());
        Privilege priView = privilegeService.createPrivilege("course", currentCourse.get().getId(), "view", "View course, id = " + currentCourse.get().getId());
        Privilege priSubmit = privilegeService.createPrivilege("course", currentCourse.get().getId(), "submit", "View course, id = " + currentCourse.get().getId());

        Role roleStudent = roleService.createRole("STUDENT", "course", currentCourse.get().getId());
        Role roleEditor = roleService.createRole("EDITOR", "course", currentCourse.get().getId());
        Role roleAdmin = roleService.createRole("ADMIN", "course", currentCourse.get().getId());

        roleAdmin.addPrivilege(priDelete);
        roleAdmin.addPrivilege(priView);
        roleAdmin.addPrivilege(priUpdate);
        roleAdmin.addPrivilege(priSubmit);

        roleEditor.addPrivilege(priUpdate);
        roleEditor.addPrivilege(priDelete);

        roleStudent.addPrivilege(priView);
        roleStudent.addPrivilege(priSubmit);

        priDelete.addRole(roleAdmin);
        priDelete.addRole(roleEditor);

        priUpdate.addRole(roleAdmin);
        priUpdate.addRole(roleEditor);

        priView.addRole(roleAdmin);
        priView.addRole(roleStudent);

        priSubmit.addRole(roleAdmin);
        priSubmit.addRole(roleStudent);

        privilegeService.savePrivilege(priDelete);
        privilegeService.savePrivilege(priUpdate);
        privilegeService.savePrivilege(priView);
        privilegeService.savePrivilege(priSubmit);

        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleEditor);
        roleService.saveRole(roleStudent);

        user.addRole(roleAdmin);
        roleAdmin.addUser(user);

        roleService.saveRole(roleAdmin);
        userRepository.save(user);

        return CompletableFuture.completedFuture(true);

    }


    /**
     *
     * Create course request response from course request
     *
     * @param request CourseRequest
     * @return CourseRequestResponse
     */
    private CourseRequestResponse createCourseRequestResponse(CourseRequest request) {
        return CourseRequestResponse.builder()
                .id(request.getId())
                .name(request.getName())
                .major(request.getMajor())
                .language(request.getLanguage())
                .description(request.getDescription())
                .proposerId(request.getUser().getId())
                .build();
    }

}
