package com.application.elerna.repository;

import com.application.elerna.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    public Optional<Contest> findByName(String name);

}
