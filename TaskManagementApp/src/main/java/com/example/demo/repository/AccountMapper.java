package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Account;
import com.example.demo.domain.AccountForm;

@Mapper
public interface AccountMapper {
	List<Account> findAll();

	Account findByUsername(String username);

	Account findByUserId(int id);

	boolean save(AccountForm accountForm);

	boolean saveAddressee(AccountForm accountForm);

	boolean update(Account account);

	boolean delete(int id);

}
