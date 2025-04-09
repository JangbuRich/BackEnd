package com.jangburich.domain.store.repository;

import com.jangburich.domain.team.domain.Team;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.domain.StoreTeam;
import com.jangburich.domain.store.dto.response.StoreTeamResponseDTO;

public interface StoreTeamRepository extends JpaRepository<StoreTeam, Long> {
	Optional<StoreTeam> findByStoreIdAndTeamId(Long store_id, Long team_id);

	List<StoreTeamResponseDTO> findAllByStore(Store store);

    Optional<StoreTeam> findByStoreAndTeam(Store store, Team team);

	List<StoreTeamResponseDTO> findAllByStoreOrderByCreatedAtDesc(Store store);
}