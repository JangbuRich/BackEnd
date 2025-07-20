package com.jangburich.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class TeamLeader {

    private Long leaderId;

    private String accountNumber;

    private String bankName;

    @Builder
    public TeamLeader(Long leaderId, String accountNumber, String bankName) {
        this.leaderId = leaderId;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }
}
