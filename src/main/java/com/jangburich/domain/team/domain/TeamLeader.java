package com.jangburich.domain.team.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class TeamLeader {
    @Column(name = "user_id")
    private Long leaderId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_name")
    private String bankName;

    public TeamLeader(Long leaderId, String accountNumber, String bankName) {
        this.leaderId = leaderId;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }
}
