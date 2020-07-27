package com.example.demo.utility;

import java.io.Serializable;

import com.example.demo.domain.AccountForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmationToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private String encodedPassword;

	private String id;

	private AccountForm accountForm;

	public ConfirmationToken(String encodedPassword, String randomId, AccountForm accountForm) {
		this.encodedPassword = encodedPassword;
		this.id = randomId;
		this.accountForm = accountForm;
	}

}
