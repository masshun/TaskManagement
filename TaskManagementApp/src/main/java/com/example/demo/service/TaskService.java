package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Task;
import com.example.demo.mapper.TaskMapper;

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

	public Task findOne(int id) {
		return taskMapper.findOne(id);
	}

	public boolean updateCompleted(Task task) {
		return taskMapper.updateCompleted(task);
	}
}
