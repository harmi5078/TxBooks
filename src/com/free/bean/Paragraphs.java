package com.free.bean;

public class Paragraphs {

	int bid;
	int gid;
	String content;
	String transcation;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Paragraphs) {
			return this.getGid() == ((Paragraphs) obj).getGid();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {

		if (content != null) {
			return content.trim().replace("\t", "");
		}

		return null;

	}

	public String getTranscation() {
		return transcation;
	}

	public void setTranscation(String transcation) {
		this.transcation = transcation;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
