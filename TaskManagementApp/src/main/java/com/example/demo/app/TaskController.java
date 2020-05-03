package com.example.demo.app;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.AccountForm;
import com.example.demo.domain.AccountSessionData;
import com.example.demo.service.TaskService;

@Controller
@RequestMapping("/")
@Transactional
public class TaskController {

	@Autowired
	AccountSessionData accountSessionData;

	@Autowired
	TaskService taskService;

	// お試してService経由無しでセッション管理を行ってみる
	@GetMapping
	public String index(@Validated AccountForm accountForm, BindingResult bindingResult, Model model,
			Principal principal) {
		accountSessionData.setId(1);
		accountSessionData.setUsername("hoge");
		accountSessionData.setEmail("hoge@email.com");
		accountSessionData.setPassword("password");

		model.addAttribute("sesUsername", accountSessionData.getUsername());
		model.addAttribute("email", accountSessionData.getEmail());

		model.addAttribute("task", taskService.findAll());
		String name = principal.getName();
		model.addAttribute("username", name);
		return "index";
	}
}
