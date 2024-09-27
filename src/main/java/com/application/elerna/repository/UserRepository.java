package com.application.elerna.repository;

import com.application.elerna.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM roles_users_teams WHERE user_id = :userId AND team_id = :teamId", nativeQuery = true)
    void removeUserTeam(@Param("userId") Long userId, @Param("teamId") Long teamId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM roles_users_teams WHERE user_id = :userId AND role_id = :roleId", nativeQuery = true)
    void removeUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

}
