package com.example.demo.domain.object;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class FormButton {

	private Map<String, String> selectLabel;

	private Map<String, Boolean> statusRadio;

	public Map<String, String> initSelectLabel() {
		this.selectLabel = new LinkedHashMap<>();
		selectLabel.put("赤 (重要度:高)", "red");
		selectLabel.put("青 (重要度:中)", "blue");
		selectLabel.put("緑 (重要度:低)", "green");
		return this.selectLabel;
	}

	public Map<String, Boolean> initStatusRadio() {
		this.statusRadio = new LinkedHashMap<>();
		statusRadio.put("未完", false);
		statusRadio.put("完了!", true);
		return this.statusRadio;
	}

}
