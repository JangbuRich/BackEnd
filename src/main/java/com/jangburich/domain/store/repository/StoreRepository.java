package com.jangburich.domain.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.store.domain.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryDslRepository {
	Optional<Store> findByOwner(Owner owner);
}
