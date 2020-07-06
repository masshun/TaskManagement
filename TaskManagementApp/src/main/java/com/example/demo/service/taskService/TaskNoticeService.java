package com.example.demo.service.taskService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.MailPropConfig;
import com.example.demo.domain.TaskForm;
import com.example.demo.service.mailService.SendMailService;
import com.example.demo.service.userService.GetUserInfoService;

@Service
public class TaskNoticeService {

	@Autowired
	MailPropConfig prop;

	@Autowired
	GetUserInfoService user;

	@Autowired
	SendMailService sendingService;

	// TODO フォームクラスを作成する
	public boolean sendTaskCreatedNoticeByMail(TaskForm taskForm, Principal p) {
		String sender = p.getName();
		int id = taskForm.getUserAddresseeId();
		String addressee = user.getAddresseeById(id).get();
		String email = user.getAddreseeMailById(id);
		String taskTitle = taskForm.getTitle();
		String taskContent = taskForm.getContent();

		String port = prop.get("port");
		String from = prop.get("mailaddress");
		String title = sender + "さんからの頼みごと「" + taskTitle + "」";

		String content = addressee + "さん" + "\n" + sender + "さんからの頼みごとです。" + "\n" + "詳細: " + taskContent + "\n"
				+ "頼みごとが完了したら、以下のリンクから更新手続きを行ってください" + "\n" + "http://" + port + "/";

		Map<String, String> map = new HashMap<>();
		map.put("from", from);
		map.put("email", email);
		map.put("title", title);
		map.put("content", content);

		sendingService.sendMail(map);

		return true;
	}

	public void sendTaskCompletedNoticeByMail(TaskForm taskForm, Principal p) {
		String sender = p.getName();
		// 頼みごとを依頼したユーザー情報を取得
		int id = taskForm.getUserId();
		Optional<String> addressee = user.getAddresseeById(id);
		String email = user.getAddreseeMailById(id);
		String taskTitle = taskForm.getTitle();
		String taskContent = taskForm.getContent();

		String address = prop.get("mailaddress");
		String port = prop.get("port");

		String title = sender + "さんの「" + taskTitle + "」が終わりました!";

		String content = addressee + "さん" + "\n" + sender + "さんの頼みごとが終わりました!" + "\n" + "詳細: " + taskContent + "\n"
				+ "以下のリンクにアクセスして頼みごとを確認してください。" + "\n" + "http://" + port;

		Map<String, String> map = new HashMap<>();
		map.put("from", address);
		map.put("email", email);
		map.put("title", title);
		map.put("content", content);

		sendingService.sendMail(map);
	}

	public void sendTaskEditedNoticeByMail(TaskForm taskForm, Principal p) {
		String sender = p.getName();
		int id = taskForm.getUserAddresseeId();
		Optional<String> addressee = user.getAddresseeById(id);
		String email = user.getAddreseeMailById(id);
		String taskTitle = taskForm.getTitle();
		String taskContent = taskForm.getContent();

		String address = prop.get("mailaddress");
		String port = prop.get("port");

		String title = sender + "さんの「" + taskTitle + "」の内容が変更されました!";

		String content = addressee + "さん" + "\n" + sender + "さんからの頼みごとの内容が変更されました。" + "\n" + "詳細: " + taskContent + "\n"
				+ "頼みごとが完了したら、以下のリンクから更新手続きを行ってください" + "\n" + "心当たりがない場合はお手数ですが、このメールを削除してください。" + "\n" + "http://"
				+ port + "/";

		Map<String, String> map = new HashMap<>();
		map.put("from", address);
		map.put("email", email);
		map.put("title", title);
		map.put("content", content);

		sendingService.sendMail(map);
	}

}
