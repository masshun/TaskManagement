package com.example.demo.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int userId;
	private String title;
	private String content;
	private String label;
	private int userAddresseeId;
	private Timestamp deadline;
	private String status;

	// many to many
	public List<Account> account;
	public List<Addressee> addressee;

}
