package com.application.elerna.repository;

import com.application.elerna.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    public Optional<Assignment> findByName(String name);

}
