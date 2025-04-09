package com.jangburich.domain.point.domain.repository;

import com.jangburich.domain.point.domain.PointTransaction;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.dto.response.StoreChargeHistoryResponse;
import com.jangburich.domain.user.domain.User;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByUser(User user);

    List<StoreChargeHistoryResponse> findAllByStore(Store store);
}