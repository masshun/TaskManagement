package com.example.demo.domain;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AccountFormTest {

	@Autowired
	private Validator validator;
	private AccountForm accountForm = new AccountForm();

	private BindingResult bindingResult = new BeanPropertyBindingResult(accountForm, "TaskForm");

	@ParameterizedTest
	@ValueSource(strings = { "あ", "30文字のあああああああああああああああああああああああああ" })
	void usernameのバリデーション通過(String input) {
		accountForm.setUsername(input);
		accountForm.setEmail("email@email.com");
		accountForm.setPassword("password");
		validator.validate(accountForm, bindingResult);
		assertEquals(bindingResult.getFieldError(), null);
	}

	@ParameterizedTest
	@NullAndEmptySource
	void usernameのnull(String input) {
		accountForm.setUsername(input);
		accountForm.setEmail("email@email.com");
		accountForm.setPassword("password");
		validator.validate(accountForm, bindingResult);
		assertEquals("必須項目です", bindingResult.getFieldError().getDefaultMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "31文字以上のあああああああああああああああああああああああああああああああ" })
	void usernameの未入力及び31文字以上入力(String input) {
		accountForm.setUsername(input);
		accountForm.setEmail("email@email.com");
		accountForm.setPassword("password");
		validator.validate(accountForm, bindingResult);
		assertEquals("1文字以上30文字以内で入力してください", bindingResult.getFieldError().getDefaultMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "password123", "12345678" })
	void passwordのバリデーション通過(String input) {
		accountForm.setUsername("hoge");
		accountForm.setEmail("email@email.com");
		accountForm.setPassword(input);
		validator.validate(accountForm, bindingResult);
		assertEquals(bindingResult.getFieldError(), null);
	}

	@ParameterizedTest
	@EmptySource
	void passwordの空文字(String input) {
		accountForm.setUsername("hoge");
		accountForm.setEmail("email@email.com");
		accountForm.setPassword(input);
		validator.validate(accountForm, bindingResult);
		assertEquals("8文字以上255文字以内で入力してください", bindingResult.getFieldError().getDefaultMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "あいうエオかきああ", "abc3こああああこ", ".password" })
	void passwordの不正値(String input) {
		accountForm.setUsername("hoge");
		accountForm.setEmail("email@email.com");
		accountForm.setPassword(input);
		validator.validate(accountForm, bindingResult);
		assertEquals("半角英数字を入力してください", bindingResult.getFieldError().getDefaultMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "a", "abc2", "pass123" })
	void passwordを8文字未満で入力(String input) {
		accountForm.setUsername("hoge");
		accountForm.setEmail("email@email.com");
		accountForm.setPassword(input);
		validator.validate(accountForm, bindingResult);
		assertEquals("8文字以上255文字以内で入力してください", bindingResult.getFieldError().getDefaultMessage());
	}

	@Test
	void メールアドレスがバリデーションチェックを通過() {
		accountForm.setUsername("hoge");
		accountForm.setPassword("password");
		accountForm.setEmail("email@email.com");
		validator.validate(accountForm, bindingResult);
		assertEquals(bindingResult.getFieldError(), null);
	}

	@ParameterizedTest
	@ValueSource(strings = { "アイウエオ@email.com", "abcexample.com", "p ss123@email.com" })
	void メールアドレスが不正値(String input) {
		accountForm.setUsername("hoge");
		accountForm.setPassword("password");
		accountForm.setEmail(input);
		validator.validate(accountForm, bindingResult);
		assertEquals("正しいメールアドレスを入力してください", bindingResult.getFieldError().getDefaultMessage());
	}

	@ParameterizedTest
	@EmptySource
	void メールアドレスの空文字(String input) {
		accountForm.setUsername("hoge");
		accountForm.setPassword("password");
		accountForm.setEmail(input);
		validator.validate(accountForm, bindingResult);
		assertEquals("正しいメールアドレスを入力してください", bindingResult.getFieldError().getDefaultMessage());
	}
}
