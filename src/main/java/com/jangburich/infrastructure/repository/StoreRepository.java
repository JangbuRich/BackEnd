package com.jangburich.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import com.jangburich.domain.user.domain.User;
import com.jangburich.presentation.user.dto.response.StoreItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByOwner (Owner owner);

    @Query("""
        select new com.jangburich.presentation.user.dto.response.StoreItem(
        	s.id
        	, s.name
        	, st.remainPoint
        	, st.point
        	, s.representativeImage
        	, (max(fs.id) is not null)
        	, st.team.name
        )
        from Store s
        left join FavoriteStore  fs on fs.store = s
        inner join StoreTeam st on st.store = s
        inner join UserTeam ut on st.team = ut.team
        where ut.user = :user
        and (:liked = false or (fs.id is not null and fs.user = :user))
        group by s.id, s.name, st.remainPoint, st.point, s.representativeImage, st.team.name
        """)
    Page<StoreItem> findAllByUser (@Param("user") User user, @Param("liked") boolean liked, Pageable pageable);

    @Query("""
        select s
        from Store s
        left join FavoriteStore fs on (fs.store = s)
        where fs.id is not null
        """
    )
    List<Store> finfd (@Param("user") long user);

}