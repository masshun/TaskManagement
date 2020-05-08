package com.example.demo.service.taskService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.domain.TaskForm;
import com.example.demo.service.userService.GetAddresseeService;

@Service
public class CreateTaskService {

	@Autowired
	GetAddresseeService getAddressee;

	@Autowired
	JavaMailSender javaMailSender;

	public boolean sendNoticeByMail(TaskForm taskForm, Principal p) {
		// フィールドは別に作る
		String sender = p.getName();
		int addresseeId = taskForm.getSendUserId();
		String addressee = getAddressee.getAdresseeById(addresseeId);
		String email = getAddressee.getAdreseeMailById(addresseeId);
		String taskTitle = taskForm.getTitle();

		// フィールドは別に作る
		String IPadnPort = "localhost:9996";
		String from = "xxxx@@gmail.com";
		String title = sender + "さん「" + taskTitle + "」";

		String content = addressee + "さん" + "\n" + sender + "さんからの頼みごとです。" + "\n" + "以下のリンクにアクセスして頼みごとの内容を確認してください。"
				+ "\n" + "http://" + IPadnPort + "/recievedTask";

		// 別に作る
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom(from);
			msg.setTo(email);
			msg.setSubject(title);
			msg.setText(content);
			javaMailSender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			// failed to send
			return false;
		}
		return true;
	}
}
