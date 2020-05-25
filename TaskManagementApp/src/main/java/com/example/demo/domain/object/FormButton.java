package com.example.demo.domain.object;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class FormButton {

	private Map<String, String> selectLabel;

	public Map<String, String> initSelectLabel() {
		this.selectLabel = new LinkedHashMap<>();
		selectLabel.put("赤 (重要度:高)", "red");
		selectLabel.put("青 (重要度:中)", "blue");
		selectLabel.put("緑 (重要度:低)", "green");
		return this.selectLabel;
	}

}
