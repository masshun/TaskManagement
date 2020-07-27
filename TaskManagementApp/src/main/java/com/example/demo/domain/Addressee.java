package com.example.demo.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Addressee implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;

}
