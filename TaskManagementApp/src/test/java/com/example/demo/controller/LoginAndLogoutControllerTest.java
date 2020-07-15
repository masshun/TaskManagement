package com.example.demo.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.repository.AccountMapper;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@Sql(statements = {
		"INSERT INTO user(username, password, email) VALUES ('ユーザー1', '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa', 'hoge@email.com')",
		"INSERT INTO user(username, password, email)VALUES('ユーザー2', '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa', 'hoge2@email.com')",
		"INSERT INTO user_addressee(name) VALUES('ユーザー1')", "INSERT INTO user_addressee(name)VALUES('ユーザー2')" })
public class LoginAndLogoutControllerTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@InjectMocks
	private LoginController controller;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity(springSecurityFilterChain)).build();
	}

	@Autowired
	AccountMapper accountMapper;

	private MockMvc mockMvc;

	@Test
	void ログイン成功後ホーム画面に遷移する() throws Exception {
		this.mockMvc.perform(formLogin().user("ユーザー1").password("password")).andExpect(authenticated());
	}

	@Test
	void DBに存在しないユーザー情報でログインするとログインページへ戻される() throws Exception {
		this.mockMvc.perform(formLogin("/login").user("unknown").password("unknown"))
				.andExpect(redirectedUrl("/login"));
	}

	@Test
	void ログイン無しにindexにgetアクセスするとログイン画面にリダイレクトする() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithMockUser(username = "ユーザー1", password = "password")
	void ログアウト処理でログイン画面に遷移する() throws Exception {
		this.mockMvc.perform(post("/logout").with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));
	}

}
