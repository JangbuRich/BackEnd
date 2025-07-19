package com.jangburich.domain.repository;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.repository.queryDsl.TeamQueryDslRepository;
import com.jangburich.domain.user.domain.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamQueryDslRepository {
    Optional<Team> findBySecretCode(String secretCode);


    @Query("SELECT t FROM Team t JOIN UserTeam ut ON ut.team = t WHERE ut.user = :user AND t.status = :status")
    Optional<List<Team>> findAllByUserAndStatus(@Param("user") User user, @Param("status") Status status);
}
