package com.example.demo.service.userService;

import java.security.Principal;
import java.util.Optional;
import java.util.OptionalInt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.repository.AccountMapper;

@Service
public class GetUserInfoService {

	@Autowired
	AccountMapper accountMapper;

	// TODO userdetailsserviceを経由する
	public Optional<String> getAddresseeById(int id) {
		Optional<Account> ac = Optional.ofNullable(accountMapper.findByUserId(id));
		Optional<String> adressee = ac.map(addressee -> addressee.getUsername());
		return adressee;
	}

	public OptionalInt getAddresseeId(String addresseeName) {
		Optional<Account> ac = Optional.ofNullable(accountMapper.findByUsername(addresseeName));
		OptionalInt addresseeId = ac.stream().mapToInt(addressee -> addressee.getId()).findFirst();
		return addresseeId;
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
