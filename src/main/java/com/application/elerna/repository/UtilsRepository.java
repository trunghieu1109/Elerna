package com.application.elerna.repository;

import com.application.elerna.model.Course;
import com.application.elerna.model.Team;
import com.application.elerna.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UtilsRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public Page<User> findUserBySearchCriteria(Integer pageNo, Integer pageSize, String... searchBy) {

        StringBuilder query = new StringBuilder("select u from User u where 1 = 1");

        if (searchBy != null) {
            for (String criteria : searchBy) {
                String regex = "(\\w+?)(:)(.*)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(criteria);

                if (matcher.find()) {
                    String addition = " and u." + matcher.group(1) + " like lower(\"%" + matcher.group(3) + "%\")";
                    log.info("Addition Criteria: " + addition);
                    query.append(addition);
                }
            }
        }

        Query selectQuery = entityManager.createQuery(query.toString());

        Long totalPages = (long) selectQuery.getResultList().size() / pageSize;

        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);

        List<User> users = selectQuery.getResultList();

        Page<User> page = new PageImpl<User>(users, PageRequest.of(pageNo, pageSize), totalPages);

        return page;
    }

    public Page<Team> findTeamByName(Integer pageNo, Integer pageSize, String searchBy) {
        StringBuilder query = new StringBuilder("select t from Team t where t.name like lower(\"%" + searchBy + "%\") and t.isActive = TRUE");

        Query selectQuery = entityManager.createQuery(query.toString());

        int size_ = selectQuery.getResultList().size();

        int totalPages = size_;

        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);
        List<Team> teams = selectQuery.getResultList();

        Page<Team> page = new PageImpl<Team>(teams, PageRequest.of(pageNo, pageSize), totalPages);

        return page;

    }

    public Page<Course> findCourseBySearchCriteria(Integer pageNo, Integer pageSize, String... searchBy) {

        StringBuilder query = new StringBuilder("select c from Course c where 1 = 1");

        if (searchBy != null) {
            for (String criteria : searchBy) {
                String regex = "(\\w+?)(:)(.*)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(criteria);

                if (matcher.find()) {
                    String addition = " and c." + matcher.group(1) + " like lower(\"%" + matcher.group(3) + "%\")";
                    log.info("Addition Criteria: " + addition);
                    query.append(addition);
                }
            }
        }

        Query selectQuery = entityManager.createQuery(query.toString());

//        Long totalPages = (long) ;

        int size_ = selectQuery.getResultList().size();

        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);

        List<Course> courses = selectQuery.getResultList();

        Page<Course> page = new PageImpl<Course>(courses, PageRequest.of(pageNo, pageSize), size_);
//        log.info(((long) Math.ceil(size_ * 1.0 / pageSize)) + " " + size_ + " " + pageSize);
        return page;
    }


}
