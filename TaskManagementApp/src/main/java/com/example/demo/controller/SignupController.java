package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.AccountForm;
import com.example.demo.domain.object.ConfirmationToken;
import com.example.demo.service.userService.RegisterUserService;

@Controller
@Transactional
@RequestMapping("/signup")
public class SignupController {

	@Autowired
	RegisterUserService registerUserService;

	@Autowired
	HttpSession httpSession;

	@GetMapping
	public String getSignUp(AccountForm accountForm, Model model) {
		model.addAttribute("accountForm", accountForm);
		return "auth/signup";
	}

	@PostMapping
	public String create(@ModelAttribute @Validated AccountForm form, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, String username, String password) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "入力値に誤りがあります");
			return "auth/signup";
		}

		ConfirmationToken token = registerUserService.setConfirmationToken(form, password);
		registerUserService.registerMail(form, token, username);

		return "auth/sendSignupMailNotice";
	}

	@CrossOrigin
	@GetMapping("/validate")
	public String validate(RedirectAttributes redirectAttributes, @RequestParam("id") String id,
			HttpServletRequest request, String password) throws Exception {

		// ダウンキャストチェック
		Object obj = httpSession.getAttribute("hoge");
		ConfirmationToken token = null;
		if (obj instanceof ConfirmationToken) {
			token = (ConfirmationToken) obj;
		} else {
			// TODO java.lang.ClassCastException
		}

		if (token != null) {
			try {
				AccountForm form = registerUserService.createForm(token);
				registerUserService.registerUser(form);
				request.login(form.getUsername(), token.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
				// TODO ServletExceptionも別に作る
			}
		}
		redirectAttributes.addFlashAttribute("result", "登録しました");
		return "redirect:/";
	}
}
