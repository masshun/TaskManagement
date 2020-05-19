package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.repository.AccountMapper;
import com.example.demo.repository.UserDetailsImpl;

@Controller
public class LoginController {

	@Autowired
	AccountMapper accountMapper;

	@GetMapping("/login")
	public String getLogin() {
		return "auth/login";
	}

	// TODO ユーザー情報の編集用画面をつくる
	@GetMapping("/account")
	public String getUserList(Model model, Principal principal) {
		Authentication auth = (Authentication) principal;
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		model.addAttribute("user", userDetails);
		return "account";
	}

	@GetMapping("/403")
	public String handleAccessDenied() {
		return "error/403";
	}
}
