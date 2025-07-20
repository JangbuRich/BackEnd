package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.FavoriteTeam;
import com.jangburich.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteTeamRepository extends JpaRepository<FavoriteTeam, Long> {
    @Query("select ft.team.id from FavoriteTeam ft where ft.user = :user")
    List<Long> findLikedTeamIdsByUser(@Param("user") User user);
}
