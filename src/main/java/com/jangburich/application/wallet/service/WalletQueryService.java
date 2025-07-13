package com.jangburich.application.wallet.service;

import com.jangburich.domain.point.domain.PointTransaction;
import com.jangburich.domain.point.domain.TransactionType;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.presentation.wallet.dto.response.PurchaseHistory;
import com.jangburich.presentation.wallet.dto.response.WalletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletQueryService {

    private final PointTransactionRepository pointTransactionRepository;
    private final UserRepository userRepository;

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

}
