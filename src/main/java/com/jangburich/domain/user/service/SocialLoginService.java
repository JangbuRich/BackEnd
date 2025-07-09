package com.jangburich.domain.user.service;

import com.jangburich.domain.user.domain.SocialLoginProvider;
import com.jangburich.domain.user.domain.SocialUserProfileDTO;
import com.jangburich.domain.user.domain.TokenResponseDTO;

public interface SocialLoginService {
    SocialLoginProvider getProvider();
    SocialUserProfileDTO getUserInfo(String accessToken);
    TokenResponseDTO login(String accessToken);
    TokenResponseDTO joinUser(String accessToken);
    TokenResponseDTO joinOwner(String accessToken);
}
