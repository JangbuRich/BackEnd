package com.jangburich.domain.owner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HomeMode {
    GENERAL_MODE("일반모드"),
    SIMPLE_MODE("간편모드")
    ;

    private final String description;
}
