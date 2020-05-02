package com.example.demo.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String username;
	private String password;
	private String email;

}
