package com.jangburich.application.store.resolver.context;

import com.jangburich.domain.entity.Store;
import com.jangburich.domain.owner.Owner;
import com.jangburich.domain.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreContext {
    private final User user;
    private final Owner owner;
    private final Store store;

    public static StoreContext of(User user, Owner owner, Store store) {
        return StoreContext.builder()
            .user(user)
            .owner(owner)
            .store(store)
            .build();
    }
}
