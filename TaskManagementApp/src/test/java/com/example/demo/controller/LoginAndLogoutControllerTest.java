package com.example.demo.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class LoginAndLogoutControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void ログイン成功後ホーム画面に遷移する() throws Exception {
		this.mockMvc.perform(formLogin("/login").user("hoge").password("password")).andExpect(status().isFound())
				.andExpect(redirectedUrl("/"));
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
	@WithMockUser(username = "hoge", password = "password")
	void ログアウト処理でログイン画面に遷移する() throws Exception {
		this.mockMvc.perform(post("/logout").with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));
		this.mockMvc.perform(get("/")).andExpect(redirectedUrl("http://localhost/login"));
	}

}
