package com.example.demo.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

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
import com.example.demo.service.taskService.TaskNoticeService;
import com.example.demo.service.taskService.TaskService;
import com.example.demo.service.userService.GetUserInfoService;
import com.example.demo.utility.PageWrapper;
import com.example.demo.utility.SearchForm;

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

	@GetMapping("/calendar")
	public String calendar() {
		return "calendar";
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

		return "task/receivedTask";
	}

	@GetMapping("/updateReceivedTask/{id}")
	public String updateReceivedTask(@PathVariable int id, Model model, AccountForm form) {
		TaskForm task = taskService.findOne(id);
		int senderId = task.getUserId();
		String sender = user.getSenderName(senderId);

		Map<String, String> statusRadio = taskService.getStatusRadio();
		model.addAttribute("sender", sender);
		model.addAttribute("status", statusRadio);
		model.addAttribute("form", form);
		model.addAttribute("task", task);
		return "task/updateReceivedTask";
	}

	@PostMapping("/updateReceivedTask/{id}")
	public String postCompleted(@PathVariable int id, @ModelAttribute TaskForm task,
			RedirectAttributes redirectAttributes, Principal p) {
		task.setId(id);
		if (task.getStatus().equals("未完")) {
			redirectAttributes.addFlashAttribute("failed", "すでに進行中になっています");
			return "redirect:/";
		}

		// 送り主にメール通知をする
		taskService.updateCompleted(task);
		taskNoticeService.sendTaskCompletedNoticeByMail(task, p);
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

	@PostMapping("/createTask")
	public String createTask(@ModelAttribute @Validated TaskForm taskForm, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, Principal p) {
		int userId = user.getLoginUserId(p);
		OptionalInt addresseeId = user.getAddresseeId(taskForm.getAddresseeName());
		if (result.hasErrors() || addresseeId.isEmpty()) {
			Map<String, String> selectLabel = taskService.getSelectLabel();
			model.addAttribute("selectLabel", selectLabel);
			model.addAttribute("userId", userId);
			model.addAttribute("taskForm", taskForm);
			model.addAttribute("result", "入力内容に誤りがあります");
			return "task/createTask";
		}
		int id = addresseeId.getAsInt();
		taskForm.setUserAddresseeId(id);
		taskForm.setStatus("未完");
		taskService.save(taskForm);

		taskNoticeService.sendTaskCreatedNoticeByMail(taskForm, p);
		redirectAttributes.addFlashAttribute("successed", "登録が完了しました");
		return "redirect:/";
	}

	@GetMapping("/readRequestedTask/{id}")
	public String readRequestedTask(@PathVariable int id, Model model) {
		TaskForm taskForm = taskService.findOne(id);
		int addresseeId = taskForm.getUserAddresseeId();
		Optional<String> addresseeOpt = user.getAddresseeById(addresseeId);
		String addressee = addresseeOpt.get();
		model.addAttribute("addressee", addressee);
		model.addAttribute("taskForm", taskForm);
		return "task/readRequestedTask";
	}

	@GetMapping("/edit/{id}")
	public String editRequestedTask(@PathVariable int id, Model model) throws ParseException {
		TaskForm taskForm = taskService.findOne(id);
		Map<String, String> selectLabel = taskService.getSelectLabel();
		Optional<String> addresseeName = user.getAddresseeById((taskForm.getUserAddresseeId()));
		// TODO Formクラスで行う
		String str = taskForm.getDeadline().substring(0, 16);
		String ad = str.replaceAll(" ", "");
		StringBuilder sb = new StringBuilder(ad);
		sb.insert(10, "T");
		taskForm.setDeadline(sb.toString());
		model.addAttribute("addresseeName", addresseeName.get());
		model.addAttribute("selectLabel", selectLabel);
		model.addAttribute("taskForm", taskForm);
		return "task/editRequestedTask";
	}

	@PostMapping("/edit/{id}")
	public String editRequestedTask(@PathVariable int id, @ModelAttribute @Validated TaskForm taskForm,
			BindingResult result, Model model, RedirectAttributes redirectAttributes, Principal p) {
		OptionalInt addresseeIdOpt = user.getAddresseeId(taskForm.getAddresseeName());
		if (result.hasErrors() || addresseeIdOpt.isEmpty()) {
			TaskForm form = taskService.findOne(id);
			Map<String, String> selectLabel = taskService.getSelectLabel();
			model.addAttribute("selectLabel", selectLabel);
			model.addAttribute("taskForm", form);
			model.addAttribute("failed", "入力値に問題があります");
			return "task/editRequestedTask";
		}
		int addresseeId = addresseeIdOpt.getAsInt();
		taskForm.setUserAddresseeId(addresseeId);
		taskForm.setId(id);
		taskForm.setUserId(user.getLoginUserId(p));
		// 削除しない限りタスクを完了扱いにはしない
		taskForm.setStatus("未完");

		taskService.update(taskForm);
		taskNoticeService.sendTaskEditedNoticeByMail(taskForm, p);
		redirectAttributes.addFlashAttribute("successed", "更新が完了しました");
		return "redirect:/";
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

		return "task/requestedTask";
	}

	@GetMapping("/searchReceivedTask")
	public String searchReceivedTask(Principal p, Model model, @ModelAttribute SearchForm form,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int userId = user.getLoginUserId(p);
		String param = form.getParam();
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

		return "task/receivedTask";
	}

}
