// package com.jangburich.domain.oauth.domain.service;
//
// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
// import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.stereotype.Service;
// import org.springframework.web.context.request.RequestContextHolder;
// import org.springframework.web.context.request.ServletRequestAttributes;
//
// import com.jangburich.domain.oauth.domain.CustomOAuthUser;
// import com.jangburich.domain.oauth.domain.KakaoResponse;
// import com.jangburich.domain.oauth.domain.OAuth2Response;
// import com.jangburich.domain.oauth.domain.OAuthUserDTO;
// import com.jangburich.domain.owner.domain.entity.Owner;
// import com.jangburich.domain.owner.domain.repository.OwnerRepository;
// import com.jangburich.domain.user.domain.User;
// import com.jangburich.domain.user.repository.UserRepository;
//
// import jakarta.servlet.http.HttpServletRequest;
// import lombok.extern.slf4j.Slf4j;
//
// @Service
// @Slf4j
// public class OAuthUserService extends DefaultOAuth2UserService {
// 	private final UserRepository userRepository;
// 	private final OwnerRepository ownerRepository;
//
// 	public OAuthUserService(UserRepository userRepository, OwnerRepository ownerRepository) {
// 		this.userRepository = userRepository;
// 		this.ownerRepository = ownerRepository;
// 	}
//
// 	@Override
// 	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
// 		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
// 		log.info("OAuth2User loaded: {}", oAuth2User.getName());
//
// 		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
// 		String state = (String)request.getSession().getAttribute("oauth2_state");
// 		request.getSession().removeAttribute("oauth2_state");
//
// 		OAuth2Response oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
//
// 		//		String userId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
// 		String userId = oAuth2Response.getProviderId();
//
// 		if ("user".equals(state)) {
// 			User existUser = userRepository.findByProviderId(userId).orElse(null);
// 			if (existUser == null) {
// 				User newUser = User.create(userId, oAuth2Response.getNickname(), oAuth2Response.getImage(),
// 					"ROLE_USER");
// 				userRepository.save(newUser);
//
// 				OAuthUserDTO userDTO = new OAuthUserDTO();
// 				userDTO.setUserId(userId);
// 				userDTO.setNickname(oAuth2Response.getNickname());
// 				userDTO.setImage(oAuth2Response.getImage());
// 				userDTO.setRole("ROLE_USER");
//
// 				return new CustomOAuthUser(userDTO);
// 			} else {
// 				existUser.setNickname(oAuth2Response.getNickname());
// 				existUser.setProfileImageUrl(oAuth2Response.getImage());
//
// 				userRepository.save(existUser);
//
// 				OAuthUserDTO userDTO = new OAuthUserDTO();
// 				userDTO.setUserId(existUser.getProviderId());
// 				userDTO.setNickname(existUser.getNickname());
// 				userDTO.setImage(existUser.getProfileImageUrl());
// 				userDTO.setCreatedAt(existUser.getCreatedAt());
// 				userDTO.setRole("ROLE_USER");
//
// 				return new CustomOAuthUser(userDTO);
// 			}
// 		} else if ("owner".equals(state)) {
// 			User existUser = userRepository.findByProviderId(userId).orElse(null);
// 			if (existUser == null || !existUser.getRole().equals("ROLE_OWNER")) {
// 				User newUser = User.create(userId, oAuth2Response.getNickname(), oAuth2Response.getImage(),
// 					"ROLE_OWNER");
// 				userRepository.save(newUser);
//
// 				OAuthUserDTO userDTO = new OAuthUserDTO();
// 				userDTO.setUserId(userId);
// 				userDTO.setNickname(oAuth2Response.getNickname());
// 				userDTO.setImage(oAuth2Response.getImage());
// 				userDTO.setRole("ROLE_OWNER");
//
// 				Owner newOwner = Owner.create(newUser);
// 				newOwner.setUser(newUser);
// 				ownerRepository.save(newOwner);
//
// 				return new CustomOAuthUser(userDTO);
// 			} else {
// 				existUser.setNickname(oAuth2Response.getNickname());
// 				existUser.setProfileImageUrl(oAuth2Response.getImage());
//
// 				userRepository.save(existUser);
//
// 				OAuthUserDTO userDTO = new OAuthUserDTO();
// 				userDTO.setUserId(existUser.getProviderId());
// 				userDTO.setNickname(existUser.getNickname());
// 				userDTO.setImage(existUser.getProfileImageUrl());
// 				userDTO.setRole("ROLE_OWNER");
//
// 				return new CustomOAuthUser(userDTO);
// 			}
// 		} else {
// 			return null;
// 		}
// 	}
// }
