package com.jangburich.domain.owner.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.user.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
	Optional<Owner> findByUser(User user);
}
