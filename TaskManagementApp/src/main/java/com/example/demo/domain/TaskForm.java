package com.example.demo.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import lombok.Data;

@Data
public class TaskForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int userId;
	@NotBlank(message = "必須項目です")
	@Size(max = 50, message = "1文字以上50文字以内で入力してください")
	private String title;
	@NotBlank(message = "必須項目です")
	@Size(max = 255, message = "1文字以上255文字以内で入力してください")
	private String content;
	@NotEmpty(message = "必須項目です")
	private String label;
	@NotNull(message = "必須項目です")
	@Min(value = 1, message = "1以上の数字を入力してください")
	private int userAddresseeId;
	@NotBlank(message = "必須項目です")
	private String deadline;
	private String status;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(TaskForm.class, deadline, new CustomDateEditor(dateFormat, false));
	}

}
