package com.example.demo.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class TaskList {

	private List<Task> completedTask;

	private List<Task> notExecutedTask;
}
