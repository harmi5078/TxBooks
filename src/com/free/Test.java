package com.free;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Test extends JPanel {

	public Test() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		this.setBackground(Color.WHITE);
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weighty = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}

	public void addEntry() {
		JLabel label = new JLabel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weighty = 1;
		gbc.weightx = 1;

		gbc.fill = GridBagConstraints.HORIZONTAL;

		label.setText("Label test");
		this.add(label, gbc);
		this.validate();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout(2, 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Test test = new Test();

		JPanel placeholder = new JPanel();
		placeholder.setPreferredSize(new Dimension(200, 200));
		placeholder.setBackground(Color.BLACK);

		frame.add(test, BorderLayout.LINE_START);
		frame.add(placeholder, BorderLayout.CENTER);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		test.addEntry();
		test.addEntry();
		test.addEntry();
		test.addEntry();
		test.addEntry();
		test.addEntry();
	}

}
