package com.example.demo.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.mapper.AccountMapper;

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

	public String getLoginUsername(Principal p) {
		String name = p.getName();
		return name;
	}

}
