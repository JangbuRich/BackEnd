package com.jangburich.application.user.service;

import com.jangburich.domain.entity.Store;
import com.jangburich.domain.repository.StoreRepository;
import com.jangburich.domain.repository.UserRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.error.DefaultException;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;
import com.jangburich.global.payload.PageInfo;
import com.jangburich.presentation.user.dto.response.StoreItem;
import com.jangburich.presentation.user.dto.response.StoreListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreListResponse getStoreList(String userId, boolean liked, Pageable pageable){
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_USER_ID));

        List<Store> strores = storeRepository.finfd(user.getUserId());
        for (Store store:strores
             ) {
            System.out.println(store.getName());
        }

        Page<StoreItem> storeItemPage = storeRepository.findAllByUser(user, liked, pageable);

        if(liked && storeItemPage.getTotalElements() > 1){
            throw new DefaultException(ErrorCode.INVALID_CHECK);
        }

        PageInfo pageInfo = new PageInfo(storeItemPage.getNumber(), storeItemPage.getSize(), storeItemPage.getTotalPages(), storeItemPage.getTotalElements(), storeItemPage.hasNext(), storeItemPage.hasPrevious());

        return new StoreListResponse(storeItemPage.toList(), pageInfo);
    }
}
