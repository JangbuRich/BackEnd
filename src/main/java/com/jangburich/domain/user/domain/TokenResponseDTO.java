package com.jangburich.domain.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponseDTO {
	private String accessToken;
	private long accessTokenExpires;
	private String refreshToken;
	private long refreshTokenExpires;
	private Boolean alreadyExists;

	@Builder
	public TokenResponseDTO(String accessToken, long accessTokenExpires, String refreshToken, long refreshTokenExpires, Boolean alreadyExists) {
		this.accessToken = accessToken;
		this.accessTokenExpires = accessTokenExpires;
		this.refreshToken = refreshToken;
		this.refreshTokenExpires = refreshTokenExpires;
		this.alreadyExists = alreadyExists;
	}
}
