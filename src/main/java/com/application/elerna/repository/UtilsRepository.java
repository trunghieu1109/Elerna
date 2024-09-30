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

    /**
     *
     * Find all user by search criteria
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String[]
     * @return Page<User>
     */
    public Page<User> findUserBySearchCriteria(Integer pageNo, Integer pageSize, String... searchBy) {

        StringBuilder query = new StringBuilder("select u from User u where 1 = 1");

        if (searchBy != null) {
            for (String criteria : searchBy) {
                String regex = "(\\w+?)(:)(.*)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(criteria);

                if (matcher.find()) {
                    String addition = " and u." + matcher.group(1) + " like lower(\"%" + matcher.group(3) + "%\")";
                    log.info("Addition Sorting Criteria: {}", addition);
                    query.append(addition);
                }
            }
        }

        Query selectQuery = entityManager.createQuery(query.toString());

        long totalPages = (long) selectQuery.getResultList().size() / pageSize;

        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);

        List<User> users = selectQuery.getResultList();

        return new PageImpl<User>(users, PageRequest.of(pageNo, pageSize), totalPages);
    }

    /**
     *
     * Find all team by searching criteria
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @param searchBy String
     * @return Page<Team>
     */
    public Page<Team> findTeamByName(Integer pageNo, Integer pageSize, String searchBy) {
        StringBuilder query = new StringBuilder("select t from Team t where t.name like lower(\"%" + searchBy + "%\") and t.isActive = TRUE");

        Query selectQuery = entityManager.createQuery(query.toString());

        int totalPages = selectQuery.getResultList().size();

        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);
        List<Team> teams = selectQuery.getResultList();

        return new PageImpl<>(teams, PageRequest.of(pageNo, pageSize), totalPages);

    }

    public Page<Course> findCourseBySearchCriteria(Integer pageNo, Integer pageSize, String... searchBy) {

        StringBuilder query = new StringBuilder("select c from Course c where 1 = 1 and c.status = TRUE");

        if (searchBy != null) {
            for (String criteria : searchBy) {
                String regex = "(\\w+?)(:)(.*)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(criteria);

                if (matcher.find()) {
                    String addition = " and c." + matcher.group(1) + " like lower(\"%" + matcher.group(3) + "%\")";
                    log.info("Addition Searching Criteria: {}", addition);
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

        return new PageImpl<>(courses, PageRequest.of(pageNo, pageSize), size_);
    }


}
