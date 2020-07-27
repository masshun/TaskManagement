package com.example.demo.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class TaskFormTest {
	@Autowired
	private Validator validator;
	private TaskForm taskForm = new TaskForm();

	private BindingResult bindingResult = new BeanPropertyBindingResult(taskForm, "TaskForm");

	@BeforeEach
	void setUp() {
		taskForm.setContent("hoge");
		taskForm.setDeadline("2020-09-09 12:00:00");
		taskForm.setLabel("red");
		taskForm.setStatus("未完");
		taskForm.setAddresseeName("fuga");
	}

	@ParameterizedTest
	@ValueSource(strings = { "氏名", "49文字のあああああ49文字のあああああ49文字のあああああ49文字のあああああ49文字のああああ" })
	void titleのバリデーション通過(String input) throws Exception {
		taskForm.setTitle(input);
		validator.validate(taskForm, bindingResult);
		assertNull(bindingResult.getFieldError());
	}

	@ParameterizedTest
	@NullAndEmptySource
	void titleの空文字(String input) throws Exception {
		taskForm.setTitle(input);
		validator.validate(taskForm, bindingResult);
		assertEquals("必須項目です", bindingResult.getFieldError().getDefaultMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "50文字以上のあああああああああああああああおあああああああああああああああああああああああああああああああ" })
	void titleの文字超過(String input) throws Exception {
		taskForm.setTitle(input);
		taskForm.setUserAddresseeId(1);
		validator.validate(taskForm, bindingResult);
		assertEquals("1文字以上50文字以内で入力してください", bindingResult.getFieldError().getDefaultMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "30文字以内のああああ" })
	void addresseeNameのバリデーション通過(String input) throws Exception {
		taskForm.setTitle("hoge");
		taskForm.setAddresseeName(input);
		validator.validate(taskForm, bindingResult);
		assertNull(bindingResult.getFieldError());
	}

	@ParameterizedTest
	@ValueSource(strings = { "30文字以上のああああああああああああああああああああああああ" })
	void addresseeNameの文字超過(String input) throws Exception {
		taskForm.setTitle("hoge");
		taskForm.setAddresseeName(input);
		validator.validate(taskForm, bindingResult);
		assertEquals("1文字以上30文字以内で入力してください", bindingResult.getFieldError().getDefaultMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	void addresseeNameに不正値を入力(String input) throws Exception {
		taskForm.setTitle("hoge");
		taskForm.setAddresseeName(input);
		validator.validate(taskForm, bindingResult);
		assertEquals("必須項目です", bindingResult.getFieldError().getDefaultMessage());
	}

}
