package com.free.bean;

public class Events {

	private int id;
	private String name;
	private String keys;
	private String addr;
	private String details;
	private int bgTime;
	private int endTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getBgTime() {
		return bgTime;
	}

	public void setBgTime(int bgTime) {
		this.bgTime = bgTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String toString() {
		String str = name;

		if (details != null) {
			str = str + "," + details;
		}

		return str;
	}

}
