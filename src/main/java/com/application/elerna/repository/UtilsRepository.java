package com.application.elerna.repository;

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

        Long totalPages = (long) selectQuery.getResultList().size();

        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults((pageNo + 1) * pageSize);

        List<User> users = selectQuery.getResultList();

        Page<User> page = new PageImpl<User>(users, PageRequest.of(pageNo, pageSize), totalPages);

        return page;
    }

}
