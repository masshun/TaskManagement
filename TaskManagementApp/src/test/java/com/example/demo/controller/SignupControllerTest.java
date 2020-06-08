package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.AccountForm;
import com.example.demo.service.userService.RegisterUserService;

@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class SignupControllerTest {

	private MockMvc mockMvc;

	@MockBean
	RegisterUserService registerUserService;

	@Autowired
	HttpSession httpSession;

	@MockBean
	AccountForm accountForm;

	@InjectMocks // モックオブジェクトの注入
	private SignupController SignupController;

	@BeforeEach // テストメソッド（@Testをつけたメソッド）実行前に都度実施
	void initmocks() {
		MockitoAnnotations.initMocks(this); // アノテーションの有効化
		mockMvc = MockMvcBuilders.standaloneSetup(SignupController).build(); // MockMvcのセットアップ
	}

	@Test
	void signupにgetリクエスト() throws Exception {
		AccountForm ac = new AccountForm();
		mockMvc.perform(get("/signup")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("accountForm", ac))
				.andExpect(view().name("auth/signup"));
	}

	@Test
	void signupにpostリクエスト() {

	}
}
