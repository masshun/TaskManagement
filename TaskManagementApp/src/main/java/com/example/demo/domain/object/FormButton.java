package com.example.demo.domain.object;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class FormButton {

	private Map<String, String> selectLabel;

	private Map<String, String> statusRadio;

	public Map<String, String> initSelectLabel() {
		this.selectLabel = new LinkedHashMap<>();
		selectLabel.put("赤 (重要度:高)", "red");
		selectLabel.put("青 (重要度:中)", "blue");
		selectLabel.put("緑 (重要度:低)", "green");
		return this.selectLabel;
	}

	public Map<String, String> initStatusRadio() {
		this.statusRadio = new LinkedHashMap<>();
		statusRadio.put("未完", "未完");
		statusRadio.put("完了!", "完了");
		return this.statusRadio;
	}

}
