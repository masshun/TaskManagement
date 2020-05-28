package com.example.demo.service.userService;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Account;
import com.example.demo.domain.AccountForm;
import com.example.demo.domain.object.ConfirmationToken;
import com.example.demo.exception.MultipleException;
import com.example.demo.repository.AccountMapper;
import com.example.demo.service.mailService.SendMailService;

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

	@Autowired
	SendMailService mailService;

	public ConfirmationToken setConfirmationToken(AccountForm form, String password) {
		String confirmationToken = UUID.randomUUID().toString();
		List<Account> account = accountMapper.findAll();

		account.forEach(m -> {
			if (form.getEmail().equals(m.getEmail())) {
				throw new MultipleException("すでに登録されているメールアドレスです");
			}
		});

		ConfirmationToken token = new ConfirmationToken(passwordEncoder.encode(password), confirmationToken, form);
		httpSession.setAttribute("hoge", token);
		return token;
	}

	@Async
	public void registerMail(AccountForm form, ConfirmationToken confirmationToken, String username) {
		// TODO メールのフィールドは別に作る
		String port = "localhost:9996";
		String from = "@@@@@email.com";
		String title = "新規登録 アカウント確認のお願い";
		String content = username + "さん" + "\n" + "\n" + "以下のリンクにアクセスしてアカウントを認証してください" + "\n" + "http://" + port
				+ "/signup/validate" + "?id=" + confirmationToken;

//		mailService.sendMail(map);
		// TODO 別に作る

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(form.getEmail());
		msg.setSubject(title);
		msg.setText(content);
		javaMailSender.send(msg);

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
		accountMapper.saveAddressee(accountForm);
	}
}