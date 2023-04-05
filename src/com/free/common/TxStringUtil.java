package com.free.common;

public class TxStringUtil {

	public final static java.text.SimpleDateFormat sdfShortTimePlus = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		return "".equals(str.trim());
	}

	public static String getNowSortPlusTime() {
		String nowDate = "";
		try {
			java.sql.Date date = null;
			date = new java.sql.Date(new java.util.Date().getTime());
			nowDate = sdfShortTimePlus.format(date);
			return nowDate;
		} catch (Exception e) {
			return "20150101010101";
		}
	}

	public static void main(String[] args) {
		System.out.println(getNowSortPlusTime());
	}
}
