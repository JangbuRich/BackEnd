package com.jangburich.infrastructure.repository;

import com.jangburich.domain.entity.FavoriteStore;
import com.jangburich.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore, Long> {
    Optional<FavoriteStore> findByUser(User user);
}
