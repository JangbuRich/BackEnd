package com.jangburich.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.domain.user.domain.AdditionalInfoCreateDTO;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.dto.response.UserHomeResponse;
import com.jangburich.domain.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.utils.JwtManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtManager jwtManager;

    public User getUserInfos(String accessToken) {
        return userRepository.findByProviderId(accessToken)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));
    }

    public UserHomeResponse getUserHome(String userId) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        return userRepository.findUserHomeData(user.getUserId());
    }

    @Transactional
    public String reissueAccessToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

        if (!jwtManager.isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다.");
        }

        return jwtManager.createAccessToken(user.getProviderId(), user.getRole());
    }

    @Transactional
    public void additionalInfo(String userId, AdditionalInfoCreateDTO additionalInfoCreateDTO) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        user.additionalInfo(
                additionalInfoCreateDTO.getName(),
                additionalInfoCreateDTO.getPhoneNum(),
                additionalInfoCreateDTO.getAgreeAdvertisement(),
                additionalInfoCreateDTO.getAgreeMarketing()
        );
    }
}
