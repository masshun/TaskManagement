package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.domain.TaskForm;
import com.example.demo.domain.object.PageWrapper;
import com.example.demo.repository.AccountMapper;
import com.example.demo.service.taskService.TaskNoticeService;
import com.example.demo.service.taskService.TaskService;
import com.example.demo.service.userService.GetUserInfoService;
import com.example.demo.service.userService.UserDetailsServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "hoge", password = "password")
public class TaskControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	TaskService taskService;

	@MockBean
	GetUserInfoService user;

	@MockBean
	TaskNoticeService task;

	@Autowired
	AccountMapper accountMapper;

	@Autowired
	private TaskController controller;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).alwaysDo(log()).build();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void indexにgetリクエストする() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

	@Test
	void requestedTaskにgetリクエストする() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			int userId = user.getLoginUserId(auth);

			String param = null;
			taskService.setRequestedTask(userId, param);
			Page<com.example.demo.domain.Task> notExecutedTask = taskService
					.getNotExecutedTask(PageRequest.of(1 - 1, 2));
			MvcResult result = mockMvc.perform(get("/requestedTask")).andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.model().attribute("notExecutedTask", notExecutedTask)).andReturn();

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
	void receivedTaskにgetリクエストする() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			int userId = user.getLoginUserId(auth);
			String param = null;
			taskService.setReceivedTask(userId, param);
			Page<com.example.demo.domain.Task> notExecutedTask = taskService
					.getNotExecutedTask(PageRequest.of(1 - 1, 2));

			MvcResult result = mockMvc.perform(get("/receivedTask")).andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.model().attribute("notExecutedTask", notExecutedTask)).andReturn();

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
	void 頼みごとの新規登録成功() throws Exception {
		mockMvc.perform(post("/create").param("content", "hoge").param("title", "hoge").param("label", "red")
				.param("deadline", "2020-09-09 12:00:00").param("userAddresseeId", "1")).andExpect(redirectedUrl("/"))
				.andExpect(flash().attribute("successed", "登録が完了しました"));
	}

	@Test
	void 頼みごとの新規登録失敗() throws Exception {

		mockMvc.perform(post("/create").param("content", "")).andExpect(MockMvcResultMatchers.model().hasErrors())
				.andDo(print()).andExpect(view().name("task/createTask"));
	}

	@Test
	void receivedTaskのpost() throws Exception {

		mockMvc.perform(post("/readTask/{id}", 1).param("title", "title").param("content", "hoge").param("label", "red")
				.param("userAddresseeId", "1").param("status", "完了")).andExpect(redirectedUrl("/"))
				.andExpect(flash().attribute("successed", "頼みごとが完了しました"));
	}

	@Test
	void receivedTaskのpostで未完のままにする() throws Exception {
		mockMvc.perform(post("/readTask/{id}", 1).param("title", "hoge").param("content", "hoge").param("label", "red")
				.param("userAddresseeId", "1").param("status", "未完")).andExpect(redirectedUrl("/"))
				.andExpect(flash().attribute("failed", "すでに進行中になっています"));
	}

	@Test
	void delete() throws Exception {
		mockMvc.perform(post("/delete/{id}", 1)).andExpect(redirectedUrl("/"))
				.andExpect(flash().attribute("successed", "削除が完了しました"));

	}
}

@Nested
@WebMvcTest(TaskController.class)
@WithMockUser(username = "hoge", password = "password")
class Task {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TaskService taskService;

	@MockBean
	GetUserInfoService user;

	@MockBean
	TaskNoticeService taskNoticeService;

	@MockBean
	UserDetailsServiceImpl impl;

	@Mock
	TaskForm taskForm;

	@Test
	void readReceivedTaskのget() throws Exception {
		Map<String, String> statusRadio = taskService.getStatusRadio();
		TaskForm form = new TaskForm();
		form.setUserId(1);
		when(taskService.findOne(1)).thenReturn(form);
		mockMvc.perform(get("/readTask/{id}", 1)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("status", statusRadio))
				.andExpect(MockMvcResultMatchers.model().attribute("task", form));
	}

	@Test
	void editのget() throws Exception {
		TaskForm form = new TaskForm();
		form.setUserId(1);
		when(taskService.findOne(1)).thenReturn(form);
		Map<String, String> selectLabel = taskService.getSelectLabel();
		mockMvc.perform(get("/edit/{id}", 1)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("selectLabel", selectLabel))
				.andExpect(MockMvcResultMatchers.model().attribute("taskForm", form))
				.andExpect(view().name("task/editRequestedTask"));
	}

}
