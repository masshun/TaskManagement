package com.example.demo.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

@Data
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
}
