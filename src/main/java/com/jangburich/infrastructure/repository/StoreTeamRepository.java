package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.Team;

import java.util.List;
import java.util.Optional;

import com.jangburich.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.StoreTeam;
import com.jangburich.presentation.store.dtos.response.store.StoreTeamResponseDTO;

public interface StoreTeamRepository extends JpaRepository<StoreTeam, Long> {
    Optional<StoreTeam> findByStoreIdAndTeamId(Long store_id, Long team_id);

    List<StoreTeamResponseDTO> findAllByStore(Store store);

    Optional<StoreTeam> findByStoreAndTeam(Store store, Team team);

    List<StoreTeamResponseDTO> findAllByStoreOrderByCreatedAtDesc(Store store);

    @Query("SELECT st FROM StoreTeam st " +
            "JOIN FETCH st.store s " +
            "WHERE s.id = :storeId")
    List<StoreTeam> findByStoreIdWithStoreAndTeam(@Param("storeId") Long storeId);

    @Query("""
            select st.team.id, sum(st.remainPoint)
            from StoreTeam st
            where st.team in :teams
            group by st.team.id
            order by st.team.id
            """)
    List<Object[]> findRemainingPointByTeams(@Param("teams") List<Team> teams);

    @Query("""
            	select sum (st.remainPoint)
            	from StoreTeam st
            	inner join Team t on st.team = t
            	inner join UserTeam ut on ut.team = t
            	where ut.user = :user
            	and st.status = "ACTIVE"
            """)
    Integer sumRemainPointByUserAndStatus(@Param("user") User user);
}