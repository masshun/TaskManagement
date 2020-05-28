package com.example.demo.domain.object;

import lombok.Data;

@Data
public class Mail {
	private String title;
	private String content;
	private final String PORT = "localhost:9996";
	private final String FROM = "hoge@email.com";

}
