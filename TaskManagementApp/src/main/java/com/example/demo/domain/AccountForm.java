package com.example.demo.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountForm implements Serializable {
	// データはSerializeして格納するため、セッション情報はSerializableを実装する必要がある
	/**
	 * あとでチェック
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	@NotBlank(message = "必須項目です")
	@Size(min = 1, max = 30, message = "1文字以上30文字以内で入力してください")
	private String username;
	@NotBlank(message = "必須項目です")
	@Pattern(regexp = "[a-zA-Z0-9]*", message = "入力は英数字である必要があります")
	private String password;
	@NotBlank(message = "必須項目です")
	@Pattern(regexp = "^([\\w])+([\\w\\._-])*\\@([\\w])+([\\w\\._-])*\\.([a-zA-Z])+$", message = "メールアドレスを入力してください")
	private String email;

}
