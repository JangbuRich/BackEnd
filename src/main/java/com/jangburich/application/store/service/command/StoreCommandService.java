package com.jangburich.application.store.service.command;

import com.jangburich.domain.common.Status;
import com.jangburich.domain.entity.FavoriteStore;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.owner.domain.entity.Owner;
import com.jangburich.domain.owner.domain.repository.OwnerRepository;
import com.jangburich.global.error.DefaultException;
import com.jangburich.infrastructure.repository.FavoriteStoreRepository;
import com.jangburich.infrastructure.repository.UserRepository;
import com.jangburich.presentation.store.dtos.request.StoreAdditionalInfoCreateRequest;
import com.jangburich.presentation.store.dtos.request.StoreCreateRequest;
import com.jangburich.presentation.store.dtos.request.StoreUpdateRequest;
import com.jangburich.presentation.store.dtos.response.store.StoreCreateResponseDto;
import com.jangburich.infrastructure.repository.StoreRepository;
import com.jangburich.application.store.provider.RandomNumberProvider;
import com.jangburich.domain.user.domain.User;
import com.jangburich.infrastructure.config.s3.S3Service;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.utils.DayOfWeekConverter;
import lombok.RequiredArgsConstructor;
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

    private final FavoriteStoreRepository favoriteStoreRepository;
    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void createFavoriteStore(String userId, Long storeId){
        User user = userRepository.findByProviderId(userId).orElseThrow(()->new DefaultException(ErrorCode.INVALID_USER_ID));

        Store store = storeRepository.findById(storeId).orElseThrow(()-> new DefaultException(ErrorCode.INVALID_STORE_ID));

        favoriteStoreRepository.save(FavoriteStore.of(user, store));
    }

    @Transactional
    public void deleteFavoriteStore(String userId, Long storeId){
        User user = userRepository.findByProviderId(userId).orElseThrow(()->new DefaultException(ErrorCode.INVALID_USER_ID));

        Store store = storeRepository.findById(storeId).orElseThrow(()-> new DefaultException(ErrorCode.INVALID_STORE_ID));

        FavoriteStore favoriteStore = favoriteStoreRepository.findByStoreAndUserAndStatus(store, user, Status.ACTIVE).orElseThrow(()->new DefaultException(ErrorCode.INVALID_CHECK));

        favoriteStore.updateStatus(Status.INACTIVE);
    }

    private String createStoreId() {
        String datePrefix = createDatePrefix();
        String eightDigitNumber = randomNumberProvider.createEightDigitNumber();

        return datePrefix + eightDigitNumber;
    }

    private String createDatePrefix() {
        return LocalDate.parse(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)).toString();
    }
}