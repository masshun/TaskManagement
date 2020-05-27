package com.example.demo.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
	@NotBlank(message = "必須項目です")
	@Size(min = 1, max = 50, message = "1文字以上20文字以下で入力してください")
	private String title;
	@NotBlank(message = "必須項目です")
	@Size(min = 1, max = 255, message = "1文字以上255文字以下で入力してください")
	private String content;
	@NotBlank(message = "必須項目です")
	private String label;
	private int userAddresseeId;
	@NotBlank(message = "必須項目です")
	@Future(message = "現在以降の日時を指定してください")
	private String deadline;
	private String status;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(TaskForm.class, deadline, new CustomDateEditor(dateFormat, false));
	}

}
