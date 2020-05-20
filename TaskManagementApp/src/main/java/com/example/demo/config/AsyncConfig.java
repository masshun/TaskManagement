package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// スレッドを並列でいくつ立てるか
		executor.setCorePoolSize(5);
		// キューが最大になった時スレッドの数をいくつまで増やすか
		executor.setMaxPoolSize(10);
		// キューはいくつまで積むか
		executor.setQueueCapacity(25);
		return executor;
	}
}
