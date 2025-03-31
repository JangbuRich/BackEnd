package com.jangburich.domain.menu.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jangburich.domain.menu.domain.MenuCreateRequestDTO;
import com.jangburich.domain.menu.domain.MenuResponse;
import com.jangburich.domain.menu.domain.MenuUpdateRequestDTO;
import com.jangburich.domain.menu.service.MenuService;
import com.jangburich.global.payload.Message;
import com.jangburich.global.payload.ResponseCustom;
import com.jangburich.utils.parser.AuthenticationParser;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Menu", description = "Menu API")
@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	@PostMapping("/register")
	public ResponseCustom<Message> registerMenu(
		Authentication authentication,
		@RequestBody MenuCreateRequestDTO menuCreateRequestDTO) {
		menuService.registerMenu(AuthenticationParser.parseUserId(authentication), menuCreateRequestDTO);
		return ResponseCustom.OK(Message.builder().message("success").build());
	}

	@PatchMapping("/update/{id}")
	public ResponseCustom<Message> updateMenu(
		Authentication authentication, @PathVariable Long id,
		@RequestBody MenuUpdateRequestDTO menuUpdateRequestDTO) {
		menuService.updateMenu(AuthenticationParser.parseUserId(authentication), id, menuUpdateRequestDTO);
		return ResponseCustom.OK(Message.builder().message("success").build());
	}

	@DeleteMapping("/{id}")
	public ResponseCustom<Message> deleteMenu(
		Authentication authentication, @PathVariable Long id) {
		menuService.deleteMenu(AuthenticationParser.parseUserId(authentication), id);
		return ResponseCustom.OK(Message.builder().message("success").build());
	}

	@GetMapping("")
	public ResponseCustom<List<MenuResponse>> getMenu(Authentication authentication) {
		List<MenuResponse> menu = menuService.getMenu(AuthenticationParser.parseUserId(authentication));
		return ResponseCustom.OK(menu);
	}
}
