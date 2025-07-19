package com.jangburich.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.jangburich.domain.entity.OrderStatus;
import com.jangburich.domain.entity.Orders;
import com.jangburich.domain.entity.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
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
