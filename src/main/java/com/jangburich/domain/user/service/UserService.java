package com.jangburich.domain.user.service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.point.domain.PointTransaction;
import com.jangburich.domain.point.domain.TransactionType;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.repository.StoreRepository;
import com.jangburich.domain.user.domain.AdditionalInfoCreateDTO;
import com.jangburich.domain.user.domain.KakaoApiResponseDTO;
import com.jangburich.domain.user.domain.TokenResponseDTO;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.dto.response.PurchaseHistory;
import com.jangburich.domain.user.dto.response.UserHomeResponse;
import com.jangburich.domain.user.dto.response.WalletResponse;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.utils.JwtManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final JwtManager jwtManager;

    @Value("${spring.jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${spring.jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    public KakaoApiResponseDTO getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoApiResponseDTO> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request,
            KakaoApiResponseDTO.class);

        return response.getBody();
    }

    public User getUserInfos(String accessToken) {

        return userRepository.findByProviderId(accessToken)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));
    }

    @Transactional
    public TokenResponseDTO joinUser(String kakaoaccessToken) {
        KakaoApiResponseDTO userInfo = getUserInfo(kakaoaccessToken);
        User user = userRepository.findByProviderId("kakao_" + userInfo.getId()).orElse(null);

        Boolean alreadyExists = false;
        if (user == null) {
            user = userRepository.save(User.create("kakao_" + userInfo.getId(), userInfo.getProperties().getNickname(),
                userInfo.getKakaoAccount().getEmail(), userInfo.getProperties().getProfileImage(), "ROLE_USER"));
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

    @Transactional
    public TokenResponseDTO joinOwner(String kakaoAccessToken) {
        KakaoApiResponseDTO userInfo = getUserInfo(kakaoAccessToken);

        User user = userRepository.findByProviderId("kakao_" + userInfo.getId()).orElse(null);

        Boolean alreadyExists = false;
        if (user == null) {
            user = userRepository.save(User.create("kakao_" + userInfo.getId(), userInfo.getProperties().getNickname(),
                userInfo.getKakaoAccount().getEmail(), userInfo.getProperties().getProfileImage(), "ROLE_OWNER"));
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

    @Transactional
    public TokenResponseDTO login(String accessToken) {
        KakaoApiResponseDTO userInfo = getUserInfo(accessToken);

        User user = userRepository.findByProviderId("kakao_" + userInfo.getId())
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

    public WalletResponse getMyWallet(String userId) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        List<PointTransaction> transactions = pointTransactionRepository.findByUser(user);

        List<PurchaseHistory> purchaseHistories = transactions.stream()
            .filter(transaction -> transaction.getTransactionType() != TransactionType.FOOD_PURCHASE)
            .sorted(Comparator.comparing(PointTransaction::getCreatedAt).reversed())
            .map(transaction -> new PurchaseHistory(
                transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("MM.dd")),
                transaction.getTransactionType() == TransactionType.PREPAY
                    ? -transaction.getTransactionedPoint()
                    : transaction.getTransactionedPoint(),
                transaction.getStore() != null ? transaction.getStore().getName() : "장부리치 지갑",
                transaction.getTransactionType().getDisplayName()))
            .toList();

        return new WalletResponse(0, purchaseHistories);
    }


    public UserHomeResponse getUserHome(String userId) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        return userRepository.findUserHomeData(user.getUserId());
    }
}
