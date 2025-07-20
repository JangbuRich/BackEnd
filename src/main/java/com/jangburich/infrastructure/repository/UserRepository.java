package com.jangburich.infrastructure.repository;

import java.util.Optional;

import com.jangburich.domain.repository.queryDsl.UserQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jangburich.domain.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserQueryDslRepository {
	Optional<User> findByProviderId(String providerId);

	Optional<User> findByRefreshToken(String token);
}
