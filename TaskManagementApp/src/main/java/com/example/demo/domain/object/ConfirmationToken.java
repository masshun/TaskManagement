package com.example.demo.domain.object;

import java.io.Serializable;

import com.example.demo.domain.AccountForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmationToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String password;

	private String confirmationToken;

	private AccountForm accountForm;

	public ConfirmationToken(String password, String confirmationToken, AccountForm accountForm) {
		this.password = password;
		this.confirmationToken = confirmationToken;
		this.accountForm = accountForm;
	}

}
