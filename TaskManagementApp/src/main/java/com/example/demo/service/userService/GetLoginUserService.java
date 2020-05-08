package com.example.demo.service.userService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.repository.AccountMapper;

@Service
public class GetLoginUserService {

	@Autowired
	AccountMapper accountMapper;

	public int getLoginUserId(Principal p) {
		String username = p.getName();
		Account ac = accountMapper.findByUsername(username);
		int userId = ac.getId();
		return userId;
	}

}
