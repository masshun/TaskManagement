package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.domain.Account;
import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;
import com.example.demo.domain.object.PageWrapper;
import com.example.demo.repository.AccountMapper;
import com.example.demo.service.taskService.TaskService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "hoge", password = "password")
public class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	TaskService taskService;

	@Autowired
	AccountMapper accountMapper;

	@Autowired
	private TaskController controller = new TaskController();

	@BeforeClass
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).alwaysDo(log()).build();
	}

	@Test
	void indexにgetメソッドでアクセスする() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

	@Test
	void requestedTaskにgetアクセスする() throws Exception {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account ac = accountMapper.findByUsername(auth.getName());
			int userId = ac.getId();
			String param = null;
			taskService.setRequestedTask(userId, param);
			Page<Task> notExecutedTask = taskService.getNotExecutedTask(PageRequest.of(1 - 1, 2));

			MvcResult result = mockMvc.perform(get("/requestedTask")).andExpect(status().isOk())
					.andExpect(model().attribute("notExecutedTask", notExecutedTask))
					.andExpect(content().contentType("text/html;charset=UTF-8")).andReturn();

			@SuppressWarnings("unchecked")
			PageWrapper<Task> notExecTaskPage = (PageWrapper<Task>) result.getModelAndView().getModel()
					.get("notExecTaskPage");
			assertEquals(notExecTaskPage.getSize(), 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void receivedTaskにgetアクセスする() throws Exception {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account ac = accountMapper.findByUsername(auth.getName());
			int userId = ac.getId();
			String param = null;
			taskService.setReceivedTask(userId, param);
			Page<Task> notExecutedTask = taskService.getNotExecutedTask(PageRequest.of(1 - 1, 2));

			MvcResult result = mockMvc.perform(get("/receivedTask")).andExpect(status().isOk())
					.andExpect(model().attribute("notExecutedTask", notExecutedTask))
					.andExpect(content().contentType("text/html;charset=UTF-8")).andReturn();

			@SuppressWarnings("unchecked")
			PageWrapper<Task> notExecTaskPage = (PageWrapper<Task>) result.getModelAndView().getModel()
					.get("notExecTaskPage");
			assertEquals(notExecTaskPage.getSize(), 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void 頼みごとの新規登録() throws Exception {
		TaskForm form = new TaskForm();
		form.setTitle("買い物");
		form.setContent("カレーの食材を買う");
		form.setLabel("red");
		form.setStatus("未完");
		form.setDeadline("2020-08-08 12:00:00");

		mockMvc.perform(post("/create").param("title", "買い物").param("content", "カレーの食材を買う").param("label", "red")
				.param("deadline", "2020-08-08 12:00:00").param("status", "未完")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));

	}

}
