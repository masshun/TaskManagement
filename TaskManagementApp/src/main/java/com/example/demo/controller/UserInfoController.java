package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.repository.UserDetailsImpl;
import com.example.demo.service.userService.GetUserInfoService;
import com.example.demo.service.userService.UserDetailsServiceImpl;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController {

	@Autowired
	GetUserInfoService user;

	@Autowired
	UserDetailsServiceImpl userImpl;

	@GetMapping
	public String getUserInfo(Model model, Principal p) {

		String username = p.getName();
		UserDetailsImpl account = userImpl.loadUserByUsername(username);
		String email = account.getEmail();
		int userId = account.getUserId();

		model.addAttribute("email", email);
		model.addAttribute("userId", userId);
		return "auth/userInfo";
	}
}
