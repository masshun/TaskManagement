package com.example.demo.utility;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.Task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class TaskList {

	private List<Task> completedTask;

	private List<Task> notExecutedTask;
}
