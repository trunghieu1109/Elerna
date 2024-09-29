package com.application.elerna.repository;

import com.application.elerna.model.ContestSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestSubmissionRepository extends JpaRepository<ContestSubmission, Long> {

}
