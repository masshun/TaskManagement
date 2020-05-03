package com.example.demo.app;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.UserDetailsImpl;

@Controller
//@SessionAttributes(types = AccountForm.class)
public class LoginController {

	@Autowired
	AccountMapper accountMapper;

	@GetMapping("/login")
	public String getLogin() {
		return "login/login";
	}

	@GetMapping("/admin")
	public String getAdmin(Model model) {
		model.addAttribute("contents", "login/admin :: admin_contents");
		return "login/admin";
	}

	@GetMapping("/logout")
	public String getLogout() {
		return "redirect:/login";
	}

	@GetMapping("/userList")
	public String getUserList(Model model, Principal principal) {
		Authentication auth = (Authentication) principal;
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		model.addAttribute("user", userDetails);
		return "login/userList";
	}

	@GetMapping("/403")
	public String handleAccessDenied() {
		return "error/403";
	}
}
