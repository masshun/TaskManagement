package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.object.Event;
import com.example.demo.service.taskService.TaskService;
import com.example.demo.service.userService.GetUserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/event")
public class RestWebController {

	@Autowired
	TaskService taskService;

	@Autowired
	GetUserInfoService user;

	@GetMapping("/all")
	public String getEvents(Principal p) {
		String jsonMsg = null;
		try {
			List<Event> events = new ArrayList<Event>();
			Event event = new Event();
			List<com.example.demo.domain.Task> task = taskService.findAll();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int id = user.getLoginUserId(p);
			final List<com.example.demo.domain.Task> loginUserTask = task.stream()
					.filter(c -> c.getUserAddresseeId() == id).collect(Collectors.toList());
			loginUserTask.forEach(t -> {
				String deadline = sdf.format(t.getDeadline());
				event.setId(t.getId());
				event.setTitle(t.getTitle());
				event.setStart(deadline);
				events.add(event);
			});

			// FullCalendarにエンコード済み文字列を渡す
			ObjectMapper mapper = new ObjectMapper();
			jsonMsg = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
		} catch (IOException ioex) {
			System.out.println(ioex.getMessage());
		}
		return jsonMsg;
	}
}
