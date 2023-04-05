package com.free.comp;

import java.awt.Dimension;

import javax.swing.JTextField;

public class TxTextField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 807325361164791087L;

	public TxTextField() {
		super();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dimemsopm = super.getPreferredSize();
		dimemsopm.setSize(dimemsopm.getWidth(), 30);
		return dimemsopm;
	}

}
