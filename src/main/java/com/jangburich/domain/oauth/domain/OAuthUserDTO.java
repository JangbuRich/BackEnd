package com.jangburich.domain.oauth.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OAuthUserDTO {
    private String userId;
    private String nickname;
    private String image;
    private String role;
    private LocalDateTime createdAt;

    @Builder
    public OAuthUserDTO(String userId, String nickname, String image, String role, LocalDateTime createdAt) {
        this.userId = userId;
        this.nickname = nickname;
        this.image = image;
        this.role = role;
        this.createdAt = createdAt;
    }
}
