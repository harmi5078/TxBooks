package com.free.comp;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.free.common.UIUtility;

public class TxListCellRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel p;

	private JTextArea ta;

	public TxListCellRenderer() {
		p = new JPanel();
		p.setLayout(new BorderLayout());

		// text
		ta = new JTextArea();
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setFont(UIUtility.TEXT_FONT);
		ta.setBackground(UIUtility.TEXT_BG);
		p.add(ta, BorderLayout.CENTER);

		p.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

		p.setBackground(ta.getBackground());
	}

	@Override
	public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
			final boolean isSelected, final boolean hasFocus) {

		int ii = index + 1;
		ta.setText(ii + "." + value.toString());

//		int width = list.getWidth();
//
//		System.out.println(width);
//
//		if (width == 0)
//			ta.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.8166), Short.MAX_VALUE);

		if (isSelected) {
			ta.setBorder(new TxBottomLineBorder());
		} else {
			ta.setBorder(BorderFactory.createEmptyBorder());
		}
		return p;

	}

}
