package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/mail.properties")
public class MailPropConfig {

	@Autowired
	private Environment env;

	public String get(String key) {
		return env.getProperty(key);
	}

}
