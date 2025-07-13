package com.jangburich.application.store.service.command;

import com.jangburich.domain.entity.Category;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.repository.OrdersRepository;
import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.domain.payment.domain.repository.TeamChargeHistoryRepository;
import com.jangburich.domain.point.domain.repository.PointTransactionRepository;
import com.jangburich.presentation.store.dtos.request.StoreAdditionalInfoCreateRequest;
import com.jangburich.presentation.store.dtos.request.StoreCreateRequest;
import com.jangburich.presentation.store.dtos.request.StoreUpdateRequest;
import com.jangburich.presentation.store.dtos.response.store.SearchStoresResponse;
import com.jangburich.presentation.store.dtos.response.store.StoreCreateResponseDto;
import com.jangburich.domain.repository.StoreRepository;
import com.jangburich.domain.repository.StoreTeamRepository;
import com.jangburich.application.store.provider.RandomNumberProvider;
import com.jangburich.domain.team.domain.repository.TeamRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.domain.repository.UserRepository;
import com.jangburich.infrastructure.config.s3.S3Service;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.utils.DayOfWeekConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreCommandService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final StoreTeamRepository storeTeamRepository;
    private final TeamRepository teamRepository;
    private final TeamChargeHistoryRepository teamChargeHistoryRepository;
    private final OrdersRepository ordersRepository;
    private final PointTransactionRepository pointTransactionRepository;

    private final S3Service s3Service;

    private final RandomNumberProvider randomNumberProvider;

    @Transactional
    public StoreCreateResponseDto createStore(String authentication, StoreCreateRequest storeCreateRequest, MultipartFile image,
                            List<MultipartFile> menuImages) {

        try {
            User user = userRepository.findByProviderId(authentication)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

            Owner owner = ownerRepository.findByUser(user)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

            List<DayOfWeek> dayOfWeeks = DayOfWeekConverter.convertStringToDayOfWeekList(
                storeCreateRequest.getDayOfWeek());

            String imageUrl = s3Service.uploadImageToS3(image);

            Store store = storeRepository.save(Store.of(owner, storeCreateRequest, dayOfWeeks, imageUrl));

            String issueCode = randomNumberProvider.createFourDigitNumber();
            store.createUniqueStoreCode(issueCode);

            String storeId = createStoreId();
            store.createStoreId(storeId);

            return new StoreCreateResponseDto(issueCode, "Success");
        } catch (Exception e) {
            return new StoreCreateResponseDto(null, e.getMessage());
        }

    }

    @Transactional
    public void createAdditionalInfo(String authentication,
                                     StoreAdditionalInfoCreateRequest storeAdditionalInfoCreateRequest) {
        User user = userRepository.findByProviderId(authentication)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        store.additionalInfo(
            storeAdditionalInfoCreateRequest.getReservationAvailable(),
            storeAdditionalInfoCreateRequest.getMinPrepayment(),
            storeAdditionalInfoCreateRequest.getMaxReservation(),
            storeAdditionalInfoCreateRequest.getPrepaymentDuration()
        );
    }

    @Transactional
    public void updateStore(String userId, StoreUpdateRequest storeUpdateRequest) {
        User user = userRepository.findByProviderId(userId)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        if (!store.getOwner().getUser().getProviderId().equals(userId)) {
            throw new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION);
        }

        storeRepository.save(updateStore(store, storeUpdateRequest));
    }

    @Transactional
    public Store updateStore(Store store, StoreUpdateRequest storeUpdateRequest) {
        store.update(storeUpdateRequest);
        return store;
    }

    public Page<SearchStoresResponse> searchByCategory(final String authentication, final Integer searchRadius,
                                                       final Category category, Double lat, Double lon, final Pageable pageable) {
        User user = userRepository.findByProviderId(authentication)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));
        return storeRepository.findStoresByCategory(user.getUserId(), searchRadius, category, lat, lon,
            pageable);
    }

    public Page<SearchStoresResponse> searchStores(final String authentication, final String keyword,
                                                   final Pageable pageable) {
        User user = userRepository.findByProviderId(authentication)
            .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));
        return storeRepository.findStores(user.getUserId(), keyword, pageable);
    }

    private String createStoreId () {
        String datePrefix = createDatePrefix();
        String eightDigitNumber = randomNumberProvider.createEightDigitNumber();

        return datePrefix + eightDigitNumber;
    }

    private String createDatePrefix () {
        return LocalDate.parse(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)).toString();
    }
}