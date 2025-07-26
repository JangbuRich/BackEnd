package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.Team;

import java.util.List;
import java.util.Optional;

import com.jangburich.domain.user.domain.User;
import com.jangburich.presentation.team.dto.response.TeamPrepaidStoreItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT st FROM StoreTeam st " + "JOIN FETCH st.store s " + "WHERE s.id = :storeId")
    List<StoreTeam> findByStoreIdWithStoreAndTeam(@Param("storeId") Long storeId);

    @Query("""
            select st
            from StoreTeam st
            inner join FavoriteStore fs on fs.store = st.store and fs.user = :user
            left join UserTeam ut on ut.team = :team and ut.user = :user
            where 1=1
            and st.status = 'ACTIVE'
            and fs.status = 'ACTIVE'
            and ut.status = 'ACTIVE'
            """)
    Optional<StoreTeam> findFavoriteStoreByUserAndTeam(@Param("user") User user, @Param("team") Team team);

    @Query("""
            SELECT st
            FROM StoreTeam st
            left join UserTeam ut on ut.user = :user and ut.team = :team
            WHERE st.team = :team
            AND st.store = (
                SELECT pt.store
                FROM PointTransaction pt
                WHERE pt.user = :user AND pt.team = :team
                ORDER BY pt.createdAt DESC
              )
            AND st.status = 'ACTIVE'
            and ut.status = 'ACTIVE'
            """)
    StoreTeam findLastStoreTeamByUserAndTeam(@Param("user") User user, @Param("team") Team team);

    @Query(value = """
            select new com.jangburich.presentation.team.dto.response.TeamPrepaidStoreItem(
                st.store.id
                , st.store.name
                , st.prepaidExpirationDate
                , st.store.representativeImage
                , st.point
                , st.remainPoint
                , (fs.id is not null)
            )
            from StoreTeam st
            left join FavoriteStore fs on fs.store = st.store and fs.user = :user
            where (st.team = :team and st.team.status = 'ACTIVE')
            and (:keyword is null or (st.store.name like concat('%', :keyword, '%')))
            and st.status = 'ACTIVE'
            """)
    Page<TeamPrepaidStoreItem> findByTeamAndKeyword(@Param("user") User user, @Param("team") Team team, @Param("keyword") String keyword, Pageable pageable);

    @Query("""
            select st.team.id, sum(st.remainPoint)
            from StoreTeam st
            where st.team in :teams
            and st.status = 'ACTIVE'
            group by st.team.id
            order by st.team.id
            """)
    List<Object[]> findRemainingPointByTeams(@Param("teams") List<Team> teams);

    @Query("""
            select sum(st.point)
                        from StoreTeam st
                        where st.team = :team
                        and st.status = 'ACTIVE'
                        and st.team.status = 'ACTIVE'
            """)
    Integer sumPointByTeam(@Param("team") Team team);

    @Query("""
            select sum(st.remainPoint)
            from StoreTeam st
            where st.team = :team
            and st.status = 'ACTIVE'
            and st.team.status = 'ACTIVE'
            """)
    Integer sumRemainPointByTeam(@Param("team") Team team);

    @Query("""
            	select sum (st.remainPoint)
            	from StoreTeam st
            	inner join Team t on st.team = t
            	inner join UserTeam ut on ut.team = t
            	where ut.user = :user
            	and st.status = 'ACTIVE'
            	and t.status = 'ACTIVE'
            	and ut.status = 'ACTIVE'
            """)
    Integer sumRemainPointByUserAndStatus(@Param("user") User user);
}