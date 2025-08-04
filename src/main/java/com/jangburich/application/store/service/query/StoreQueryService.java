package com.jangburich.application.store.service.query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.jangburich.domain.entity.*;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.payload.PageInfo;
import com.jangburich.infrastructure.repository.*;
import com.jangburich.infrastructure.repository.queryDsl.StoreQueryDslRepository;
import com.jangburich.presentation.store.dtos.response.store.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jangburich.application.store.resolver.StoreResolver;
import com.jangburich.presentation.store.dtos.response.store.view.StoreHomeResponse;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.utils.DateTimeFormatterUtil;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreQueryService {
    // StoreQueryService 가 목적에 맞지 않는 너무 많은 의존성을 가지고 있음 -> 분리해야함.
    // Todo: Order 관련 로직들은 Order 패키지로 옮겨져야 하는게 맞음 -> 추후 이동하기.
    // Todo: Team(group) 관련 로직들은 Team 패키지로 옮겨져야 하는게 맞음

    private final CustomOrderRepository customOrderRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final StoreRepository storeRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final UserRepository userRepository;

    private final StoreQueryDslRepository storeQueryDslRepository;

    private final StoreResolver storeResolver;

    /**
     * Home 화면 매장 고유 Code 를 반환한다.
     *
     * @param authentication Authentication
     * @return StoreHomeResponse.UniqueCode
     */
    public StoreHomeResponse.UniqueCode getStoreUniqueCode(String authentication) {
        Store store = storeResolver.getStoreByUserId(authentication);

        String storeUniqueCode = store.getStoreUniqueCode();

        return StoreHomeResponse.UniqueCode.builder().uniqueCode(storeUniqueCode).build();
    }

    /**
     * 나의 장부를 보여준다.
     *
     * @param authentication Authentication ID
     * @return StoreHomeResponse.AccountInfo - 장부 DTO
     */
    public StoreHomeResponse.AccountInfo getStoreAccountInfo(String authentication) {
        Store store = storeResolver.getStoreByUserId(authentication);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<Orders> ordersByStoreAndDate = customOrderRepository.queryOrdersByStoreIdAndStartDateAndEndDate(store.getId(), startOfDay, endOfDay);
        int totalOrderPrice = getTotalOrderPrice(ordersByStoreAndDate);

        return StoreHomeResponse.AccountInfo.builder().today(DateTimeFormatterUtil.formatToKoreanDateTime(LocalDateTime.now())).todayTotalOrderCount(ordersByStoreAndDate.size()).todayTotalOrderPrice(totalOrderPrice).totalPrepayPrice(0).newPrepayPrice(0).newPrepayGroup(0).build();
    }

    public StoreGetResponse getStoreInfo(String authentication) {
        Store store = storeResolver.getStoreByUserId(authentication);

        if (!store.getOwner().getUser().getProviderId().equals(authentication)) {
            throw new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION);
        }

        return new StoreGetResponse().of(store);
    }

    public List<StoreChargeHistoryResponse> getPaymentHistory(String userId) {
        Store store = storeResolver.getStoreByUserId(userId);

        return pointTransactionRepository.findAllByStore(store).stream().sorted(Comparator.comparing(StoreChargeHistoryResponse::createdAt).reversed()) // 최신순 정렬
                .toList();
    }

    /**
     * @param authentication Authentication ID
     * @param searchRadius   조회 반경 거리
     * @param category       매장 카테고리
     * @param lat            사용자 현재 위도
     * @param lon            사용자 현재 경도
     * @param pageable       페이지 정보
     * @return StoreListResponse - 가게 리스트 DTO
     */
    public StoreListResponse getStoreListByCategory(final String authentication, final Integer searchRadius, final Category category, Double lat, Double lon, final Pageable pageable) {
        User user = userRepository.findByProviderId(authentication).orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Page<StoreListItem> storeListItemPage = storeQueryDslRepository.findStoresByCategory(user.getUserId(), searchRadius, category, lat, lon, pageable);

        PageInfo pageInfo = new PageInfo(storeListItemPage.getNumber(), storeListItemPage.getSize(), storeListItemPage.getTotalPages(), storeListItemPage.getTotalElements(), storeListItemPage.hasNext(), storeListItemPage.hasPrevious());

        return new StoreListResponse(storeListItemPage.stream().toList(), pageInfo);
    }

    /**
     * @param authentication AuthenticationId
     * @param keyword        검색어
     * @param pageable       페이지 정보
     * @return StoreListResponse - 가게 리스트 DTO
     */
    public StoreListResponse searchStores(final String authentication, final String keyword, final Pageable pageable) {
        User user = userRepository.findByProviderId(authentication).orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Page<StoreListItem> storeListItemPage = storeQueryDslRepository.findStores(user.getUserId(), keyword, pageable);

        PageInfo pageInfo = new PageInfo(storeListItemPage.getNumber(), storeListItemPage.getSize(), storeListItemPage.getTotalPages(), storeListItemPage.getTotalElements(), storeListItemPage.hasNext(), storeListItemPage.hasPrevious());

        return new StoreListResponse(storeListItemPage.stream().toList(), pageInfo);
    }

    /**
     * @param authentication AuthenticationId
     * @param storeId        가게 ID
     * @return StoreDetailsResponse - 가게 상세 조회 DTO
     */
    public StoreDetailsResponse getStoreDetail(final String authentication, final Long storeId) {
        User user = userRepository.findByProviderId(authentication).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_USER_ID));

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new DefaultException(ErrorCode.INVALID_STORE_ID));

        Optional<FavoriteStore> favoriteStore = favoriteStoreRepository.findByStoreAndUser(store, user);

        List<StoreTeam> storeTeamList = storeTeamRepository.findAllByStoreAndUser(store, user);

        Boolean groupAvailable = !storeTeamList.isEmpty();

        Boolean isLeader = Boolean.FALSE;
        if (groupAvailable) {
            for (StoreTeam storeTeam : storeTeamList) {
                if (storeTeam.getTeam().getTeamLeader().getLeaderId().equals(user.getUserId())) {
                    isLeader = Boolean.TRUE;
                    break;
                }
            }
        }

        return new StoreDetailsResponse(store.getId(), store.getName(), favoriteStore.isPresent(), groupAvailable, isLeader, store.getCategory().name(), store.getAddress(), DateTimeFormatterUtil.formatToKoreanTime(store.getCloseTime()), store.getContactNumber(), store.getRepresentativeImage(), null);
    }

    private int getTotalOrderPrice(List<Orders> ordersByStoreAndDate) {
        return ordersByStoreAndDate.stream().mapToInt(Orders::getOrderPrice).sum();
    }
}
