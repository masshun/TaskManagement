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

	private String id;

	private AccountForm accountForm;

	public ConfirmationToken(String password, String id, AccountForm accountForm) {
		this.password = password;
		this.id = id;
		this.accountForm = accountForm;
	}

}
