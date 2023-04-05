package com.free.panel;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.free.bean.Book;
import com.free.bean.Paragraphs;
import com.free.common.UIUtility;
import com.free.comp.TxListCellRenderer;
import com.free.dao.SqliteAdapter;
import com.free.msg.TxMessage;
import com.free.msg.TxMessageAdapter;

public class TxBookContentsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JList<Paragraphs> jlistContent = new JList<>();
	DefaultListModel<Paragraphs> listModelContent = new DefaultListModel<Paragraphs>();

	JPanel jpContent = new JPanel();

	private Book book = null;
	private ArrayList<Paragraphs> paraList = null;

	public TxBookContentsPanel(Book book) {
		this.book = book;
		init();
		initAction();
	}

	private void init() {

		JScrollPane sjpContents = new JScrollPane(jlistContent);

		jlistContent.setFont(UIUtility.TEXT_FONT);
		jlistContent.setCellRenderer(new TxListCellRenderer());
		jlistContent.setFixedCellWidth(20);
		jpContent.setLayout(new BorderLayout());
		jpContent.add(sjpContents, BorderLayout.CENTER);

		setLayout(new BorderLayout());
		add(jpContent, BorderLayout.CENTER);
	}

	private void initAction() {

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

				message.putValue(TxMessage.NAME_BOOK, book);
				message.putValue(TxMessage.NAME_PARA, para);
				message.putValue(TxMessage.NAME_PARALIST, pList);

				TxMessageAdapter.notiyMessage(TxMessageAdapter.MSTYPE_SELECTCONTENT, message);

			}
		});
	}

	public void loadBookContents() {

		paraList = (ArrayList<Paragraphs>) SqliteAdapter.selectBookContents(book);
		if (paraList == null) {
			return;
		}
		listModelContent.removeAllElements();
		listModelContent.addAll(paraList);
		jlistContent.setModel(listModelContent);

	}

	private int changes = 0;

	public void reLoadBookContents() {
		if (paraList == null) {
			return;
		}

		if (changes < 2) {
			changes++;

			listModelContent.removeAllElements();
			listModelContent.addAll(paraList);
			jlistContent.setModel(listModelContent);
		}
	}

}
