package com.example.demo.service.userService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.repository.AccountMapper;

@Service
public class GetUserInfoService {

	@Autowired
	AccountMapper accountMapper;

	// TODO userdetailsserviceを経由する
	public String getAddresseeById(int id) {
		Account ac = accountMapper.findByUserId(id);
		String adressee = ac.getUsername();
		return adressee;
	}

	public String getAddreseeMailById(int id) {
		Account ac = accountMapper.findByUserId(id);
		String email = ac.getEmail();
		return email;
	}

	public int getLoginUserId(Principal p) {
		String username = p.getName();
		Account ac = accountMapper.findByUsername(username);
		int userId = ac.getId();
		return userId;
	}

	public String getSenderName(int id) {
		Account ac = accountMapper.findByUserId(id);
		String sender = ac.getUsername();
		return sender;
	}
}
