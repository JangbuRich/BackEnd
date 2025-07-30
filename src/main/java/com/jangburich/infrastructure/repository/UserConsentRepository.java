package com.jangburich.infrastructure.repository;

import com.jangburich.domain.user.domain.UserConsent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConsentRepository extends JpaRepository<UserConsent, Long> {
}
