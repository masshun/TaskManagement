package com.example.demo.controller;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.AccountForm;
import com.example.demo.domain.Task;
import com.example.demo.domain.TaskForm;
import com.example.demo.domain.object.PageWrapper;
import com.example.demo.domain.object.SearchForm;
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
	public String index() {
		return "index";
	}

	@GetMapping("/requestedTask")
	public String requestedTask(Model model, Principal p, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		int userId = user.getLoginUserId(p);
		String param = null;
		taskService.setRequestedTask(userId, param);

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(2);

		Page<Task> notExecutedTask = taskService.getNotExecutedTask(PageRequest.of(currentPage - 1, pageSize));
		Page<Task> completedTask = taskService.getCompletedTask(PageRequest.of(currentPage - 1, pageSize));

		// TODO seviceに詰める
		PageWrapper<Task> notExecTaskPageWrapper = taskService.getNotExecTaskPage(notExecutedTask);
		PageWrapper<Task> completedTaskWrapper = new PageWrapper<Task>(completedTask);
		int totalNotExecutedTaskPages = notExecutedTask.getTotalPages();
		int totalCompletedTaskPages = completedTask.getTotalPages();

		model.addAttribute("notExecutedTask", notExecutedTask);
		model.addAttribute("completedTask", completedTask);
		if (totalNotExecutedTaskPages > 0) {
			model.addAttribute("notExecTaskPage", notExecTaskPageWrapper);
		}
		if (totalCompletedTaskPages > 0) {
			model.addAttribute("completedTaskPage", completedTaskWrapper);
		}

		return "task/requestedTask";
	}

	@GetMapping("/receivedTask")
	public String receivedTask(Model model, Principal p, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		int userId = user.getLoginUserId(p);
		String param = null;
		taskService.setReceivedTask(userId, param);

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(2);

		Page<Task> notExecutedTask = taskService.getNotExecutedTask(PageRequest.of(currentPage - 1, pageSize));
		Page<Task> completedTask = taskService.getCompletedTask(PageRequest.of(currentPage - 1, pageSize));

		PageWrapper<Task> notExecTaskPageWrapper = new PageWrapper<Task>(notExecutedTask);
		PageWrapper<Task> completedTaskWrapper = new PageWrapper<Task>(completedTask);
		int totalNotExecutedTaskPages = notExecutedTask.getTotalPages();
		int totalCompletedTaskPages = completedTask.getTotalPages();

		model.addAttribute("notExecutedTask", notExecutedTask);
		model.addAttribute("completedTask", completedTask);
		if (totalNotExecutedTaskPages > 0) {
			model.addAttribute("notExecTaskPage", notExecTaskPageWrapper);
		}
		if (totalCompletedTaskPages > 0) {
			model.addAttribute("completedTaskPage", completedTaskWrapper);
		}

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
			int userId = user.getLoginUserId(p);
			Map<String, String> selectLabel = taskService.getSelectLabel();
			model.addAttribute("selectLabel", selectLabel);
			model.addAttribute("userId", userId);
			model.addAttribute("taskForm", taskForm);
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
	public String editRequiredTask(@PathVariable int id, @ModelAttribute @Validated TaskForm taskForm,
			BindingResult result, Model model, RedirectAttributes redirectAttributes, Principal p) {
		if (!result.hasErrors()) {
			taskForm.setId(id);
			taskForm.setUserId(user.getLoginUserId(p));
			// 削除しない限りタスクを完了扱いにはしない
			taskForm.setStatus("未完");

			taskService.update(taskForm);
			redirectAttributes.addFlashAttribute("successed", "更新が完了しました");
			return "redirect:/";
		} else {
			TaskForm form = taskService.findOne(id);
			Map<String, String> selectLabel = taskService.getSelectLabel();
			model.addAttribute("selectLabel", selectLabel);
			model.addAttribute("taskForm", form);
			return "task/editRequestedTask";
		}

	}

	@PostMapping("/delete/{id}")
	public String deleteRequiredTask(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
		taskService.delete(id);
		redirectAttributes.addFlashAttribute("successed", "削除が完了しました");
		return "redirect:/";
	}

	@GetMapping("/searchRequestedTask")
	public String searchRequestedTask(Principal p, Model model, @ModelAttribute SearchForm form,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int userId = user.getLoginUserId(p);
		taskService.setRequestedTask(userId, form.getParam());
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);

		Page<Task> notExecutedTask = taskService.getNotExecutedTask(PageRequest.of(currentPage - 1, pageSize));
		Page<Task> completedTask = taskService.getCompletedTask(PageRequest.of(currentPage - 1, pageSize));

		PageWrapper<Task> notExecTaskPageWrapper = new PageWrapper<Task>(notExecutedTask);
		PageWrapper<Task> completedTaskWrapper = new PageWrapper<Task>(completedTask);
		int totalNotExecutedTaskPages = notExecutedTask.getTotalPages();
		int totalCompletedTaskPages = completedTask.getTotalPages();

		model.addAttribute("notExecutedTask", notExecutedTask);
		model.addAttribute("completedTask", completedTask);
		if (totalNotExecutedTaskPages > 0) {
			model.addAttribute("notExecTaskPage", notExecTaskPageWrapper);
		}
		if (totalCompletedTaskPages > 0) {
			model.addAttribute("completedTaskPage", completedTaskWrapper);
		}

		return "task/requestedTask";
	}

	@GetMapping("/searchReceivedTask")
	public String searchReceivedTask(Principal p, Model model, @ModelAttribute SearchForm form,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int userId = user.getLoginUserId(p);
		String param = form.getParam();
		taskService.setReceivedTask(userId, param);

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);

		Page<Task> notExecutedTask = taskService.getNotExecutedTask(PageRequest.of(currentPage - 1, pageSize));
		Page<Task> completedTask = taskService.getCompletedTask(PageRequest.of(currentPage - 1, pageSize));

		PageWrapper<Task> notExecTaskPageWrapper = new PageWrapper<Task>(notExecutedTask);
		PageWrapper<Task> completedTaskWrapper = new PageWrapper<Task>(completedTask);
		int totalNotExecutedTaskPages = notExecutedTask.getTotalPages();
		int totalCompletedTaskPages = completedTask.getTotalPages();

		model.addAttribute("notExecutedTask", notExecutedTask);
		model.addAttribute("completedTask", completedTask);
		if (totalNotExecutedTaskPages > 0) {
			model.addAttribute("notExecTaskPage", notExecTaskPageWrapper);
		}
		if (totalCompletedTaskPages > 0) {
			model.addAttribute("completedTaskPage", completedTaskWrapper);
		}

		return "task/receivedTask";
	}

}
