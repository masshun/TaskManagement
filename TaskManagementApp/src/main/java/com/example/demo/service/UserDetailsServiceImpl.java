package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	AccountMapper accountMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountMapper.findByUsername(username);
		// accountがnullになっている
		if (account == null) {
			throw new UsernameNotFoundException("User 404");
		}
		if (!account.getUsername().equals(username)) {
			throw new UsernameNotFoundException("ユーザー名およびパスワードと一致しません");
		}
		// DBからユーザー情報を取得
		return new UserDetailsImpl(account);
	}

}
