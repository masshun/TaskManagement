package com.example.demo.domain.object;

public class Event {
	private int id;
	private String title;

	/**
	 * カレンダーの開始日付
	 */
	private String start;

	/**
	 * カレンダーの終了日付
	 */
	private String end;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}
