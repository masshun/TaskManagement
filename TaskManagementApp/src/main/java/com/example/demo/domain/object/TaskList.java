package com.example.demo.domain.object;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.Task;

import lombok.Data;

@Data
@Component
public class TaskList {

	private List<Task> completedTask;

	private List<Task> notExecutedTask;
}
