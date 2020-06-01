package com.example.demo.service.userService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Account;
import com.example.demo.domain.AccountForm;
import com.example.demo.domain.object.ConfirmationToken;
import com.example.demo.domain.object.Mail;
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
	HttpSession httpSession;

	@Autowired
	Mail mail;

	@Autowired
	SendMailService mailService;

	public ConfirmationToken setConfirmationToken(AccountForm form, String password) {
		String id = UUID.randomUUID().toString();
		List<Account> account = accountMapper.findAll();

		account.forEach(m -> {
			if (form.getEmail().equals(m.getEmail())) {
				throw new MultipleException("すでに登録されているメールアドレスです");
			}
		});

		ConfirmationToken token = new ConfirmationToken(passwordEncoder.encode(password), id, form);
		httpSession.setAttribute(id, token);
		return token;
	}

	public void registerMail(AccountForm form, ConfirmationToken confirmationToken, String username) {

		String title = "新規登録 アカウント確認のお願い";
		String content = username + "さん" + "\n" + "\n" + "以下のリンクにアクセスしてアカウントを認証してください" + "\n" + "http://"
				+ mail.getPORT() + "/signup/validate" + "?id=" + confirmationToken;

		Map<String, String> map = new HashMap<>();
		map.put("from", mail.getFROM());
		map.put("title", title);
		map.put("email", form.getEmail());
		map.put("content", content);
		mailService.sendMail(map);

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