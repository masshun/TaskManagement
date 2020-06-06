package com.example.demo.service.taskService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.TaskForm;
import com.example.demo.domain.object.Mail;
import com.example.demo.service.mailService.SendMailService;
import com.example.demo.service.userService.GetUserInfoService;

@Service
public class TaskNoticeService {

	@Autowired
	GetUserInfoService getAddressee;

	@Autowired
	SendMailService sendingService;

	@Autowired
	Mail mail;

	public boolean sendNoticeByMail(TaskForm taskForm, Principal p) {

		String sender = p.getName();
		int id = taskForm.getUserAddresseeId();
		String addressee = getAddressee.getAddresseeById(id);
		String email = getAddressee.getAddreseeMailById(id);
		String taskTitle = taskForm.getTitle();

		String port = mail.getPORT();
		String from = mail.getFROM();
		String title = sender + "さん「" + taskTitle + "」";

		String content = addressee + "さん" + "\n" + sender + "さんからの頼みごとです。" + "\n" + "以下のリンクにアクセスして頼みごとの内容を確認してください。"
				+ "\n" + "http://" + port + "/";

		Map<String, String> map = new HashMap<>();
		map.put("from", from);
		map.put("email", email);
		map.put("title", title);
		map.put("content", content);

		sendingService.sendMail(map);

		return true;
	}

	public void sendCompletedNoticeByMail(TaskForm taskForm, Principal p) {
		String sender = p.getName();
		int id = taskForm.getUserId();
		String addressee = getAddressee.getAddresseeById(id);
		String email = getAddressee.getAddreseeMailById(id);
		String taskTitle = taskForm.getTitle();

		String port = mail.getPORT();
		String from = mail.getFROM();
		String title = sender + "さん「" + taskTitle + "」完了!";

		String content = addressee + "さん" + "\n" + sender + "さんの頼みごとが終わりました!" + "\n" + "以下のリンクにアクセスして頼みごとの内容を確認してください。"
				+ "\n" + "http://" + port + "/";

		Map<String, String> map = new HashMap<>();
		map.put("from", from);
		map.put("email", email);
		map.put("title", title);
		map.put("content", content);

		sendingService.sendMail(map);
	}
}
