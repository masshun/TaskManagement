package com.example.demo.utility;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.config.MailPropConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailForm {
	@Autowired
	MailPropConfig prop;
	
	String sender;
	String addressee;
	String taskTitle;
	String taskContent;
	String port = prop.get("port");
}
