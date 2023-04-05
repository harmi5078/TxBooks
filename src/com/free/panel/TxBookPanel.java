package com.free.panel;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.free.bean.Book;
import com.free.common.Constants;
import com.free.common.UIUtility;
import com.free.comp.TxListCellRenderer;
import com.free.dao.SqliteAdapter;
import com.free.msg.TxMessageAdapter;

public class TxBookPanel extends TxPanelComponent {

	JList<Book> jlistBook = new JList<>();
	DefaultListModel<Book> listModelBook = new DefaultListModel<Book>();

	JPanel jpCenter = new JPanel();

	JComboBox<Book> cbBookName = new JComboBox<Book>();
	JComboBox<Book> cbVolume = new JComboBox<Book>();

	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();

	public TxBookPanel() {
		initPanel();
	}

	private void initPanel() {

		initLeftPanel();
		initRightPanel();

		initAction();

		loadChiefs();
		loadVolumes();
		loadBooks();

	}

	private void initAction() {
		cbBookName.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					loadVolumes();
				}
			}
		});

		cbVolume.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}

				Book volume = (Book) cbVolume.getSelectedItem();

				Constants.bgtime = volume.getBgtime();
				Constants.endtime = volume.getEndtime();

				loadBooks();
			}
		});

		jlistBook.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int clickTis = e.getClickCount();
				if (clickTis < 2) {
					return;
				}

				Book book = jlistBook.getSelectedValue();

				if (book == null) {
					return;
				}

				Constants.curBook = book;
				TxMessageAdapter.notiyMessage(TxMessageAdapter.MSTYPE_SELECTBOOK, null);

			}
		});
	}

	public JPanel getLeftPanel() {
		return leftPanel;
	}

	public JPanel getRightPanel() {
		return rightPanel;
	}

	private void initRightPanel() {

		JPanel jpFilterBook = new JPanel();
		jpFilterBook.setLayout(new BorderLayout());

		JPanel jpBName = new JPanel();
		jpBName.setLayout(new BorderLayout());

		JPanel jpBName1 = new JPanel();
		jpBName1.setLayout(new BorderLayout());
		JLabel labName = new JLabel("书名:");
		labName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpBName1.add(labName, BorderLayout.WEST);
		jpBName1.add(cbBookName, BorderLayout.CENTER);
		jpBName1.setBorder(BorderFactory.createEmptyBorder(14, 0, 10, 0));
		jpBName.add(jpBName1, BorderLayout.NORTH);

		JPanel jpVolumn = new JPanel();
		jpVolumn.setLayout(new BorderLayout());
		JPanel jpVolumn1 = new JPanel();
		jpVolumn1.setLayout(new BorderLayout());
		JLabel labNation = new JLabel("卷册:");
		labNation.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpVolumn1.add(labNation, BorderLayout.WEST);
		jpVolumn1.add(cbVolume, BorderLayout.CENTER);

		jpVolumn.add(jpVolumn1, BorderLayout.NORTH);
		jpVolumn.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		jpBName.add(jpVolumn, BorderLayout.CENTER);
		jpFilterBook.add(jpBName, BorderLayout.CENTER);

		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(jpFilterBook, BorderLayout.CENTER);

		UIUtility.setFont16(rightPanel);
	}

	private void initLeftPanel() {

		JPanel jpContent = new JPanel();

		JScrollPane sjpContents = new JScrollPane(jlistBook);
		jpContent.add(sjpContents, BorderLayout.CENTER);
		jlistBook.setFont(UIUtility.TEXT_FONT);
		jlistBook.setCellRenderer(new TxListCellRenderer());
		jlistBook.setFixedCellWidth(20);

		jpContent.setBackground(UIUtility.TEXT_BG);
		sjpContents.setBackground(UIUtility.TEXT_BG);

		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(sjpContents, BorderLayout.CENTER);
	}

	private void loadBooks() {

		Book volume = (Book) cbVolume.getSelectedItem();

		ArrayList<Book> bookList = (ArrayList<Book>) SqliteAdapter.selectBooks(volume);

		listModelBook.removeAllElements();
		listModelBook.addAll(bookList);
		jlistBook.setModel(listModelBook);

	}

	private void loadChiefs() {

		ArrayList<Book> bookList = (ArrayList<Book>) SqliteAdapter.selectAlbum();
		if (bookList == null) {
			return;
		}

		for (int i = 0; i < bookList.size(); i++) {
			if (bookList.get(i).getStatus() == 0) {
				continue;
			}
			cbBookName.addItem(bookList.get(i));
		}

	}

	private void loadVolumes() {

		Book chielf = (Book) cbBookName.getSelectedItem();

		ArrayList<Book> bookList = (ArrayList<Book>) SqliteAdapter.selectVolumes(chielf);
		if (bookList == null) {
			return;
		}

		cbVolume.removeAllItems();

		for (int i = 0; i < bookList.size(); i++) {
			cbVolume.addItem(bookList.get(i));
		}

	}

	@Override
	public double getDividerLocation() {
		return 0.68;
	}

}
