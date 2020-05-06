package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.AccountForm;
import com.example.demo.repository.AccountMapper;

@Service
@Transactional
public class RegisterUserService {

	@Autowired
	AccountMapper accountMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	// 登録時パスワードをハッシュ化してsave
	public void registerUser(AccountForm accountForm) {
		accountForm.setPassword(passwordEncoder.encode(accountForm.getPassword()));
		accountMapper.save(accountForm);
	}
}