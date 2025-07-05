package com.jangburich.domain.owner.domain.controller.dto.req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class OwnerCreateReqDTO {
	private String businessName;
	private String businessRegistrationNumber;
	private String phoneNumber;
	private String name;
	private LocalDate openingDate;
}
