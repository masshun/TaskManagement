package com.example.demo.service.taskService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.TaskForm;
import com.example.demo.service.mailService.SendMailService;
import com.example.demo.service.userService.GetUserInfoService;

@Service
public class TaskNoticeService {

	@Autowired
	GetUserInfoService getAddressee;

	@Autowired
	SendMailService sendingService;

	public boolean sendNoticeByMail(TaskForm taskForm, Principal p) {
		// TODO あとでフィールドは別に作って呼び出す リレーションに変更しておく
		String sender = p.getName();
		int id = taskForm.getUserAddresseeId();
		String addressee = getAddressee.getAddresseeById(id);
		String email = getAddressee.getAddreseeMailById(id);
		String taskTitle = taskForm.getTitle();

		// TODO ここもあとでフィールドは別に作って呼び出す
		String IPadnPort = "localhost:9996";
		String from = "xxxx@@gmail.com";
		String title = sender + "さん「" + taskTitle + "」";

		String content = addressee + "さん" + "\n" + sender + "さんからの頼みごとです。" + "\n" + "以下のリンクにアクセスして頼みごとの内容を確認してください。"
				+ "\n" + "http://" + IPadnPort + "/";

		Map<String, String> map = new HashMap<>();
		map.put("from", from);
		map.put("email", email);
		map.put("title", title);
		map.put("content", content);

		sendingService.sendMail(map);

		return true;
	}

	public boolean sendCompletedNoticeByMail(TaskForm taskForm, Principal p) {
		String sender = p.getName();
		int id = taskForm.getUserId();
		String addressee = getAddressee.getAddresseeById(id);
		String email = getAddressee.getAddreseeMailById(id);
		String taskTitle = taskForm.getTitle();

		String IPadnPort = "localhost:9996";
		String from = "xxxx@@email.com";
		String title = sender + "さん「" + taskTitle + "」完了!";

		String content = addressee + "さん" + "\n" + sender + "さんの頼みごとが終わりました!" + "\n" + "以下のリンクにアクセスして頼みごとの内容を確認してください。"
				+ "\n" + "http://" + IPadnPort + "/";

		Map<String, String> map = new HashMap<>();
		map.put("from", from);
		map.put("email", email);
		map.put("title", title);
		map.put("content", content);

		sendingService.sendMail(map);

		return true;
	}
}
