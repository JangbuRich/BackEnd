package com.jangburich.domain.owner.domain.controller.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

public final class OwnerResDto {

    @AllArgsConstructor
    @Getter
    public static class OwnerValidationResDto {
        private boolean valid;

        public static OwnerValidationResDto of (boolean result) {
            return new OwnerValidationResDto(result);
        }

    }
}
