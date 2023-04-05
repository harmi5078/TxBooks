package com.free.bean;

public class Book {

	private String name = null;
	private int albumId;
	private String chiefAlbum;
	private int volumeId;
	private String volume;
	private int bid;

	private int bgtime;
	private int endtime;

	private int status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		if (obj instanceof Book) {
			return this.getBid() == ((Book) obj).getBid();
		} else {
			return false;
		}

	}

	public String getChiefAlbum() {
		return chiefAlbum;
	}

	public void setChiefAlbum(String chiefAlbum) {
		this.chiefAlbum = chiefAlbum;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public int getBgtime() {
		return bgtime;
	}

	public void setBgtime(int bgtime) {
		this.bgtime = bgtime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(int volumeId) {
		this.volumeId = volumeId;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

}
