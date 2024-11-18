package com.jangburich.utils;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
	private final OAuth2AuthorizationRequestResolver defaultResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
		return customizeAuthorizationRequest(req, request);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
		return customizeAuthorizationRequest(req, request);
	}

	private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest req, HttpServletRequest request) {
		if (req == null) {
			return null;
		}

		String state = request.getParameter("state");
		if (state != null) {
			request.getSession().setAttribute("oauth2_state", state);
		}

		return OAuth2AuthorizationRequest.from(req)
			.state(state)
			.build();
	}

}
