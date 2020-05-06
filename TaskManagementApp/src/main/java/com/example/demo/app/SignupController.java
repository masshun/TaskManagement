package com.example.demo.app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.AccountForm;
import com.example.demo.service.RegisterUserService;

@Controller
public class SignupController {

	@Autowired
	RegisterUserService registerUserService;

	@GetMapping("/signup")
	public String getSignUp(AccountForm accountForm, Model model) {
		model.addAttribute("accountForm", accountForm);
		return "login/signup";
	}

	@PostMapping("/signup")
	String create(@ModelAttribute @Validated AccountForm form, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, String username, String password) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "入力値に誤りがあります");
		}
		registerUserService.registerUser(form);
		redirectAttributes.addFlashAttribute("success", "登録が完了しました");
		try {
			request.login(username, password);
		} catch (ServletException e) {
			// エラーページに飛ばす
		}
		return "redirect:/";
	}
}
