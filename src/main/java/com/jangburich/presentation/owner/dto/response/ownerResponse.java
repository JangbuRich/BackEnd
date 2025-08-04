package com.jangburich.presentation.owner.dto.response;

import lombok.Builder;
import lombok.Getter;

public final class ownerResponse {

    @Builder
    @Getter
    public static class StoreOwnerInfo {
        private String ownerName;
    }

    @Builder
    @Getter
    public static class StoreOwnerInfoDetail {
        private String email;
        private String phoneNumber;
        private String businessNo;
        private boolean agreeMarketing;
        private boolean agreeAdvertise;
        private boolean agreeEmail;
        private boolean agreeSms;
    }

}
