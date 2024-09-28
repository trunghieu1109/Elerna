package com.application.elerna.repository;

import com.application.elerna.model.Team;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    public Optional<Team> findByName(String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM roles_users_teams WHERE team_id = :teamId AND role_id = :roleId", nativeQuery = true)
    void removeTeamRole(@Param("teamId") Long teamId, @Param("roleId") Long roleId);

}
