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
	// TODO 可変オブジェクトの修正
	private Timestamp deadline;

	public Timestamp getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
	}

	private String status;

	// many to many
	public List<Account> account;
	public List<Addressee> addressee;

}
