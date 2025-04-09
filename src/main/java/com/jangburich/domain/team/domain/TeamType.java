package com.jangburich.domain.team.domain;

import lombok.Getter;

@Getter
public enum TeamType {
    INDIVIDUAL("개인"),
    GATHERING("모임"),
    FAMILY("가족"),
    COMPANY("회사"),
    CLUB("동아리"),
    ALUMNI("동호회"),
    STUDENT_ASSOCIATION("학생회"),
    OTHER("기타");

    private final String description;

    TeamType(String description) {
        this.description = description;
    }

}
