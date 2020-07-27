package com.example.demo.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountForm implements Serializable {
	// データはSerializeして格納するため、セッション情報はSerializableを実装する必要がある

	private static final long serialVersionUID = 1L;

	private int id;
	@NotEmpty(message = "必須項目です")
	@Size(max = 30, message = "1文字以上30文字以内で入力してください")
	private String username;

	@Size(min = 8, max = 255, message = "8文字以上255文字以内で入力してください")
	@Pattern(regexp = "[a-zA-Z0-9]*", message = "半角英数字を入力してください")
	private String password;

	@Size(min = 8, max = 255, message = "8文字以上255文字以内で入力してください")
	@Pattern(regexp = "[a-zA-Z0-9]*", message = "半角英数字を入力してください")
	private String confPassword;

	@Size(max = 255, message = "255文字以内で入力してください")
	@Pattern(regexp = "^([\\w])+([\\w\\._-])*\\@([\\w])+([\\w\\._-])*\\.([a-zA-Z])+$", message = "正しいメールアドレスを入力してください")
	private String email;

}
