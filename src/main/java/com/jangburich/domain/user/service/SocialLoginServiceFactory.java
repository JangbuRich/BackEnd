package com.jangburich.domain.user.service;

import com.jangburich.domain.user.domain.SocialLoginProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SocialLoginServiceFactory {
    private final Map<SocialLoginProvider, SocialLoginService> serviceMap;

    public SocialLoginServiceFactory(List<SocialLoginService> services) {
        this.serviceMap = services.stream()
                .collect(Collectors.toMap(SocialLoginService::getProvider, Function.identity()));
    }

    public SocialLoginService getService(SocialLoginProvider provider){
        return Optional.ofNullable(serviceMap.get(provider))
                .orElseThrow(()->new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: "+provider));
    }
}
