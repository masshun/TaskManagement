package com.example.demo.app;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.AccountForm;
import com.example.demo.domain.Task;
import com.example.demo.service.GetLoginUserService;
import com.example.demo.service.TaskService;

@Controller
@RequestMapping("/")
public class TaskController {

	@Autowired
	GetLoginUserService loginUser;

	@Autowired
	TaskService taskService;

	private Map<String, Boolean> completed;

	private Map<String, Boolean> initCompleted() {
		Map<String, Boolean> radio = new LinkedHashMap<>();
		radio.put("進行中", false);
		radio.put("完了!", true);
		return radio;
	}

	@GetMapping
	public String index(Model model, Principal p) {
		int userId = loginUser.getLoginUserId(p);
		String username = loginUser.getLoginUsername(p);
		System.out.println(taskService.findCompletedTasks(userId));
		model.addAttribute("username", username);
		model.addAttribute("id", userId);
		model.addAttribute("task", taskService.findAll(userId));
		model.addAttribute("taskNotice", taskService.findCompletedTasks(userId));

		return "index";
	}

	@GetMapping("/received")
	public String receiveTask(Model model, Principal p) {
		int userId = loginUser.getLoginUserId(p);
		List<Task> task = taskService.findReceivedTask(userId);

		model.addAttribute("receivedTask", task);
		return "received";
	}

	@GetMapping("/readTask/{id}")
	public String readTask(@PathVariable int id, Model model, AccountForm form) {
		Task task = taskService.findOne(id);
		completed = initCompleted();

		model.addAttribute("completed", completed);
		model.addAttribute("form", form);
		model.addAttribute("task", task);
		return "readTask";
	}

	@PostMapping("/readTask/{id}")
	public String postCompleted(@PathVariable int id, @ModelAttribute Task task,
			RedirectAttributes redirectAttributes) {
		task.setId(id);
		taskService.updateCompleted(task);
		redirectAttributes.addFlashAttribute("successed", "頼みごとが完了しました");
		return "redirect:/";
	}
}
