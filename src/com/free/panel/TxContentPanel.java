package com.free.panel;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.free.bean.Book;
import com.free.bean.Paragraphs;
import com.free.common.UIUtility;
import com.free.comp.TxListCellRenderer;
import com.free.comp.TxTextField;
import com.free.dao.SqliteAdapter;
import com.free.msg.TxMessage;
import com.free.msg.TxMessageAdapter;

public class TxContentPanel extends TxPanelComponent {

	JList<Paragraphs> jlistContent = new JList<>();
	DefaultListModel<Paragraphs> listModelContent = new DefaultListModel<Paragraphs>();

	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();

	public TxContentPanel() {
		initPanel();
	}

	private void initPanel() {

		initLeftPanel();
		initRightPanel();

		jlistContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int clickTis = e.getClickCount();
				if (clickTis < 2) {
					return;
				}

				Paragraphs para = jlistContent.getSelectedValue();

				if (para == null) {
					return;
				}

				Enumeration<Paragraphs> paraList = (Enumeration<Paragraphs>) listModelContent.elements();

				ArrayList<Paragraphs> pList = new ArrayList<Paragraphs>();
				while (paraList.hasMoreElements()) {
					pList.add(paraList.nextElement());
				}

				TxMessage message = new TxMessage();

				message.putValue("p", para);
				message.putValue("plist", pList);

				TxMessageAdapter.notiyMessage(TxMessageAdapter.MSTYPE_SELECTCONTENT, message);

			}
		});
	}

	private void initRightPanel() {

		JPanel jpContent = new JPanel();
		jpContent.setLayout(new BorderLayout());

		JScrollPane sjpContents = new JScrollPane(jlistContent);
		jpContent.add(sjpContents, BorderLayout.CENTER);
		jlistContent.setFont(UIUtility.TEXT_FONT);
		jlistContent.setCellRenderer(new TxListCellRenderer());

		jpContent.setBackground(UIUtility.TEXT_BG);
		sjpContents.setBackground(UIUtility.TEXT_BG);

		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(jpContent, BorderLayout.CENTER);

	}

	private void initLeftPanel() {
		JTextField tfWords = new TxTextField();
		JTextField tfPerson = new TxTextField();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel jpName = new JPanel();
		jpName.setLayout(new BorderLayout());

		JPanel jpName1 = new JPanel();
		jpName1.setLayout(new BorderLayout());
		JLabel labName = new JLabel("内容:");
		labName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpName1.add(labName, BorderLayout.WEST);
		jpName1.add(tfWords, BorderLayout.CENTER);
		jpName1.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
		jpName.add(jpName1, BorderLayout.NORTH);

		JPanel jpNation = new JPanel();
		jpNation.setLayout(new BorderLayout());
		JPanel jpNation1 = new JPanel();
		jpNation1.setLayout(new BorderLayout());
		JLabel labNation = new JLabel("国家:");
		labNation.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpNation1.add(labNation, BorderLayout.WEST);
		jpNation1.add(tfPerson, BorderLayout.CENTER);

		jpNation.add(jpNation1, BorderLayout.NORTH);

		jpName.add(jpNation, BorderLayout.CENTER);
		mainPanel.add(jpName, BorderLayout.CENTER);

		mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 10, 0, 8));

		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(mainPanel, BorderLayout.CENTER);

		UIUtility.setFont16(leftPanel);
	}

	public JPanel getLeftPanel() {
		return leftPanel;
	}

	public JPanel getRightPanel() {
		return rightPanel;
	}

	public void loadBookContents(Book selectBook) {

		ArrayList<Paragraphs> paraList = (ArrayList<Paragraphs>) SqliteAdapter.selectBookContents(selectBook);
		if (paraList == null) {
			return;
		}
		listModelContent.removeAllElements();
		listModelContent.addAll(paraList);
		jlistContent.setModel(listModelContent);
		jlistContent.revalidate();
		rightPanel.repaint();
	}

	@Override
	public double getDividerLocation() {
		return 0.21;
	}

}
