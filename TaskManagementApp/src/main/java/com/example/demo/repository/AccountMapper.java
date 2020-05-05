package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Account;

@Mapper
public interface AccountMapper {
	List<Account> findAll();

	Account findByUsername(String username);

	boolean save(Account account);

	boolean update(Account account);

	boolean delete(int id);

}
