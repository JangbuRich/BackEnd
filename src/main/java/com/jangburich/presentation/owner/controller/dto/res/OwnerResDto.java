package com.jangburich.presentation.owner.controller.dto.res;

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
