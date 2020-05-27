package com.example.demo.controller;

import java.security.Principal;
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
import com.example.demo.service.taskService.TaskNoticeService;
import com.example.demo.service.taskService.TaskService;
import com.example.demo.service.userService.GetUserInfoService;

@Controller
@RequestMapping("/")
public class TaskController {

	@Autowired
	GetUserInfoService user;

	@Autowired
	TaskNoticeService taskNoticeService;

	@Autowired
	TaskService taskService;

	@GetMapping
	public String index(Model model) {
		return "index";
	}

	@GetMapping("/requestedTask")
	public String requestedTask(Model model, Principal p) {
		int userId = user.getLoginUserId(p);
		taskService.setRequestedTask(userId);

		List<Task> notExecutedTask = taskService.getNotExecutedTask();
		List<Task> completedTask = taskService.getCompletedTask();

		model.addAttribute("notExecutedTask", notExecutedTask);
		model.addAttribute("completedTask", completedTask);
		if (completedTask.isEmpty()) {
			model.addAttribute("emptyCompleted", "完了した頼みごとはありません");
		}
		if (notExecutedTask.isEmpty()) {
			model.addAttribute("emptyNotExecuted", "これから取り組む頼みごとはありません");
		}
		return "task/requestedTask";
	}

	@GetMapping("/receivedTask")
	public String receivedTask(Model model, Principal p) {
		int userId = user.getLoginUserId(p);
		taskService.setReceivedTask(userId);

		List<Task> notExecutedTask = taskService.getNotExecutedTask();
		List<Task> completedTask = taskService.getCompletedTask();
		model.addAttribute("notExecutedTask", notExecutedTask);
		model.addAttribute("completedTask", completedTask);
		if (completedTask.isEmpty()) {
			model.addAttribute("emptyCompleted", "完了した頼みごとはありません");
		}
		if (notExecutedTask.isEmpty()) {
			model.addAttribute("emptyNotExecuted", "これから取り組む頼みごとはありません");
		}

		return "task/receivedTask";
	}

	@GetMapping("/readTask/{id}")
	public String readTask(@PathVariable int id, Model model, AccountForm form) {
		// TODO 検索条件の追加
		TaskForm task = taskService.findOne(id);
		int senderId = task.getUserId();
		String sender = user.getSenderName(senderId);

		Map<String, Boolean> statusRadio = taskService.getStatusRadio();
		model.addAttribute("sender", sender);
		model.addAttribute("status", statusRadio);
		model.addAttribute("form", form);
		model.addAttribute("task", task);
		return "task/readReceivedTask";
	}

	@PostMapping("/readTask/{id}")
	public String postCompleted(@PathVariable int id, @ModelAttribute Task task, RedirectAttributes redirectAttributes,
			Principal p) {
		task.setId(id);
		if (task.getStatus().equals("未完")) {
			redirectAttributes.addFlashAttribute("failed", "すでに進行中になっています");
			return "redirect:/";
		}

		taskService.updateCompleted(task);

		// 送り主にメール通知をする
		int userId = task.getUserId();
		TaskForm form = taskService.findOne(userId);
		taskNoticeService.sendCompletedNoticeByMail(form, p);
		redirectAttributes.addFlashAttribute("successed", "頼みごとが完了しました");
		return "redirect:/";
	}

	@GetMapping("/createTask")
	public String createTask(TaskForm taskForm, Model model, Principal p) {
		int userId = user.getLoginUserId(p);
		// デフォルトは進行中
		Map<String, String> selectLabel = taskService.getSelectLabel();
		model.addAttribute("selectLabel", selectLabel);
		model.addAttribute("userId", userId);
		model.addAttribute("taskForm", taskForm);
		return "task/createTask";
	}

	@PostMapping("/create")
	public String createTask(@ModelAttribute @Validated TaskForm taskForm, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, Principal p) {
		if (!result.hasErrors()) {
			taskForm.setStatus("未完");
			taskService.save(taskForm);

			taskNoticeService.sendNoticeByMail(taskForm, p);
			redirectAttributes.addFlashAttribute("successed", "登録が完了しました");
			return "redirect:/";
		} else {
			model.addAttribute("failed", "入力値に誤りがあります");
			return "task/createTask";
		}
	}

	// TODO 修正
	@GetMapping("/readRequestedTask/{id}")
	public String readTask(@PathVariable int id, Model model) {
		TaskForm taskForm = taskService.findOne(id);
		int addresseeId = taskForm.getUserAddresseeId();
		String addressee = user.getAddresseeById(addresseeId);
		model.addAttribute("addressee", addressee);
		model.addAttribute("taskForm", taskForm);
		return "task/readRequestedTask";
	}

	@GetMapping("/edit/{id}")
	public String editRequiredTask(@PathVariable int id, Model model) {
		TaskForm taskForm = taskService.findOne(id);
		Map<String, String> selectLabel = taskService.getSelectLabel();
		model.addAttribute("selectLabel", selectLabel);
		model.addAttribute("taskForm", taskForm);
		return "task/editRequestedTask";
	}

	@PostMapping("/edit/{id}")
	public String editRequiredTask(@PathVariable int id, @ModelAttribute @Validated TaskForm taskForm, Model model,
			RedirectAttributes redirectAttributes, Principal p) {
		taskForm.setId(id);
		taskForm.setUserId(user.getLoginUserId(p));
		// 削除しない限りタスクを完了扱いにはしない
		taskForm.setStatus("未完");

		taskService.update(taskForm);
		redirectAttributes.addFlashAttribute("successed", "更新が完了しました");
		return "redirect:/";
	}

	@PostMapping("/delete/{id}")
	public String deleteRequiredTask(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
		taskService.delete(id);
		redirectAttributes.addFlashAttribute("successed", "削除が完了しました");
		return "redirect:/";
	}
}
