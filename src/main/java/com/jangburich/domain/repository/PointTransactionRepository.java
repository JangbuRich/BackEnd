package com.jangburich.domain.repository;

import com.jangburich.domain.entity.PointTransaction;
import com.jangburich.domain.entity.Store;
import com.jangburich.presentation.store.dtos.response.store.StoreChargeHistoryResponse;
import com.jangburich.domain.user.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.presentation.wallet.dto.response.PointTransactionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByUser(User user);

    List<StoreChargeHistoryResponse> findAllByStore(Store store);

    @Query("""
    SELECT new com.jangburich.presentation.wallet.dto.response.PointTransactionItem(
        pt.id
        , pt.store.id
        , pt.store.name
        , pt.store.category
        , pt.transactionedPoint
        , null
        , pt.updatedAt
    )
    FROM PointTransaction pt
    WHERE pt.transactionType = "PREPAY"
    And pt.user = :user
    AND (:createdAfter IS NULL OR pt.createdAt >= :createdAfter)
    AND (:createdBefore IS NULL OR pt.createdAt <= :createdBefore)
""")
    Page<PointTransactionItem> findAllPrepayByCreatedAfterAndCreatedBefore(@Param("user") User user, @Param("createdAfter") LocalDateTime createdAfter, @Param("createdBefore") LocalDateTime createdBefore, Pageable pageable);
}