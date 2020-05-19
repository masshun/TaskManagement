package com.example.demo.service.mailService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {

	@Autowired
	JavaMailSender javaMailSender;

	// 引数の簡略化を行いたい
	public boolean sendMail(Map<String, String> map) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom(map.get("from"));
			msg.setTo(map.get("email"));
			msg.setSubject(map.get("title"));
			msg.setText(map.get("content"));
			javaMailSender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			// failed to send
			return false;
		}
		return true;
	}
}
