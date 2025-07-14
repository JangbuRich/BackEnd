package com.jangburich.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.domain.entity.OrderStatus;
import com.jangburich.domain.entity.Orders;
import com.jangburich.domain.team.domain.Team;

import com.jangburich.domain.user.domain.User;
import com.jangburich.presentation.wallet.dto.response.PointTransactionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("""
    SELECT new com.jangburich.presentation.wallet.dto.response.PointTransactionItem(
        o.id
        , o.store.id
        , o.store.name
        , o.store.category
        , o.orderPrice
        , o.orderStatus
        , o.updatedAt
    )
    FROM Orders o
    WHERE o.user = :user
    AND (:createdAfter IS NULL OR o.createdAt >= :createdAfter)
    AND (:createdBefore IS NULL OR o.createdAt <= :createdBefore)
""")
    Page<PointTransactionItem> findAllTicketByCreatedAfterAndCreatedBefore(@Param("user")User user, @Param("createdAfter")LocalDateTime createdAfter, @Param("createdBefore")LocalDateTime createdBefore, Pageable pageable);

    Page<Orders> findByUserAndOrderStatus(User user, OrderStatus orderStatus, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE store_id = :storeId AND updated_at < :updatedAt AND order_status in :orderStatus order by id desc",
            nativeQuery = true)
    List<Orders> findOrdersByStoreAndDateAndStatusNative(
            @Param("storeId") Long storeId,
            @Param("updatedAt") LocalDateTime updatedAt,
            @Param("orderStatus") List<OrderStatus> orderStatus);

    @Query("SELECT o FROM Orders o " +
            "WHERE o.store.id = :storeId " +
            "AND o.updatedAt >= :startOfDay " +
            "AND o.updatedAt < :endOfDay " +
            "AND o.orderStatus in :orderStatus "
            + "ORDER BY o.createdAt DESC limit 6")
    List<Orders> findOrdersByStoreAndTodayDateAndStatus(
            @Param("storeId") Long storeId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("orderStatus") List<OrderStatus> orderStatus);

    List<Orders> findAllByTeam(Team team);
}

