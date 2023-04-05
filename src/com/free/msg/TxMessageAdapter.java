package com.free.msg;

import java.util.HashMap;
import java.util.Vector;

public class TxMessageAdapter {

	public static String MSTYPE_ADDPERSON = "1";
	public static String MSTYPE_ADDPERSONRELATION = "2";
	public static String MSTYPE_SELECTBOOK = "3";
	public static String MSTYPE_SELECTCONTENT = "4";

	private static HashMap<String, Vector<TxMessageListener>> lisenterMap = new HashMap<String, Vector<TxMessageListener>>();

	public static void notiyMessage(String messageType, TxMessage message) {

		Vector<TxMessageListener> v = lisenterMap.get(messageType);

		if (v == null) {
			return;
		}

		for (int i = 0; i < v.size(); i++) {
			v.get(i).notiyMessage(messageType, message);
		}

	}

	public static void addMessageListener(String messageType, TxMessageListener listener) {
		Vector<TxMessageListener> v = lisenterMap.get(messageType);

		if (v == null) {
			v = new Vector<TxMessageListener>();
			lisenterMap.put(messageType, v);
		}

		if (v.contains(listener)) {
			return;
		}

		v.add(listener);
	}

}
