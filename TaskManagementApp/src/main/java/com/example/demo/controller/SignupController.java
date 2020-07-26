package com.example.demo.controller;

import javax.servlet.ServletException;
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
	public String postSignUp(@ModelAttribute @Validated AccountForm form, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (bindingResult.hasErrors() || !(form.getPassword().equals(form.getConfPassword()))) {
			model.addAttribute("error", "入力値に誤りがあります");
			return "auth/signup";
		}

		ConfirmationToken confToken = registerUserService.setConfirmationToken(form);
		registerUserService.authenticateByMail(confToken);

		return "auth/noticeHasBeSendMail";
	}

	@CrossOrigin
	@GetMapping("/validate")
	public String validate(RedirectAttributes redirectAttributes, @RequestParam("id") String id,
			HttpServletRequest request) throws ServletException {
		// UUIDに該当する一時テーブルのユーザー情報を登録
		Object sessionUserData = httpSession.getAttribute(id);
		if (sessionUserData == null) {
			redirectAttributes.addFlashAttribute("result", "登録に失敗しました");
			return "redirect:/login";
		}

		// ControllerAdviceでダウンキャスト例外
		ConfirmationToken sessionUserDataToConfToken = (ConfirmationToken) sessionUserData;

		AccountForm accountForm = registerUserService.createForm(sessionUserDataToConfToken);
		registerUserService.registerUser(accountForm);

		// 自動ログイン
		String formUsername = accountForm.getUsername();
		String formRawPassword = sessionUserDataToConfToken.getAccountForm().getPassword();
		request.login(formUsername, formRawPassword);

		redirectAttributes.addFlashAttribute("result", "登録しました");
		return "redirect:/";
	}
}
