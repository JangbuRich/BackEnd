package com.jangburich.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialUserProfileDTO {
    @JsonProperty("social_id")
    private String socialId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("provider")
    private SocialLoginProvider provider;
}
