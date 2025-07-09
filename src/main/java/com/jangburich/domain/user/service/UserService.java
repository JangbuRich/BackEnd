package com.jangburich.domain.user.service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.domain.point.domain.PointTransaction;
import com.jangburich.domain.point.domain.TransactionType;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.domain.user.domain.AdditionalInfoCreateDTO;
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
    private final PointTransactionRepository pointTransactionRepository;
    private final JwtManager jwtManager;

    public User getUserInfos(String accessToken) {
        return userRepository.findByProviderId(accessToken)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));
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
