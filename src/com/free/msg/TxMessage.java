package com.free.msg;

import java.util.HashMap;

public class TxMessage {

	public static String NAME_BOOK = "b";
	public static String NAME_PARA = "p";
	public static String NAME_PARALIST = "plist";

	private HashMap<String, Object> params = new HashMap<String, Object>();

	public void putValue(String name, Object value) {
		params.put(name, value);
	}

	public Object getValue(String name) {
		return params.get(name);
	}
}
