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
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;

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
                        createCourseResponses(course)))
                .build();
    }

    @Override
    public CourseResponse getCourseDetail(Long courseId) {

        var course_ = courseRepository.findById(courseId);

        if (course_.isEmpty()) {
            throw new ResourceNotFound("Course does not exist, courseId: " + courseId);
        }

        Course course = course_.get();

        return createCourseResponses(course);
    }

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
                .assignments(course.getAssignments().stream().map(assign->assign.getName()).toList())
                .lessons(course.getLessons().stream().map(lesson->lesson.getName()).toList())
                .contests(course.getContests().stream().map(contest -> contest.getName()).toList())
                .build();
    }

    @Override
    public String registerCourse(Long userId, Long courseId) {

        var user = userRepository.findById(userId);

        if (user.isEmpty() || !user.get().isActive()) {
            throw new InvalidRequestData("User not existed or invalid");
        }

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new InvalidRequestData("Course not existed");
        }

        user.get().addCourse(course.get());
        course.get().addUser(user.get());

        courseRepository.save(course.get());
        userRepository.save(user.get());

        var role = roleRepository.findByName("STUDENT_COURSE_" + course.get().getId());

        if (role == null) {
            throw new InvalidRequestData("Role not existed");
        }

        role.addUser(user.get());
        user.get().addRole(role);

        roleRepository.save(role);
        userRepository.save(user.get());

        return "User " + userId + " Registered Course " + courseId;
    }

    public String registerTeamCourse(Long teamId, Long courseId) {

        var team = teamRepository.findById(teamId);

        if (team.isEmpty() || !team.get().isActive()) {
            throw new InvalidRequestData("Team not existed or invalid");
        }

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
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

    @Override
    public String unregisterCourse(Long userId, Long courseId) {

        var user = userRepository.findById(userId);

        if (user.isEmpty() || !user.get().isActive()) {
            throw new InvalidRequestData("User not existed or invalid");
        }

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new InvalidRequestData("Course not existed");
        }

        User userStd = user.get();
        Course courseStd = course.get();

        if (!userStd.getCourses().contains(courseStd)) {
            throw new InvalidRequestData("User has not joined course before");
        }

        if (!courseStd.getUsers().contains(userStd)) {
            throw new InvalidRequestData("User has not joined course before");
        }

        userStd.getCourses().remove(courseStd);
        courseStd.getUsers().remove(userStd);

        userRepository.save(userStd);
        courseRepository.save(courseStd);

        log.info("Remove user role");

        Set<Role> roles = userStd.getRoles();

        for (Role role : roles) {
            if (role.getName().toLowerCase().contains("course")
                    && role.getName().toLowerCase().contains("" + courseStd.getId())) {
                userRepository.removeUserRole(userId, role.getId());

            }
        }

        return "User " + userId + " unregistered course " + courseId;
    }

    @Override
    public String unregisterTeamCourse(Long teamId, Long courseId) {
        var team = teamRepository.findById(teamId);

        if (team.isEmpty() || !team.get().isActive()) {
            throw new InvalidRequestData("Team not existed or invalid");
        }

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new InvalidRequestData("Course not existed");
        }

        Team teamStd = team.get();
        Course courseStd = course.get();

        if (!teamStd.getCourses().contains(courseStd)) {
            throw new InvalidRequestData("Team has not joined course before");
        }

        if (!courseStd.getTeams().contains(teamStd)) {
            throw new InvalidRequestData("Team has not joined course before");
        }

        teamStd.getCourses().remove(courseStd);
        courseStd.getTeams().remove(teamStd);

        teamRepository.save(teamStd);
        courseRepository.save(courseStd);

        log.info("Remove team role");

        Set<Role> roles = teamStd.getRoles();

        for (Role role : roles) {

            if (role.getName().toLowerCase().contains("course")
                    && role.getName().toLowerCase().contains("" + courseStd.getId())) {
                teamRepository.removeTeamRole(teamId, role.getId());

            }
        }

        return "Team " + teamId + " unregistered course " + courseId;
    }

    @Override
    public PageResponse<?> getAllRegisteredCourse(Long userId, Integer pageNo, Integer pageSize) {

        var user = userRepository.findById(userId);

        if (user.isEmpty() || !user.get().isActive()) {
            throw new InvalidRequestData("User is not existed or invalid");
        }

        List<Course> courses = new ArrayList<>(user.get().getCourses().stream().toList());

        Collections.sort(courses, new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {

                return o1.getName().compareTo(o2.getName());
            }
        });

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(courses.size() * 1.0 / pageSize))
                .data(courses.subList(Math.max(pageNo * pageSize, 0),
                        Math.min((pageNo + 1) * pageSize, courses.size()))
                        .stream().map(course -> createCourseResponses(course)))
                .build();
    }

    @Override
    public String updateCourse(UpdateCourseRequest request) {

        Long courseId = request.getCourseId();

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new ResourceNotFound("Course doesnot exist in database");
        }

        Course currentCourse = course.get();

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

    @Override
    public PageResponse<?> getAllStudentList(Long courseId, Integer pageNo, Integer pageSize) {

        var course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            throw new InvalidRequestData("Course is not existed");
        }

        List<User> users = new ArrayList<>(course.get().getUsers().stream().toList());

        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {

                return o1.getUsername().compareTo(o2.getUsername());
            }
        });

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil(users.size() * 1.0 / pageSize))
                .data(users.subList(Math.max(pageNo * pageSize, 0),
                                Math.min((pageNo + 1) * pageSize, users.size()))
                        .stream().map(user -> userService.createUserDetail(user)))
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
//        roleEditor.addPrivilege(priSubmit);

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

    }

}
