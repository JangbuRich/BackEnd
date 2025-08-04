package com.jangburich.application.store.resolver;

import com.jangburich.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;

import com.jangburich.application.store.resolver.context.StoreContext;
import com.jangburich.domain.entity.Store;
import com.jangburich.domain.owner.Owner;
import com.jangburich.infrastructure.repository.OwnerRepository;
import com.jangburich.infrastructure.repository.StoreRepository;
import com.jangburich.domain.user.domain.User;
import com.jangburich.global.error.DefaultNullPointerException;
import com.jangburich.global.payload.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StoreResolver {
    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    // TODO: 사실 위 상황은 3중 join 을 통하여 한번에 조회하는게 더 좋기는 하다... -> queryDSL 로 교체하기.
    public Store getStoreByUserId(String userId) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        return storeRepository.findByOwner(owner)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));
    }

    public StoreContext getStoreContext(String userId) {
        User user = userRepository.findByProviderId(userId)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Owner owner = ownerRepository.findByUser(user)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        Store store = storeRepository.findByOwner(owner)
                .orElseThrow(() -> new DefaultNullPointerException(ErrorCode.INVALID_AUTHENTICATION));

        return StoreContext.of(user, owner, store);
    }

}
