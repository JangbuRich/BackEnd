package com.jangburich.infrastructure.repository;

import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.domain.UserConsent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConsentRepository extends JpaRepository<UserConsent, Long> {
    Optional<UserConsent> findByUser(User user);
}
