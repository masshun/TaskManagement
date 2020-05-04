package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Task;

@Mapper
public interface TaskMapper {
	List<Task> findAll(int userId);

	List<Task> findReceivedTask(int userId);

	Task findOne(int id);

	boolean updateCompleted(Task task);

	void save(Task task);

	boolean update(Task task);

	boolean delete(int id);

}
