package com.example.demo.service.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.repository.AccountMapper;

@Service
public class GetAddresseeService {

	@Autowired
	AccountMapper accountMapper;

	// userdetailsserviceを経由する
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
}
