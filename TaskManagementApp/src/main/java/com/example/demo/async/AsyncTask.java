package com.example.demo.async;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Task;
import com.example.demo.service.mailService.SendMailService;
import com.example.demo.service.taskService.TaskService;
import com.example.demo.service.userService.GetUserInfoService;

@Component
public class AsyncTask implements Runnable {

	@Autowired
	TaskService taskService;

	@Autowired
	GetUserInfoService getAddressee;

	@Autowired
	SendMailService sendingService;

	@Override
	@Async
	public void run() {
		// タスクを全て洗い出す

		List<Task> task = taskService.findAll();

		// タスクの期限が１時間前であるかどうかチェック
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		List<Task> inProgressTask = task.stream().filter(s -> s.getStatus() == "未完").collect(Collectors.toList());
		LocalDateTime now = timestamp.toLocalDateTime();

		// TODO フィールドは別のクラスで設定する
		int id = 0;
		String addressee = "";
		LocalDateTime deadline = null;
		String email = "";
		String taskTitle = "";
		String title = "頼まれたことはもう終わりましたか？";

		String IPadnPort = "localhost:9996";
		String from = "xxxx@@gmail.com";
		String content = addressee + "さん" + "\n" + "頼みごと: " + "\n" + taskTitle
				+ "の期限が1時間を切りました。以下のリンクにアクセスして頼みごとの内容を確認してください。" + "\n" + "http://" + IPadnPort + "/";

		Map<String, String> map = new HashMap<>();
		map.put("from", from);
		map.put("title", title);

		for (Task t : inProgressTask) {
			deadline = t.getDeadline().toLocalDateTime();
			// 1時間以内であるかどうか
			if ((ChronoUnit.HOURS.between(now, deadline) <= 1 && ChronoUnit.HOURS.between(now, deadline) >= 0)) {
				id = t.getUserAddresseeId();
				addressee = getAddressee.getAddresseeById(id);
				email = getAddressee.getAddreseeMailById(id);
				taskTitle = t.getTitle();

				map.put("email", email);
				map.put("content", content);

				// １時間を切っていたらsendUserIdのアカウントメールに通知を送る
				boolean result = sendingService.sendMail(map);
				if (result) {
					System.out.println("送信完了しました");
				} else {
					System.out.println("送信が失敗しました");
				}

			}
		}
	}

}
