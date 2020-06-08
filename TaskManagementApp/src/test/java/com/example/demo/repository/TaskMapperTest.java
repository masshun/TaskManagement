package com.example.demo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;

@MybatisTest
@TestPropertySource(properties = "test.properties")
@Transactional
@Sql(statements = {
		"INSERT INTO task(title, content, label, deadline, user_id, user_addressee_id, status) VALUES ('タイトル', 'コンテンツ', 'red', '2020-09-09 12:00:00', 1, 2, '未完')",
		"INSERT INTO task(title, content, label, deadline, user_id, user_addressee_id, status) VALUES ('買い物', '食材を買う', 'red', '2020-09-08 12:00:00', 2, 1, '完了')",
		"INSERT INTO user(username, password, email) VALUES ('ユーザー1', 'password', 'hoge@email.com')",
		"INSERT INTO user(username, password, email)VALUES('ユーザー2', 'password', 'hoge2@email.com')",
		"INSERT INTO user_addressee(name) VALUES('ユーザー1')", "INSERT INTO user_addressee(name)VALUES('ユーザー2')" })
public class TaskMapperTest {

	@Autowired
	TaskMapper taskMapper;

	@Autowired
	AccountMapper accountMapper;

	@Test
	void 頼みごとの新規登録() throws Exception {
		TaskForm form = getInsertTaskData();
		taskMapper.save(form);
		TaskForm actual = taskMapper.findOne(form.getId());
		assertEquals(actual.toString(), form.toString());
	}

	@Test
	void 頼まれたことの完了更新() throws Exception {
		TaskForm form = getInsertTaskData();
		form.setStatus("完了");
		taskMapper.save(form);
		taskMapper.updateCompleted(form);
		TaskForm actual = taskMapper.findOne(form.getId());
		assertEquals(actual.toString(), form.toString());
	}

	@Test
	void 頼みごとの更新() throws Exception {
		TaskForm form = getInsertTaskData();
		taskMapper.save(form);
		form.setTitle("洗濯物を干す");
		taskMapper.update(form);
		TaskForm actual = taskMapper.findOne(form.getId());
		assertEquals(actual.toString(), form.toString());
	}

	@Test
	void 頼みごとの削除() throws Exception {
		TaskForm form = getInsertTaskData();
		taskMapper.save(form);
		taskMapper.delete(form.getId());
		TaskForm deletedForm = taskMapper.findOne(form.getId());
		assertNull(deletedForm);
	}

	@Test
	void 全件取得() throws Exception {
		List<Task> list = taskMapper.findAll();
		Optional<Task> one = list.stream().findFirst();
		assertEquals("コンテンツ", one.get().getContent());
	}

	@Test
	void 検索による頼みごとリストの取得() throws Exception {
		TaskForm form = getCompletedData();
		taskMapper.save(form);
		int userId = 0;
		int userAddresseeId = 0;
		String param = null;
		List<Task> list = taskMapper.findAllById(userId, userAddresseeId, param);
		Optional<Task> one = list.stream().findFirst();
		assertEquals("駅で落とした忘れ物を取りに行く", one.get().getContent());
	}

	@Test
	void 曖昧検索() throws Exception {
		TaskForm form = getForSearchData();
		taskMapper.save(form);
		int userId = 0;
		int userAddresseeId = 0;
		String param = "提出";
		List<Task> list = taskMapper.findAllById(userId, userAddresseeId, param);
		Optional<Task> one = list.stream().findFirst();
		assertEquals("書類を提出する", one.get().getTitle());
	}

	private TaskForm getInsertTaskData() {
		TaskForm form = new TaskForm();
		form.setTitle("洗濯物を取り込む");
		form.setContent("洗濯物を取り込む");
		form.setLabel("red");
		form.setDeadline("2020-08-08 12:00:00");
		form.setStatus("未完");
		form.setUserId(2);
		form.setUserAddresseeId(1);
		return form;
	}

	private TaskForm getCompletedData() {
		TaskForm form = new TaskForm();
		form.setTitle("忘れ物を取りに行く");
		form.setContent("駅で落とした忘れ物を取りに行く");
		form.setLabel("red");
		form.setDeadline("2020-08-07 12:00:00");
		form.setStatus("完了");
		form.setUserId(5);
		form.setUserAddresseeId(6);
		return form;
	}

	private TaskForm getForSearchData() {
		TaskForm form = new TaskForm();
		form.setTitle("書類を提出する");
		form.setContent("上司に書類を提出する");
		form.setLabel("red");
		form.setDeadline("2020-08-08 12:00:00");
		form.setStatus("未完");
		form.setUserId(13);
		form.setUserAddresseeId(14);
		return form;

	}

}
