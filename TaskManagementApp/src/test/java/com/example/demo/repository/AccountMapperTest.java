package com.example.demo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Account;
import com.example.demo.domain.AccountForm;

@MybatisTest
@TestPropertySource(properties = "test.properties")
@Transactional
@Sql(statements = { "INSERT INTO user(username, password, email) VALUES ('ユーザー1', 'password', 'hoge@email.com')",
		"INSERT INTO user(username, password, email)VALUES('ユーザー2', 'password', 'hoge2@email.com')",
		"INSERT INTO user_addressee(name) VALUES('ユーザー1')", "INSERT INTO user_addressee(name)VALUES('ユーザー2')" })
public class AccountMapperTest {

	@Autowired
	AccountMapper accountMapper;

	@Test
	void 全件取得() throws Exception {
		List<Account> list = accountMapper.findAll();
		Optional<Account> account = list.stream().findAny();
		assertEquals("ユーザー1", account.get().getUsername());
	}

	@Test
	void リストに存在しないユーザーネーム() throws Exception {
		List<Account> list = accountMapper.findAll();
		Optional<Account> account = list.stream().findAny();
		Account notActual = getAccount();
		assertNotEquals(notActual, account);
	}

	@Test
	void ユーザーネームで取得() throws Exception {
		String username = "ユーザー1";
		Account account = accountMapper.findByUsername(username);
		assertEquals("ユーザー1", account.getUsername());
	}

	@Test
	void 存在しないユーザーネームで取得() throws Exception {
		String username = "ユーザー3";
		Account account = accountMapper.findByUsername(username);
		assertNull(account);
	}

	@Test
	void IDで取得() throws Exception {
		AccountForm one = getAccountData();
		accountMapper.save(one);
		int id = one.getId();
		Account account = accountMapper.findByUserId(id);
		assertEquals("セットユーザー", account.getUsername());
	}

	@Test
	void 存在しないIDで取得() throws Exception {
		int id = 2;
		Account account = accountMapper.findByUserId(id);
		assertNull(account);
	}

	@Test
	void アカウントの新規登録() throws Exception {
		AccountForm account = getAccountData();
		accountMapper.save(account);
		Account actual = accountMapper.findByUsername("セットユーザー");
		assertEquals(account.getUsername(), actual.getUsername());
	}

	private AccountForm getAccountData() {
		AccountForm account = new AccountForm();
		account.setUsername("セットユーザー");
		account.setPassword("password");
		account.setEmail("setUser@email.com");
		return account;
	}

	private Account getAccount() {
		Account account = new Account();
		account.setUsername("セットユーザー2");
		account.setPassword("password");
		account.setEmail("setUser2@email.com");
		return account;
	}

}
