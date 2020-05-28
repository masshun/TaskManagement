package com.example.demo.service.mailService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {

	@Autowired
	JavaMailSender javaMailSender;

	@Async
	public void sendMail(Map<String, String> map) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(map.get("from"));
		msg.setTo(map.get("email"));
		msg.setSubject(map.get("title"));
		msg.setText(map.get("content"));
		javaMailSender.send(msg);

	}
}
