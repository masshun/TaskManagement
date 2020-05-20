package com.example.demo.service.userService;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.AccountForm;
import com.example.demo.domain.ConfirmationToken;
import com.example.demo.repository.AccountMapper;

@Service
@Transactional
public class RegisterUserService {

	@Autowired
	AccountMapper accountMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	HttpSession httpSession;

	public ConfirmationToken setConfirmationToken(AccountForm form, String password) throws Exception {
		String confirmationToken = UUID.randomUUID().toString();

		ConfirmationToken token = new ConfirmationToken(passwordEncoder.encode(password), confirmationToken, form);
		// TODO sessionに入れるtokenがあるかどうか & session例外
		httpSession.setAttribute("hoge", token);
		return token;
	}

	@Async
	public String registerMail(AccountForm form, ConfirmationToken confirmationToken, String username) {
		// TODO メールのフィールドは別に作る
		String IPadnPort = "localhost:9996";
		String from = "xxx@@gmail.com";
		String title = "新規登録 アカウント確認のお願い";
		String content = username + "さん" + "\n" + "\n" + "以下のリンクにアクセスしてアカウントを認証してください" + "\n" + "http://" + IPadnPort
				+ "/signup/validate" + "?id=" + confirmationToken;

		// TODO 別に作る
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom(from);
			msg.setTo(form.getEmail());
			msg.setSubject(title);
			msg.setText(content);
			javaMailSender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO failed to send

		}
		return "送信しました";
	}

	public AccountForm createForm(ConfirmationToken token) {
		AccountForm form = new AccountForm();
		form.setEmail(token.getAccountForm().getEmail());
		form.setUsername(token.getAccountForm().getUsername());
		form.setPassword(token.getPassword());

		return form;
	}

	public void registerUser(AccountForm accountForm) {
		accountForm.setPassword(passwordEncoder.encode(accountForm.getPassword()));
		accountMapper.save(accountForm);
	}
}