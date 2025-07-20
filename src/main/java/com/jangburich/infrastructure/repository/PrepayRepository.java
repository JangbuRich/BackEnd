package com.jangburich.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jangburich.domain.prepay.Prepay;

public interface PrepayRepository extends JpaRepository<Prepay, Long> {

    @Query("SELECT r FROM Prepay r JOIN FETCH r.store WHERE r.store.id = :storeId and r.prepayStatus = 'PENDING'")
    List<Prepay> findAllByStoreIdWithStore(@Param("storeId") Long storeId);

    Optional<Prepay> findByStoreIdAndTeamId(Long storeId, Long teamId);
}
