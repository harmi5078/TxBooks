package com.free.comp;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.free.bean.Events;
import com.free.common.TxStringUtil;
import com.free.common.UIUtility;

public class TxEventListCellRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel p;

	private JTextArea ta;
	private JTextArea ta2;

	public TxEventListCellRenderer() {
		p = new JPanel();
		p.setLayout(new BorderLayout());

		// text
		ta = new JTextArea();
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setFont(UIUtility.TEXT_FONT_16);
		ta.setBackground(UIUtility.TEXT_BG);

		ta2 = new JTextArea();
		ta2.setLineWrap(true);
		ta2.setWrapStyleWord(true);
		ta2.setFont(UIUtility.TEXT_FONT_16);
		ta2.setBackground(UIUtility.TEXT_BG);

//		p.add(ta2, BorderLayout.NORTH);
		p.add(ta, BorderLayout.CENTER);

		p.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));

		p.setBackground(ta.getBackground());
	}

	@Override
	public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
			final boolean isSelected, final boolean hasFocus) {

		Events evt = (Events) value;

		String pStr = "";

		if (!TxStringUtil.isEmpty(evt.getName())) {
			pStr = evt.getName();
		}

//		if (!TxStringUtil.isEmpty(person.getTitle())) {
//			if (TxStringUtil.isEmpty(pStr)) {
//				pStr = pStr + person.getTitle();
//			} else {
//				pStr = pStr + "," + person.getTitle();
//			}
//		}
//
//		if (!TxStringUtil.isEmpty(person.getDynastyName())) {
//			if (TxStringUtil.isEmpty(pStr)) {
//				pStr = pStr + person.getDynastyName();
//			} else {
//				pStr = pStr + "," + person.getDynastyName();
//			}
//		}

		if (!TxStringUtil.isEmpty(evt.getDetails())) {
			if (TxStringUtil.isEmpty(pStr)) {
				pStr = pStr + evt.getDetails();
			} else {
				pStr = pStr + "," + evt.getDetails();
			}
		}

		ta.setText(pStr);
		ta2.setText(evt.getName());

		int width = list.getWidth();

		if (width > 0) {
			ta.setSize(width, Short.MAX_VALUE);
		}

		if (isSelected) {
			ta.setBorder(new TxBottomLineBorder());
		} else {
			ta.setBorder(BorderFactory.createEmptyBorder());
		}
		return p;

	}
}
