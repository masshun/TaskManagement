package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.async.AsyncTask;

@Component
@EnableScheduling
public class DeadlineNoticeController {

	@Autowired
	private AsyncTask asyncTask;

	// 1時間ごとに期限チェックの実行 秒分時日月曜日
	@Scheduled(cron = "0 0 */1  * * *", zone = "Asia/Tokyo")
	public void checkDeadline() {
		this.doTask();
	}

	public void doTask() {
		asyncTask.run();
	}
}
