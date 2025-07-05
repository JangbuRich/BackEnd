package com.jangburich.domain.owner.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.domain.owner.domain.controller.dto.req.OwnerCreateReqDTO;
import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OwnerCommandService {
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    public void registerOwner(String customOAuthUser, OwnerCreateReqDTO ownerCreateReqDTO) {
        log.info("OAuthUser : {}", customOAuthUser);

        User user = userRepository.findByProviderId(customOAuthUser)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        owner.register(
            ownerCreateReqDTO.getName(),
            ownerCreateReqDTO.getBusinessRegistrationNumber(),
            owner.getBusinessName(),
            owner.getOpeningDate(),
            owner.getPhoneNumber()
        );

        ownerRepository.save(owner);
    }

}
