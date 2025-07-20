package com.jangburich.infrastructure.repository;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.Team;
import com.jangburich.domain.entity.UserTeam;
import com.jangburich.domain.user.domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    int countByTeam(Team team);

    @Query("""
            select ut.team.id, count(ut)
            from UserTeam ut
            where ut.team in :teams
            group by ut.team.id
            order by ut.team.id
            """
    )
    List<Object[]> countByTeams(@Param("teams") List<Team> teams);

    @Query("""
            select ut.team.id, ut.user
            from UserTeam ut
            where ut.team in :teams
            order by ut.team.id
            """)
    List<Object[]> findLeaderByTeams(@Param("teams") List<Team> teams);

    @Query("""
                select ut.team.id, ut.user.profileImageUrl
                from UserTeam ut
                where ut.team in :teams
                order by ut.team.id
            """)
    List<Object[]> findProfileImagesByTeams(@Param("teams") List<Team> teams);

    boolean existsByUserAndTeam(User user, Team team);

    List<UserTeam> findAllByTeam(Team team);

    List<UserTeam> findAllByTeamAndStatus(Team team, Status status);
}
