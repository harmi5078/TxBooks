package com.free;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.free.common.Constants;
import com.free.msg.TxMessage;
import com.free.msg.TxMessageAdapter;
import com.free.msg.TxMessageListener;
import com.free.panel.TxBookPanel;
import com.free.panel.TxContentPanel;
import com.free.panel.TxPanelComponent;

public class TxMain extends JFrame implements TxMessageListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();

	JButton jbBook = new JButton("书单列表");
	JButton jbPerson = new JButton("人物列表");

	JSplitPane jSplitPane = new JSplitPane();
	JPanel mainPanel = new JPanel();

	TxBookPanel bookPanel = new TxBookPanel();
	TxContentPanel contentPanel = null;

	public TxMain() {

		init();

		TxMessageAdapter.addMessageListener(TxMessageAdapter.MSTYPE_SELECTBOOK, this);
		TxMessageAdapter.addMessageListener(TxMessageAdapter.MSTYPE_SELECTCONTENT, this);
	}

	private void init() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		mainPanel.setLayout(new BorderLayout());

		JPanel jpTop = new JPanel();
		jpTop.setLayout(new BorderLayout());
//		jpTop.add(jbBook, BorderLayout.WEST);
//		jpTop.add(jbPerson, BorderLayout.EAST);

		initAction();

		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		mainPanel.add(jSplitPane, BorderLayout.CENTER);
		mainPanel.add(jpTop, BorderLayout.NORTH);

		contentPane.add(mainPanel, BorderLayout.CENTER);

		Rectangle bounds = new Rectangle(scrSize);
		this.setBounds(bounds);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(false);
		this.setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);// 别忘关闭窗口
	}

	private void initAction() {

		jbBook.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showBookList();
			}

		});

		jbPerson.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showContent();
			}

		});

	}

	private void loadPanel(TxPanelComponent panel) {
		leftPanel.removeAll();
		leftPanel.add(panel.getLeftPanel(), BorderLayout.CENTER);
		leftPanel.revalidate();

		rightPanel.removeAll();
		rightPanel.add(panel.getRightPanel(), BorderLayout.CENTER);

		rightPanel.revalidate();
		jSplitPane.setDividerLocation(panel.getDividerLocation());
		mainPanel.repaint();
	}

	protected void showContent() {

		if (contentPanel == null) {
			contentPanel = new TxContentPanel();
		}
		loadPanel(contentPanel);

		contentPanel.loadBookContents(Constants.curBook);

	}

	private void showContentDetail(TxMessage message) {

//		if (contentDetailPanel == null) {
//			contentDetailPanel = new TxContentDetailPanel();
//		}
//		loadPanel(contentDetailPanel);
//
//		Paragraphs para = (Paragraphs) message.getValue("p");
//		ArrayList<Paragraphs> pList = (ArrayList<Paragraphs>) message.getValue("plist");
//
//		contentDetailPanel.setParagraphs(para);
//		contentDetailPanel.setParagraphList(pList);

	}

	protected void showBookList() {

		loadPanel(bookPanel);

	}

	public static void main(String[] args) {

		TxMain m = new TxMain();

	}

	@Override
	public void notiyMessage(String messageType, TxMessage message) {

		if (TxMessageAdapter.MSTYPE_SELECTBOOK.equals(messageType)) {
			showContent();
		}

		if (TxMessageAdapter.MSTYPE_SELECTCONTENT.equals(messageType)) {
			showContentDetail(message);
		}

	}

}
