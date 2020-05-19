package com.example.demo.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskForm implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private int id;
	private int userId;
	private String title;
	private String content;
	private String label;
	private int userAddresseeId;
	private String deadline;
	private String status;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(TaskForm.class, deadline, new CustomDateEditor(dateFormat, false));
	}

}
