package com.example.demo.app;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.AccountForm;
import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;
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
		model.addAttribute("username", username);
		model.addAttribute("id", userId);
		return "index";
	}

	@GetMapping("/requestedTask")
	public String requestedTask(Model model, Principal p) {
		int userId = loginUser.getLoginUserId(p);
		List<Task> inProgress = taskService.findInProgressTask(userId);
		List<Task> completed = taskService.findCompletedTask(userId);
		model.addAttribute("inProgress", inProgress);
		model.addAttribute("completed", completed);
		return "requestedTask";
	}

	@GetMapping("/receivedTask")
	public String receivedTask(Model model, Principal p) {
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

	@GetMapping("/createTask")
	public String createTask(TaskForm taskForm, Model model, Principal p) {
		int userId = loginUser.getLoginUserId(p);
		// デフォルトは進行中
		model.addAttribute("userId", userId);
		model.addAttribute("taskForm", taskForm);
		return "createTask";
	}

	@PostMapping("/create")
	public String createTask(@ModelAttribute @Validated TaskForm taskForm, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			taskForm.setCompleted(false);
			taskService.save(taskForm);
			redirectAttributes.addFlashAttribute("successed", "登録が完了しました");
			return "redirect:/";
		} else {
			model.addAttribute("failed", "入力値に誤りがあります");
			return "createTask";
		}
	}
}
