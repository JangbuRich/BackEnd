package com.jangburich.domain.team.dto.response;

import lombok.Getter;

@Getter
public class TeamSecretCodeResponse {
    private final String uuid;

    public TeamSecretCodeResponse(String uuid) {
        this.uuid = uuid;
    }
}
