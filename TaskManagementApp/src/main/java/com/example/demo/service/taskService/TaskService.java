package com.example.demo.service.taskService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;
import com.example.demo.domain.object.FormButton;
import com.example.demo.domain.object.PageWrapper;
import com.example.demo.domain.object.TaskList;
import com.example.demo.repository.TaskMapper;

@Service
@Transactional
public class TaskService {

	@Autowired
	TaskMapper taskMapper;

	@Autowired
	TaskList task;

	@Autowired
	FormButton form;

	public List<Task> findAll() {
		return taskMapper.findAll();
	}

	public TaskForm findOne(int userId) {
		return taskMapper.findOne(userId);
	}

	public void setReceivedTask(@Param("userId") int userId, @Param("param") String param) {
		int userAddresseeId = 0;
		List<Task> all = taskMapper.findAllById(userId, userAddresseeId, param);

		List<Task> notExecuted = all.stream().filter(s -> s.getStatus().equals("未完")).collect(Collectors.toList());
		task.setNotExecutedTask(notExecuted);

		List<Task> completed = all.stream().filter(s -> s.getStatus().equals("完了")).collect(Collectors.toList());
		task.setCompletedTask(completed);
	}

	public void setRequestedTask(@Param("userId") int userAddresseeId, @Param("param") String param) {
		int userId = 0;
		List<Task> all = taskMapper.findAllById(userId, userAddresseeId, param);

		// 完了した頼みごと
		List<Task> completed = all.stream().filter(s -> s.getStatus().equals("完了")).collect(Collectors.toList());
		task.setCompletedTask(completed);

		// 未完の頼みごと
		List<Task> notExecuted = all.stream().filter(s -> s.getStatus().equals("未完")).collect(Collectors.toList());
		task.setNotExecutedTask(notExecuted);
	}

	public Page<Task> getCompletedTask(Pageable pageable) {
		List<Task> all = task.getCompletedTask();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Task> list;

		if (all.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, all.size());
			list = all.subList(startItem, toIndex);
		}

		Page<Task> page = new PageImpl<Task>(list, PageRequest.of(currentPage, pageSize), all.size());

		return page;
	}

	public Page<Task> getNotExecutedTask(Pageable pageable) {
		List<Task> all = task.getNotExecutedTask();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Task> list;

		if (all.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, all.size());
			list = all.subList(startItem, toIndex);
		}

		Page<Task> page = new PageImpl<Task>(list, PageRequest.of(currentPage, pageSize), all.size());

		return page;
	}

	public boolean updateCompleted(Task task) {
		return taskMapper.updateCompleted(task);
	}

	public PageWrapper<Task> getNotExecTaskPage(Page<Task> notExecutedTask) {
		PageWrapper<Task> notExecTaskPage = new PageWrapper<Task>(notExecutedTask);
		return notExecTaskPage;
	}

	public boolean save(TaskForm form) {
		boolean result = taskMapper.save(form);
		if (result) {
			return true;
		} else {
			// Exceptionの実装
			return false;
		}
	}

	// 変換
	public boolean update(TaskForm taskForm) {
		boolean result = taskMapper.update(taskForm);
		if (result) {
			return true;
		} else {
			// Exceptionの実装
			return false;
		}
	}

	public boolean delete(int id) {
		boolean result = taskMapper.delete(id);
		if (!result) {
			// exception
		}
		return true;
	}

	public Map<String, String> getSelectLabel() {
		Map<String, String> selectLabel = form.initSelectLabel();
		return selectLabel;
	}

	public Map<String, Boolean> getStatusRadio() {
		Map<String, Boolean> statusRadio = form.initStatusRadio();
		return statusRadio;
	}

}
