package com.jangburich.domain.user.service.impl;

import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.repository.StoreRepository;
import com.jangburich.domain.user.domain.*;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.domain.user.service.SocialLoginService;
import com.jangburich.utils.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("!local")
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService {

    private final JwtManager jwtManager;

    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Value("${spring.jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${spring.jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    @Override
    public SocialLoginProvider getProvider(){
        return SocialLoginProvider.KAKAO;
    }

    @Override
    public SocialUserProfileDTO getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoApiResponseDTO> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request,
                KakaoApiResponseDTO.class);

        KakaoApiResponseDTO kakaoApiResponseDTO = response.getBody();

        return SocialUserProfileDTO.builder()
                .socialId(String.valueOf(kakaoApiResponseDTO.getId()))
                .email(kakaoApiResponseDTO.getKakaoAccount().getEmail())
                .name(kakaoApiResponseDTO.getKakaoAccount().getProfile().getNickname())
                .profileImage(kakaoApiResponseDTO.getKakaoAccount().getProfile().getProfileImageUrl())
                .provider(SocialLoginProvider.KAKAO)
                .build();
    }

    @Override
    @Transactional
    public TokenResponseDTO joinOwner(String kakaoAccessToken) {
        SocialUserProfileDTO userInfo = getUserInfo(kakaoAccessToken);

        User user = userRepository.findByProviderId("kakao_" + userInfo.getSocialId()).orElse(null);

        Boolean alreadyExists = false;
        if (user == null) {
            user = userRepository.save(User.create("kakao_" + userInfo.getSocialId(), userInfo.getName(),
                    userInfo.getEmail(), userInfo.getProfileImage(), "ROLE_OWNER"));
            Owner newOwner = ownerRepository.save(Owner.create(user));
            storeRepository.save(Store.create(newOwner));
        } else {
            alreadyExists = true;
        }

        String accessToken = jwtManager.createAccessToken(user.getProviderId(), user.getRole());
        String refreshToken = jwtManager.createRefreshToken();

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return TokenResponseDTO.builder()
                .alreadyExists(alreadyExists)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpires(accessTokenExpiration)
                .refreshTokenExpires(refreshTokenExpiration)
                .build();
    }

    @Override
    @Transactional
    public TokenResponseDTO joinUser(String kakaoaccessToken) {
        SocialUserProfileDTO userInfo = getUserInfo(kakaoaccessToken);
        User user = userRepository.findByProviderId("kakao_" + userInfo.getSocialId()).orElse(null);

        Boolean alreadyExists = false;
        if (user == null) {
            user = userRepository.save(User.create("kakao_" + userInfo.getSocialId(), userInfo.getName(),
                    userInfo.getEmail(), userInfo.getProfileImage(), "ROLE_USER"));
        } else {
            alreadyExists = true;
        }

        String accessToken = jwtManager.createAccessToken(user.getProviderId(), user.getRole());
        String refreshToken = jwtManager.createRefreshToken();

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return TokenResponseDTO.builder()
                .alreadyExists(alreadyExists)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpires(accessTokenExpiration)
                .refreshTokenExpires(refreshTokenExpiration)
                .build();
    }

    @Override
    @Transactional
    public TokenResponseDTO login(String accessToken) {
        SocialUserProfileDTO userInfo = getUserInfo(accessToken);

        User user = userRepository.findByProviderId("kakao_" + userInfo.getSocialId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        String newAccessToken = jwtManager.createAccessToken(user.getProviderId(), user.getRole());
        String newRefreshToken = jwtManager.createRefreshToken();

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .accessTokenExpires(accessTokenExpiration)
                .refreshTokenExpires(refreshTokenExpiration)
                .build();
    }
}
