package com.application.elerna.repository;

import com.application.elerna.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    public Optional<Team> findByName(String name);

}
