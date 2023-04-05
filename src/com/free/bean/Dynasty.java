package com.free.bean;

public class Dynasty {

	private int id;
	private String name;
	private String creator;

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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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

	public String toString() {
		return name + "(" + getYear(bgTime) + "至" + getYear(endTime) + ")";
	}

	public static String getYear(int year) {

		if (year == 0) {
			return "公元元年";
		}

		if (year < 0) {
			return "公元前" + year * -1;
		} else {
			return "公元" + year;
		}
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		if (obj instanceof Dynasty) {
			return this.getId() == ((Dynasty) obj).getId();
		} else {
			return false;
		}

	}
}
