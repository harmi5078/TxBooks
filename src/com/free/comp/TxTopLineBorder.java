package com.free.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class TxTopLineBorder extends AbstractBorder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TxTopLineBorder() {

	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		int w = width;

		g.translate(x, y);

		g.setColor(getShadowColor(c));
		g.drawLine(1, 0, w - 3, 0);

		g.setColor(getHighlightColor(c));

		g.drawLine(1, 1, w - 3, 1);

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
		insets.set(2, 2, 2, 2);
		return insets;
	}
}
