package com.example.demo.app;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.AccountForm;
import com.example.demo.domain.ConfirmationToken;
import com.example.demo.service.userService.RegisterUserService;

@Controller
@Transactional
public class SignupController {

//	private JavaMailSender javaMailSender;
//
//	@Autowired
//	public SignupController(JavaMailSender javaMailSender) {
//		this.javaMailSender = javaMailSender;
//	}

	@Autowired
	RegisterUserService registerUserService;

	@Autowired
	HttpSession httpSession;

	@GetMapping("/signup")
	public String getSignUp(AccountForm accountForm, Model model) {
		model.addAttribute("accountForm", accountForm);
		return "login/signup";
	}

	@ResponseBody
	@PostMapping("/signup")
	public String create(@ModelAttribute @Validated AccountForm form, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, String username, String password) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "入力値に誤りがあります");
			return "/login/login";
		}
		// 重複確認をあとで行う

		try {
			ConfirmationToken confirmationToken = registerUserService.setConfirmationToken(form, password);
			String result = registerUserService.registerMail(form, confirmationToken, username);
			return result;
		} catch (Exception e) {
			// 例外処理
		}
		String status = "OK";
		return status;
	}

	@CrossOrigin
	@GetMapping("/validate")
	public String validate(RedirectAttributes redirectAttributes, @RequestParam("id") String id,
			HttpServletRequest request, String password) throws Exception {
		String isRegisterd = "false";
		Object re = httpSession.getAttribute("hoge");
		ConfirmationToken token = (ConfirmationToken) re;

		if (token != null) {
			try {
				AccountForm form = registerUserService.createForm(token);
				registerUserService.registerUser(form);

				request.login(form.getUsername(), token.getPassword());
				isRegisterd = "true";
			} catch (Exception e) {
				e.printStackTrace();
				// ServletExceptionも別に作る
				isRegisterd = "false";
			}
		}
		redirectAttributes.addFlashAttribute("result", isRegisterd);
		return "redirect:/";
	}
}
