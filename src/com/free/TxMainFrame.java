package com.free;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.free.bean.Book;
import com.free.bean.Paragraphs;
import com.free.common.UIUtility;
import com.free.msg.TxMessage;
import com.free.msg.TxMessageAdapter;
import com.free.msg.TxMessageListener;
import com.free.panel.TxBookListPanel;
import com.free.panel.TxOneContentPanel;

public class TxMainFrame extends JFrame implements TxMessageListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Container contentPane;

	JTabbedPane tabPanel = new JTabbedPane();

	public TxMainFrame() {
		init();

		TxBookListPanel bookListPanel = new TxBookListPanel();
		tabPanel.add(bookListPanel, "书单列表");
		bookListPanel.loadChiefs();

		TxMessageAdapter.addMessageListener(TxMessageAdapter.MSTYPE_SELECTCONTENT, this);
	}

	private void init() {

		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		contentPane.add(mainPanel, BorderLayout.CENTER);

		mainPanel.add(tabPanel, BorderLayout.CENTER);
		UIUtility.setFont16(mainPanel);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();

		System.out.println("scrSize.width:" + scrSize.width);
		Rectangle bounds = new Rectangle(scrSize);
		this.setBounds(bounds);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(false);
		this.setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);// 别忘关闭窗口
	}

	public static void main(String[] args) {
		TxMainFrame m = new TxMainFrame();
	}

	@Override
	public void notiyMessage(String messageType, TxMessage message) {
		if (TxMessageAdapter.MSTYPE_SELECTCONTENT.equals(messageType)) {
			showContentDetail(message);
		}

	}

	@SuppressWarnings("unchecked")
	private void showContentDetail(TxMessage message) {

		Book book = (Book) message.getValue(TxMessage.NAME_BOOK);

		Paragraphs para = (Paragraphs) message.getValue(TxMessage.NAME_PARA);

		ArrayList<Paragraphs> pList = (ArrayList<Paragraphs>) message.getValue(TxMessage.NAME_PARALIST);

		TxOneContentPanel panel = new TxOneContentPanel();

		int indexof = pList.indexOf(para) + 1;

		String title = book.getName() + "(" + indexof + ")";

		tabPanel.addTab(title, panel);
		panel.setParagraphs(para);
		panel.setParagraphList(pList);

		tabPanel.setSelectedComponent(panel);
	}
}
