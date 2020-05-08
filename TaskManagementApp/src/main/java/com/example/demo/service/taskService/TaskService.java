package com.example.demo.service.taskService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;
import com.example.demo.repository.TaskMapper;

@Service
@Transactional
public class TaskService {

	@Autowired
	TaskMapper taskMapper;

	public List<Task> findAll(int userId) {
		return taskMapper.findAll(userId);
	}

	public List<Task> findReceivedTask(int userId) {
		return taskMapper.findReceivedTask(userId);
	}

	public TaskForm findOne(int id) {
		return taskMapper.findOne(id);
	}

	public List<Task> findCompletedTask(int userId) {
		return taskMapper.findCompletedTask(userId);

	}

	public List<Task> findInProgressTask(int userId) {
		return taskMapper.findInProgressTask(userId);
	}

	public boolean updateCompleted(Task task) {
		return taskMapper.updateCompleted(task);
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

}
