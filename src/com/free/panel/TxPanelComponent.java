package com.free.panel;

import javax.swing.JPanel;

public abstract class TxPanelComponent {

	public abstract JPanel getLeftPanel();

	public abstract JPanel getRightPanel();

	public abstract double getDividerLocation();

}
