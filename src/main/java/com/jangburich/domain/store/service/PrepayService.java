package com.jangburich.domain.store.service;

import com.jangburich.domain.point.domain.PointTransaction;
import com.jangburich.domain.point.domain.TransactionType;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.domain.store.domain.Store;
import com.jangburich.domain.store.domain.StoreTeam;
import com.jangburich.domain.store.dto.request.PrepayRequest;
import com.jangburich.domain.store.dto.response.PrepaymentInfoResponse;
import com.jangburich.domain.store.repository.StoreRepository;
import com.jangburich.domain.store.repository.StoreTeamRepository;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrepayService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final TeamRepository teamRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final PointTransactionRepository pointTransactionRepository;

    @Transactional
    public Message prepay(String userId, PrepayRequest prepayRequest) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Team team = teamRepository.findById(prepayRequest.teamId())
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 팀 id 입니다."));

        Store store = storeRepository.findById(prepayRequest.storeId())
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 id 입니다."));

        team.validateIsTeamLeader(team.getTeamLeader().getUser_id(), user.getUserId());

        if (!team.getTeamLeader().getUser_id().equals(user.getUserId())) {
            return Message.builder()
                .message("팀의 리더가 아닌 사람은 선결제를 할 수 없습니다.")
                .build();
        }

        PointTransaction pointTransaction = PointTransaction
            .builder()
            .transactionType(TransactionType.PREPAY)
            .transactionedPoint(prepayRequest.prepayAmount())
            .user(user)
            .store(store)
            .team(team)
            .build();

        pointTransactionRepository.save(pointTransaction);

        LocalDate expirationDate = LocalDate.now().plusDays(store.getPrepaymentDuration());

        StoreTeam buildedStoreTeam = StoreTeam
            .builder()
            .team(team)
            .store(store)
            .point(prepayRequest.prepayAmount())
            .personalAllocatedPoint(prepayRequest.personalAllocatedAmount())
            .remainPoint(prepayRequest.prepayAmount())
            .prepaidExpirationDate(expirationDate)
            .build();

        Optional<StoreTeam> storeAndTeam = storeTeamRepository.findByStoreAndTeam(store, team);

        if (storeAndTeam.isEmpty()) {
            storeAndTeam = Optional.of(storeTeamRepository.save(buildedStoreTeam));
        }

        StoreTeam storeTeam = storeAndTeam.get();
        storeTeam.recharge(prepayRequest.prepayAmount());

        // TODO 주석 처리
        // storeTeam.setPersonalAllocatedPoint(prepayRequest.personalAllocatedAmount());

//        if (storeTeam.getPrepayCount() != null) {
//            storeTeam.setPrepayCount(storeTeam.getPrepayCount() + 1);
//        } else {
//            storeTeam.setPrepayCount(1);
//        }

        return Message.builder()
            .message("매장 선결제가 완료되었습니다.")
            .build();
    }

    @Transactional
    public PrepaymentInfoResponse getPrepayInfo(String userId, Long storeId, Long teamId) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        StoreTeam storeTeam = storeTeamRepository.findByStoreIdAndTeamId(storeId, teamId)
            .orElse(null);

        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 id 입니다."));

        Integer remainPrepay = 0;
        if (storeTeam != null) {
            remainPrepay = storeTeam.getRemainPoint();
        }

        return PrepaymentInfoResponse.builder()
            .remainPrepay(remainPrepay)
            .minPrepayAmount(store.getMinPrepayment())
            .wallet(0) // TODO 수정 필요
            .category(store.getCategory().getDisplayName())
            .storeName(store.getName())
            .build();
    }
}