package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;
import java.util.OptionalInt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.domain.TaskForm;
import com.example.demo.service.taskService.TaskNoticeService;
import com.example.demo.service.taskService.TaskService;
import com.example.demo.service.userService.GetUserInfoService;
import com.example.demo.utility.PageWrapper;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@Sql(statements = {
		"INSERT INTO user(username, password, email) VALUES ('ユーザー1', '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa', 'hoge@email.com')",
		"INSERT INTO user(username, password, email) VALUES ('ユーザー2', '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa', 'hoge@email.com')",
		"INSERT INTO user_addressee(name) VALUES('ユーザー1')", "INSERT INTO user_addressee(name) VALUES('ユーザー2')" })
@WithMockUser(username = "ユーザー1", password = "password")
public class TaskControllerTest {

	private MockMvc mockMvc;

	@Autowired
	TaskService taskService;

	@MockBean
	GetUserInfoService user;

	@MockBean
	TaskNoticeService task;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@InjectMocks
	private TaskController controller;

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity(springSecurityFilterChain))
				.alwaysDo(log()).build();
		MockitoAnnotations.initMocks(this);
		mockMvc.perform(formLogin("/login").user("ユーザー1").password("password"));
	}

	@Test
	void indexのget() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

	@Test
	void calendarのget() throws Exception {
		mockMvc.perform(get("/calendar")).andExpect(status().isOk()).andExpect(view().name("calendar"));
	}

	@Test
	void requestedTaskのget() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			int userId = user.getLoginUserId(auth);

			String param = null;
			taskService.setRequestedTask(userId, param);
			Page<com.example.demo.domain.Task> notExecutedTask = taskService
					.getNotExecutedTask(PageRequest.of(1 - 1, 2));

			mockMvc.perform(get("/requestedTask")).andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.model().attribute("notExecutedTask", notExecutedTask));
			PageWrapper<com.example.demo.domain.Task> notExecTaskPageWrapper = new PageWrapper<com.example.demo.domain.Task>(
					notExecutedTask);
			assertEquals(notExecTaskPageWrapper.getSize(), 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void receivedTaskのget() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			int userId = user.getLoginUserId(auth);
			String param = null;
			taskService.setReceivedTask(userId, param);
			Page<com.example.demo.domain.Task> notExecutedTask = taskService
					.getNotExecutedTask(PageRequest.of(1 - 1, 2));
			PageWrapper<com.example.demo.domain.Task> notExecTaskPageWrapper = new PageWrapper<com.example.demo.domain.Task>(
					notExecutedTask);

			mockMvc.perform(get("/receivedTask")).andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.model().attribute("notExecutedTask", notExecutedTask));

			assertEquals(notExecTaskPageWrapper.getSize(), 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	void createの成功() throws Exception {
		TaskForm taskForm = new TaskForm();
		taskForm.setAddresseeName("ユーザー2");
		taskForm.setContent("hoge");
		taskForm.setDeadline("2020-09-09 12:00:00");
		taskForm.setTitle("hoge");
		taskForm.setLabel("red");
		OptionalInt i = OptionalInt.of(2);
		when(user.getAddresseeId(taskForm.getAddresseeName())).thenReturn(i);
		mockMvc.perform(post("/createTask").param("content", "hoge").param("title", "hoge").param("label", "red")
				.param("deadline", "2020-09-09 12:00:00").param("addresseeName", "ユーザー2")
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(redirectedUrl("/"))
				.andExpect(flash().attribute("successed", "登録が完了しました"));
	}

	@Test
	void createの失敗() throws Exception {
		mockMvc.perform(post("/createTask").param("content", "").param("title", "hoge").param("label", "red")
				.param("deadline", "2020-09-09 12:00:00").param("addresseeName", "ユーザー1")
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.model().hasErrors())
				.andExpect(view().name("task/createTask"));
	}

	@Test
	void receivedTaskのpost() throws Exception {
		mockMvc.perform(post("/updateReceivedTask/{id}", 1).param("title", "title").param("content", "hoge")
				.param("label", "red").param("addresseeName", "foo").param("status", "完了")
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(redirectedUrl("/"))
				.andExpect(flash().attribute("successed", "頼みごとが完了しました"));
	}

	@Test
	void receivedTaskのpostで未完のままにする() throws Exception {
		mockMvc.perform(post("/updateReceivedTask/{id}", 1).param("title", "hoge").param("content", "hoge")
				.param("label", "red").param("addresseeName", "foo").param("status", "未完")
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(redirectedUrl("/"))
				.andExpect(flash().attribute("failed", "すでに進行中になっています"));
	}

	@Test
	void delete() throws Exception {
		mockMvc.perform(post("/delete/{id}", 1).with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(redirectedUrl("/")).andExpect(flash().attribute("successed", "削除が完了しました"));
	}

	@Test
	void editのget() throws Exception {
		TaskForm taskForm = getTaskForm();
		taskService.save(taskForm);
		String addresseeName = "ユーザー1";
		Optional<String> addresseeNameOpt = Optional.of(addresseeName);
		when(user.getAddresseeById(taskForm.getUserAddresseeId())).thenReturn(addresseeNameOpt);
		mockMvc.perform(get("/edit/{id}", 1)).andExpect(status().isOk())
				.andExpect(view().name("task/editRequestedTask"));
	}

	@Test
	void editのpost() throws Exception {
		TaskForm taskForm = getTaskForm();
		taskService.save(taskForm);
		int id = taskForm.getId();
		OptionalInt i = OptionalInt.of(id);
		when(user.getAddresseeId(taskForm.getAddresseeName())).thenReturn(i);
		mockMvc.perform(post("/edit/{id}", id).param("title", "hoge").param("content", "hoge")
				.param("addresseeName", "ユーザー1").param("deadline", "2020-09-09 12:00:00").param("label", "red")
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(redirectedUrl("/"));
	}

	@Test
	void editのpost失敗() throws Exception {
		TaskForm taskForm = getTaskForm();
		taskService.save(taskForm);
		int id = taskForm.getId();
		OptionalInt i = OptionalInt.of(id);
		when(user.getAddresseeId(taskForm.getAddresseeName())).thenReturn(i);
		mockMvc.perform(post("/edit/{id}", id).param("title", "").param("content", "hoge")
				.param("addresseeName", "ユーザー1").param("deadline", "2020-09-09 12:00:00").param("label", "red")
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(view().name("task/editRequestedTask"));
	}

	public TaskForm getTaskForm() {
		TaskForm taskForm = new TaskForm();
		taskForm.setAddresseeName("ユーザー1");
		taskForm.setContent("hoge");
		taskForm.setDeadline("2020-09-09 12:00:00");
		taskForm.setTitle("hoge");
		taskForm.setLabel("red");
		taskForm.setUserId(2);
		taskForm.setUserAddresseeId(15);
		taskForm.setStatus("未完");
		return taskForm;
	}
}
