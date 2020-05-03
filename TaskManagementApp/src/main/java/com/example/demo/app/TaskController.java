package com.example.demo.app;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.TaskService;

@Controller
@RequestMapping("/")
@Transactional
public class TaskController {

	@Autowired
	AccountMapper accountMapper;

	@Autowired
	TaskService taskService;

	@GetMapping
	public String index(Model model, Principal p) {

		String username = p.getName();
		Account ac = accountMapper.findByUsername(username);
		int id = ac.getId();
		model.addAttribute("id", id);

		return "index";
	}
}
