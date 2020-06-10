package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import com.example.demo.domain.AccountForm;
import com.example.demo.service.userService.GetUserInfoService;
import com.example.demo.service.userService.RegisterUserService;
import com.example.demo.service.userService.UserDetailsServiceImpl;

@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@Sql(statements = { "INSERT INTO user(username, password, email) VALUES ('ユーザー1', 'password', 'hoge@email.com')",
		"INSERT INTO user(username, password, email)VALUES('ユーザー2', 'password', 'hoge2@email.com')",
		"INSERT INTO user_addressee(name) VALUES('ユーザー1')", "INSERT INTO user_addressee(name)VALUES('ユーザー2')" })
public class SignupControllerTest {

	private MockMvc mockMvc;

	@MockBean
	RegisterUserService registerUserService;

	@Autowired
	HttpSession httpSession;

	@MockBean
	GetUserInfoService user;

	@MockBean
	UserDetailsServiceImpl userImpl;

	@MockBean
	AccountForm accountForm;

	private MockHttpSession session;

	@Autowired
	private MockHttpServletRequest request;

	@InjectMocks // モックオブジェクトの注入
	private SignupController SignupController;

	@BeforeEach // テストメソッド（@Testをつけたメソッド）実行前に都度実施
	void initmocks() {
		MockitoAnnotations.initMocks(this); // アノテーションの有効化
		mockMvc = MockMvcBuilders.standaloneSetup(SignupController).alwaysDo(log()).build(); // MockMvcのセットアップ
		request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletWebRequest(request));
	}

	@Test
	void signupにgetリクエスト() throws Exception {
		AccountForm ac = new AccountForm();
		mockMvc.perform(get("/signup")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("accountForm", ac))
				.andExpect(view().name("auth/signup"));
	}

	@Test
	void signupにpostリクエスト() throws Exception {
		mockMvc.perform(post("/signup").param("username", "user1").param("password", "password").param("email",
				"email@email.com")).andExpect(view().name("auth/sendSignupMailNotice"));
	}

	@Test
	void signupにパラメータ付与せずにpostリクエスト() throws Exception {
		mockMvc.perform(post("/signup")).andExpect(MockMvcResultMatchers.model().attribute("error", "入力値に誤りがあります"))
				.andExpect(view().name("auth/signup"));
	}

}
