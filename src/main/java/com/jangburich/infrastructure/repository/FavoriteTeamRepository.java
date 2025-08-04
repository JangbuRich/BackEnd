package com.jangburich.infrastructure.repository;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.FavoriteTeam;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteTeamRepository extends JpaRepository<FavoriteTeam, Long> {
    List<FavoriteTeam> findAllByUserAndStatus(User user, Status status);

    Optional<FavoriteTeam> findByTeamAndUserAndStatus(Team team, User user, Status status);

    @Query("select ft.team.id from FavoriteTeam ft where ft.user = :user and ft.status = 'ACTIVE'")
    List<Long> findLikedTeamIdsByUser(@Param("user") User user);
}
