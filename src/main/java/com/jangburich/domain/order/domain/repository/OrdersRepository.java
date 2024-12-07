package com.jangburich.domain.order.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.jangburich.domain.order.domain.OrderStatus;
import com.jangburich.domain.order.domain.Orders;
import com.jangburich.domain.order.dto.response.OrderResponse;
import com.jangburich.domain.team.domain.Team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersQueryDslRepository {
    @Query(value = "SELECT * FROM orders WHERE store_id = :storeId AND updated_at < :updatedAt AND order_status = :orderStatus",
            nativeQuery = true)
    List<Orders> findOrdersByStoreAndDateAndStatusNative(
            @Param("storeId") Long storeId,
            @Param("updatedAt") LocalDateTime updatedAt,
            @Param("orderStatus") String orderStatus);

    @Query("SELECT o FROM Orders o " +
            "WHERE o.store.id = :storeId " +
            "AND o.updatedAt >= :startOfDay " +
            "AND o.updatedAt < :endOfDay " +
            "AND o.orderStatus = :orderStatus "
            + "ORDER BY o.createdAt DESC")
    List<Orders> findOrdersByStoreAndTodayDateAndStatus(
            @Param("storeId") Long storeId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("orderStatus") OrderStatus orderStatus);

    List<Orders> findAllByTeam(Team team);
}
