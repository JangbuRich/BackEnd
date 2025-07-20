package com.jangburich.presentation.store.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public final class StorePrepayTeamRequest {

    @Getter
    public static class ApprovalInfo {

        @JsonProperty("teamId")
        @NotBlank(message = "Team ID 는 필수 입니다.")
        private Long teamId;
    }
}
