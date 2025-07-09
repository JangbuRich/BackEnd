package com.jangburich.domain.repository;

import java.util.Optional;

import com.jangburich.domain.repository.queryDsl.StoreQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryDslRepository {
	Optional<Store> findByOwner(Owner owner);
}
