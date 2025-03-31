package com.jangburich.domain.owner.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class OwnerGetResDTO {
    private Long id;
    private String phoneNumber;
    private String businessName;
    private String businessRegistrationNumber;
    private String name;
    private LocalDate openingDate;

    public OwnerGetResDTO(Long id, String phoneNumber, String businessName, String businessRegistrationNumber,
                          String name,
                          LocalDate openingDate) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.businessName = businessName;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.name = name;
        this.openingDate = openingDate;
    }

    public static OwnerGetResDTO of(Owner owner) {
        return new OwnerGetResDTO(
            owner.getId(),
            owner.getPhoneNumber(),
            owner.getBusinessName(),
            owner.getBusinessRegistrationNumber(),
            owner.getName(),
            owner.getOpeningDate()
        );
    }
}
