package com.example.demo.domain.object;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Mail {

	private final String PORT = "localhost:9996";
	private final String FROM = "hoge@email.com";

}
