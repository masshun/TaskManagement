package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;

@Mapper
public interface TaskMapper {

	List<Task> findAll();

	List<Task> findAllById(@Param("userId") int userId, @Param("userAddresseeId") int userAddresseeId,
			@Param("param") String param);

	TaskForm findOne(int id);

	boolean updateCompleted(Task task);

	boolean save(TaskForm form);

	boolean update(TaskForm taskForm);

	boolean delete(int id);

	Integer selectTaskCount();

}
