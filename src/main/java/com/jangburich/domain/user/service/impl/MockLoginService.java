package com.jangburich.domain.user.service.impl;

import com.jangburich.domain.owner.domain.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.repository.StoreRepository;
import com.jangburich.domain.user.domain.SocialLoginProvider;
import com.jangburich.domain.user.domain.SocialUserProfileDTO;
import com.jangburich.domain.user.domain.TokenResponseDTO;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.domain.user.service.SocialLoginService;
import com.jangburich.utils.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.jangburich.domain.user.domain.QUser.user;

@Service
@Profile("local")
@RequiredArgsConstructor
public class MockLoginService implements SocialLoginService {

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
        return SocialLoginProvider.LOCAL;
    }

    @Override
    public SocialUserProfileDTO getUserInfo(String accessToken){
        return SocialUserProfileDTO.builder()
                .socialId(accessToken)
                .email("fakeuser@test.com")
                .name("Fake User")
                .profileImage(null)
                .provider(SocialLoginProvider.LOCAL)
                .build();
    }

    @Override
    public TokenResponseDTO joinOwner(String mockAccessToken){
        SocialUserProfileDTO userInfo = getUserInfo(mockAccessToken);

        User user = userRepository.save(User.create("local_" + userInfo.getSocialId(), userInfo.getName(),
                userInfo.getEmail(), userInfo.getProfileImage(), "ROLE_OWNER"));
        Owner newOwner = ownerRepository.save(Owner.create(user));
        storeRepository.save(Store.create(newOwner));

        String accessToken = jwtManager.createAccessToken(user.getProviderId(), user.getRole());
        String refreshToken = jwtManager.createRefreshToken();

        user.updateRefreshToken(refreshToken);

        return TokenResponseDTO.builder()
                .alreadyExists(false)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpires(accessTokenExpiration)
                .refreshTokenExpires(refreshTokenExpiration)
                .build();
    }

    @Override
    public TokenResponseDTO joinUser(String mockAccessToken){
        SocialUserProfileDTO userInfo = getUserInfo(mockAccessToken);

        User user = userRepository.save(User.create("local_" + userInfo.getSocialId(), userInfo.getName(),
                userInfo.getEmail(), userInfo.getProfileImage(), "ROLE_USER"));

        String accessToken = jwtManager.createAccessToken(user.getProviderId(), user.getRole());
        String refreshToken = jwtManager.createRefreshToken();

        user.updateRefreshToken(refreshToken);

        return TokenResponseDTO.builder()
                .alreadyExists(false)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpires(accessTokenExpiration)
                .refreshTokenExpires(refreshTokenExpiration)
                .build();
    }

    @Override
    public TokenResponseDTO login(String accessToken){
        SocialUserProfileDTO userInfo = getUserInfo(accessToken);

        User user = userRepository.findByProviderId(userInfo.getSocialId())
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
