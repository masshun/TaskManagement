package com.example.demo.service.mailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendingMailService {

	@Autowired
	JavaMailSender javaMailSender;

	// ラムダ式にして引数の簡略化を行いたい
	public boolean sendMail(String from, String email, String title, String content) {
		return true;
	}
}
