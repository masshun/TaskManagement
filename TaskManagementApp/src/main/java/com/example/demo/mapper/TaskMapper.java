package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Task;

@Mapper
public interface TaskMapper {
	List<Task> findAll();

	Task findOne(int id);

	void save(Task dateCal);

	boolean update(Task dateCal);

	boolean delete(int id);

}