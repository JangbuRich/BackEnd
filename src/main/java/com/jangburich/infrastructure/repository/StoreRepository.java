package com.jangburich.infrastructure.repository;

import java.util.Optional;

import com.jangburich.infrastructure.repository.queryDsl.StoreQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
	Optional<Store> findByOwner(Owner owner);
}
