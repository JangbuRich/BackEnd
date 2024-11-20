package com.jangburich.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jangburich.utils.JwtFilter;
import com.jangburich.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtUtil jwtUtil;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)

			.authorizeHttpRequests(
				request -> request.requestMatchers("/**", "/oauth2/**", "/login/**", "/swagger-ui/**",
						"/v3/api-docs/**", "/swagger-resources/**", "/payments/**")
					.permitAll()
					.anyRequest()
					.authenticated());

		return http.build();
	}
}