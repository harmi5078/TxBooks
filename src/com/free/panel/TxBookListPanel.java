package com.free.panel;

import java.awt.BorderLayout;
import java.awt.Toolkit;
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
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.free.bean.Book;
import com.free.common.Constants;
import com.free.common.UIUtility;
import com.free.dao.SqliteAdapter;

public class TxBookListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTabbedPane tabPanel = new JTabbedPane();

	JList<Book> jlistBook = new JList<>();
	DefaultListModel<Book> listModelBook = new DefaultListModel<Book>();

	public TxBookListPanel() {
		init();
		initAction();
	}

	private void init() {

		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		int screenWidth = kit.getScreenSize().width; // 获取屏幕的宽

		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		add(mainPanel, BorderLayout.CENTER);

		JPanel jpLeft = initLeftBookTree();
		JPanel jpRightTabPane = initRightTab();

		JSplitPane jSplitPane = new JSplitPane();
		jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		jSplitPane.setLeftComponent(jpLeft);
		jSplitPane.setRightComponent(jpRightTabPane);
		jSplitPane.setDividerSize(1);// 设置分割线的宽度
		jSplitPane.setDividerLocation(((int) (screenWidth * 0.18)));// 设定分割线的距离左边的位置

		mainPanel.add(jSplitPane, BorderLayout.CENTER);
		UIUtility.setFont16(mainPanel);
	}

	private JPanel initRightTab() {
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(tabPanel, BorderLayout.CENTER);
		return rightPanel;
	}

	JComboBox<Book> cbBookName = new JComboBox<Book>();
	JComboBox<Book> cbVolume = new JComboBox<Book>();

	private JPanel initLeftBookTree() {

		JPanel jpLeft = new JPanel();
		jpLeft.setLayout(new BorderLayout());

		// 书单
		JScrollPane sjpBooks = new JScrollPane(jlistBook);
		jpLeft.add(sjpBooks, BorderLayout.CENTER);

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

		jpLeft.add(jpFilterBook, BorderLayout.NORTH);

		jpLeft.setBorder(BorderFactory.createEmptyBorder(0, 12, 10, 12));

		return jpLeft;

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

		jlistBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

				String title = book.getName();

				int indexOf = tabPanel.indexOfTab(title);
				if (indexOf >= 0) {
					TxBookContentsPanel bookContentPanel = (TxBookContentsPanel) tabPanel.getComponentAt(indexOf);

					tabPanel.setSelectedComponent(bookContentPanel);
					bookContentPanel.reLoadBookContents();
				} else {
					TxBookContentsPanel bookContentPanel = new TxBookContentsPanel(book);
					tabPanel.add(bookContentPanel, title);
					bookContentPanel.loadBookContents();
					tabPanel.setSelectedComponent(bookContentPanel);
				}

			}
		});

		tabPanel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				TxBookContentsPanel bookContentPanel = (TxBookContentsPanel) tabPanel.getSelectedComponent();
				bookContentPanel.reLoadBookContents();
			}
		});
	}

	private void loadBooks() {

		Book volume = (Book) cbVolume.getSelectedItem();

		ArrayList<Book> bookList = (ArrayList<Book>) SqliteAdapter.selectBooks(volume);

		listModelBook.removeAllElements();
		listModelBook.addAll(bookList);
		jlistBook.setModel(listModelBook);

	}

	public void loadChiefs() {

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

	public void loadVolumes() {

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
}
