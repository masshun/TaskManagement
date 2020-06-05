package com.example.demo.repository;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.TaskForm;

@MybatisTest
@TestPropertySource(properties = "test.properties")
@Transactional
@Sql(statements = {
		"INSERT INTO task(title, content, label, deadline, user_id, user_addressee_id, status) VALUES ('タイトル', 'コンテンツ', 'red', '2020-09-09 12:00:00', 1, 2, '未完')",
		"INSERT INTO task(title, content, label, deadline, user_id, user_addressee_id, status) VALUES ('買い物', '食材を買う', 'red', '2020-09-08 12:00:00', 2, 1, '完了')", })
public class TaskMapperTest {

	@Autowired
	TaskMapper taskMapper;

	// private static Logger logger = LoggerFactory.getLogger(TaskMapperTest.class);

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

	@Disabled
	@Test
	void 頼みごとの更新() {
		TaskForm form = getInsertTaskData();
		taskMapper.save(form);

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

	private TaskForm getSetCompletedData() {
		TaskForm form = new TaskForm();
		form.setTitle("忘れ物を取りに行く");
		form.setContent("駅で落とした忘れ物を取りに行く");
		form.setLabel("red");
		form.setDeadline("2020-08-07 12:00:00");
		form.setStatus("未完");
		form.setUserId(2);
		form.setUserAddresseeId(1);
		return form;
	}
}
