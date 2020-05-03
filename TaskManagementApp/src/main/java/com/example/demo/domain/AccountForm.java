package com.example.demo.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class AccountForm implements Serializable {
	// データはSerializeして格納するため、セッション情報はSerializableを実装する必要がある
	/**
	 * あとでチェック
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String username;
	private String password;
	private String email;

}
