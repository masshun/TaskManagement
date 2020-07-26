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

import com.example.demo.config.MailPropConfig;
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
	MailPropConfig prop;

	@Autowired
	AccountMapper accountMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	HttpSession httpSession;

	@Autowired
	SendMailService mailService;

	public ConfirmationToken setConfirmationToken(AccountForm accountForm) {
		String randomId = UUID.randomUUID().toString();
		List<Account> accountList = accountMapper.findAll();

		accountList.forEach(m -> {
			if (accountForm.getEmail().equals(m.getEmail())) {
				throw new MultipleException("すでに登録されているメールアドレスです");
			}
			if (accountForm.getUsername().equals(m.getUsername())) {
				throw new MultipleException("すでに登録されているユーザー名です");
			}
		});
		
		//DB登録用のエンコードパスワードを別個用意し、自動ログイン用のエンコードされていないパスワードをaccountFormに格納している
		String encodedPassword = passwordEncoder.encode(accountForm.getPassword());
		ConfirmationToken confToken = new ConfirmationToken(encodedPassword, randomId, accountForm);
		httpSession.setAttribute(randomId, confToken);
		return confToken;
	}

	public void authenticateByMail(ConfirmationToken confToken) {
		String title = "新規登録 アカウント確認のお願い";
		String content = 
				confToken.getAccountForm().getUsername() + "さん" + 
				"\n" + 
				"\n" + 
				"以下のリンクにアクセスしてアカウントを認証してください。" + 
				"\n" + 
				"http://" + prop.get("port") + "signup/validate" + "?id=" + confToken.getId();
		
		Map<String, String> mailMap = new HashMap<>();
		mailMap.put("from", prop.get("mailaddress"));
		mailMap.put("title", title);
		mailMap.put("email", confToken.getAccountForm().getEmail());
		mailMap.put("content", content);
		mailService.sendMail(mailMap);
	}

	public AccountForm createForm(ConfirmationToken token) {
		AccountForm accountform = new AccountForm();
		accountform.setEmail(token.getAccountForm().getEmail());
		accountform.setUsername(token.getAccountForm().getUsername());
		accountform.setPassword(token.getEncodedPassword());
		return accountform;
	}

	public void registerUser(AccountForm accountForm) {
		// TODO 1回の呼び出しに変更する
		accountMapper.save(accountForm);
		accountMapper.saveAddressee(accountForm);
	}
}