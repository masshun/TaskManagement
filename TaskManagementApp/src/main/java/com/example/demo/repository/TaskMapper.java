package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;

@Mapper
public interface TaskMapper {
	List<Task> findAllByUserId(int userId);

	List<Task> findAll();

	List<Task> findReceivedTask(int userId);

	List<Task> findCompletedTask(int userId);

	List<Task> findInProgressTask(int userId);

	TaskForm findOne(int id);

	boolean updateCompleted(Task task);

	boolean save(TaskForm form);

	boolean update(TaskForm taskForm);

	boolean delete(int id);

}
