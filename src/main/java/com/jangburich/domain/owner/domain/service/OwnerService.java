package com.jangburich.domain.owner.domain.service;

import org.springframework.stereotype.Service;

import com.jangburich.domain.owner.domain.Owner;
import com.jangburich.domain.owner.domain.OwnerCreateReqDTO;
import com.jangburich.domain.owner.domain.OwnerGetResDTO;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerService {

	private final OwnerRepository ownerRepository;
	private final UserRepository userRepository;

	@Transactional
	public void registerOwner(String customOAuthUser, OwnerCreateReqDTO ownerCreateReqDTO) {
		User user = userRepository.findByProviderId(customOAuthUser)
			.orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

		Owner owner = ownerRepository.findByUser(user)
			.orElseThrow(() -> new DefaultNullPointerException(
				ErrorCode.INVALID_AUTHENTICATION));

		owner.register(
			ownerCreateReqDTO.getName(),
			ownerCreateReqDTO.getBusinessRegistrationNumber(),
			owner.getBusinessName(),
			owner.getOpeningDate(),
			owner.getPhoneNumber()
		);
	}

	public OwnerGetResDTO getOwnerInfo(String customOAuthUser) {
		User user = userRepository.findByProviderId(customOAuthUser)
			.orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

		Owner owner = ownerRepository.findByUser(user)
			.orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));
		return OwnerGetResDTO.of(owner);
	}
}
