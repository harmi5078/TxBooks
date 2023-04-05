package com.free.comp;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextPane;

import com.free.bean.Paragraphs;
import com.free.common.Constants;

public class TxTextPane extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 557594289035472789L;

	private Paragraphs para = null;

	public TxTextPane() {

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Constants.CurParagraphs = para;

			}
		});

		setEditable(false);
		setFont(new Font("黑体", 0, 22));

	}

	public Paragraphs getPara() {
		return para;
	}

	public void setPara(Paragraphs para) {
		this.para = para;
	}

	@Override
	public String toString() {
		return para.getContent();
	}
}
