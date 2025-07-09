package com.jangburich.application.service.prepay;

import com.jangburich.domain.entity.Store;
import com.jangburich.domain.entity.StoreTeam;
import com.jangburich.domain.point.domain.PointTransaction;
import com.jangburich.domain.point.domain.TransactionType;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.domain.repository.StoreRepository;
import com.jangburich.domain.repository.StoreTeamRepository;
import com.jangburich.domain.team.domain.Team;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.user.repository.UserRepository;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.global.payload.Message;
import com.jangburich.presentation.prepay.dto.request.PrepayRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrepayCommandService {

    private final PointTransactionRepository pointTransactionRepository;
    private final StoreRepository storeRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public Message prepay(String userId, PrepayRequest prepayRequest) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Team team = teamRepository.findById(prepayRequest.teamId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 팀 id 입니다."));

        Store store = storeRepository.findById(prepayRequest.storeId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 가게 id 입니다."));

        team.validateTeamLeader(user.getUserId());

        if (!team.getTeamLeader().getLeaderId().equals(user.getUserId())) {
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
                .personalAllocatedPoint(0)
                .remainPoint(prepayRequest.prepayAmount())
                .prepaidExpirationDate(expirationDate)
                .build();

        Optional<StoreTeam> storeAndTeam = storeTeamRepository.findByStoreAndTeam(store, team);

        if (storeAndTeam.isEmpty()) {
            storeAndTeam = Optional.of(storeTeamRepository.save(buildedStoreTeam));
        }

        StoreTeam storeTeam = storeAndTeam.get();
        storeTeam.recharge(prepayRequest.prepayAmount());

        return Message.builder()
                .message("매장 선결제가 완료되었습니다.")
                .build();
    }
}
