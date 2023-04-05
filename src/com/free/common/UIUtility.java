package com.free.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;

public class UIUtility {

	public static Font TEXT_FONT = new Font("华光准圆_CNKI", 0, 20);

	public static Font TEXT_FONT_18 = new Font("华光准圆_CNKI", 0, 18);

	public static Font TEXT_FONT_16 = new Font("华光准圆_CNKI", 0, 16);

	public static Color TEXT_BG = new Color(170, 213, 255);
//	public static Color TEXT_BG = new Color(111, 185, 120);

	public static void setFont16(JComponent component) {

		component.setFont(UIUtility.TEXT_FONT_16);
		Component[] comps = component.getComponents();

		if (comps == null || comps.length == 0) {
			return;
		}

		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof JComponent) {
				setFont16((JComponent) comps[i]);
			}
		}
	}

}
