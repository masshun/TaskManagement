package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;

@Mapper
public interface TaskMapper {
	List<Task> findAll(int userId);

	List<Task> findReceivedTask(int userId);

	List<Task> findCompletedTask(int userId);

	List<Task> findInProgressTask(int userId);

	Task findOne(int id);

	boolean updateCompleted(Task task);

	boolean save(TaskForm form);

	boolean update(Task task);

	boolean delete(int id);

}
