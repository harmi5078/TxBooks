package com.free.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class TxBottomLineBorder extends AbstractBorder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TxBottomLineBorder() {

	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		int h = height;
		int w = width;

		g.translate(x, y);

//		g.setColor(getHighlightColor(c));
//		g.drawLine(0, 0, 0, h - 2);
//		g.drawLine(1, 0, w - 2, 0);
//
//		g.setColor(getHighlightColor(c));
//		g.drawLine(1, 1, 1, h - 3);
//		g.drawLine(2, 1, w - 3, 1);

		g.setColor(getHighlightColor(c));
		g.drawLine(0, h - 1, w - 1, h - 1);
//		g.drawLine(w - 1, 0, w - 1, h - 2);

		g.setColor(getShadowColor(c));
		g.drawLine(1, h - 2, w - 2, h - 2);
//		g.drawLine(w - 2, 1, w - 2, h - 3);

		g.translate(-x, -y);
	}

	private Color getHighlightColor(Component c) {

		return c.getBackground().brighter();
	}

	private Color getShadowColor(Component c) {
		return c.getBackground().darker();
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.set(1, 1, 1, 1);
		return insets;
	}
}
