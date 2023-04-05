package com.free.bean;

public class Person {

	private int pid;
	private String name;
	private int nation;
	private String nationName;
	private String title;
	private String addr;
	private String detail;
	private int dynasty;

	private String dynastyName;

	public int getNation() {
		return nation;
	}

	public void setNation(int nation) {
		this.nation = nation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getDynasty() {
		return dynasty;
	}

	public void setDynasty(int dynasty) {
		this.dynasty = dynasty;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDynastyName() {
		return dynastyName;
	}

	public void setDynastyName(String dynastyName) {
		this.dynastyName = dynastyName;
	}

	public String toString() {
		String str = name;

		if (detail != null) {
			str = str + "," + detail;
		}

		return str;
	}

	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}
}
