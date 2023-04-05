package com.free.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import com.free.MainFrame;
import com.free.TxParagraphDialog;
import com.free.bean.Book;
import com.free.bean.Paragraphs;
import com.free.common.Constants;
import com.free.common.UIUtility;
import com.free.dao.SqliteAdapter;

public class TxBookFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2072985518757181121L;

//	Container contentPane;

	JList<Book> jlistBook = new JList<>();
	DefaultListModel<Book> listModelBook = new DefaultListModel<Book>();

	JList<Paragraphs> jlistContent = new JList<>();
	DefaultListModel<Paragraphs> listModelContent = new DefaultListModel<Paragraphs>();

	JPanel jpCenter = new JPanel();

	JPanel jpContent = new JPanel();
	JLabel lbBookName = new JLabel();

	JComboBox<String> cbBookName = new JComboBox<String>();
	JComboBox<String> cbVolume = new JComboBox<String>();

	int curMaxPid = 0;

	public TxBookFrame() {
		JMenuBar menuBar = new JMenuBar();
		JMenu mainMenu = new JMenu("主菜单");
		JMenuItem miBooks = new JMenuItem("书籍");
		mainMenu.add(miBooks);
		menuBar.add(mainMenu);

		this.setJMenuBar(menuBar);

		Border border = BorderFactory.createEmptyBorder(0, 12, 10, 12);

//		contentPane = this.getContentPane();
//		contentPane.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

//		contentPane.add(mainPanel, BorderLayout.CENTER);

		JPanel jpLeft = new JPanel();
		jpCenter.setLayout(new BorderLayout());
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
		labName.setFont(UIUtility.TEXT_FONT);
//		tfName.setPreferredSize(new Dimension(200, 30));
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
		labNation.setFont(UIUtility.TEXT_FONT);
		jpVolumn1.add(labNation, BorderLayout.WEST);
		jpVolumn1.add(cbVolume, BorderLayout.CENTER);

		jpVolumn.add(jpVolumn1, BorderLayout.NORTH);
		jpVolumn.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		jpBName.add(jpVolumn, BorderLayout.CENTER);
		jpFilterBook.add(jpBName, BorderLayout.CENTER);

		jpLeft.add(jpFilterBook, BorderLayout.NORTH);

		jpLeft.setBorder(border);

		cbBookName.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					loadVolumns();
				}
			}
		});

		cbVolume.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
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

				if (Constants.curBook == null) {
					Constants.curBook = book;
					loadBookContents(Constants.curBook);
					Constants.CurMaxContentId = 0;
					lbBookName.setText(book.toString());
					return;
				}

				if (Constants.curBook.equals(book)) {
					return;
				} else {
					Constants.curBook = book;
					loadBookContents(Constants.curBook);
					Constants.CurMaxContentId = 0;
					lbBookName.setText(book.toString());
				}

			}
		});

		// 翻页控制
		JPanel jpNextLast = new JPanel();

		lbBookName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		lbBookName.setFont(UIUtility.TEXT_FONT);
		jpNextLast.setLayout(new BorderLayout());
		jpNextLast.add(lbBookName, BorderLayout.WEST);

		// 文本区域
		jpContent.setLayout(new BorderLayout());

		jpCenter.add(jpNextLast, BorderLayout.NORTH);
		jpCenter.add(jpContent, BorderLayout.CENTER);

		JScrollPane sjpContents = new JScrollPane(jlistContent);
		jpContent.add(sjpContents);
		jlistContent.setFont(UIUtility.TEXT_FONT);
		jlistContent.setCellRenderer(new TxListCellRenderer());
		jlistContent.setFixedCellWidth(20);

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

				TxParagraphDialog jd = TxParagraphDialog.getOneInstance(para);

				jd.setVisible(true);

			}
		});

		jlistBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel jpBottm = new JPanel();
		jpBottm.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		jpCenter.add(jpBottm, BorderLayout.SOUTH);
		mainPanel.add(jpCenter, BorderLayout.CENTER);
		mainPanel.add(jpLeft, BorderLayout.WEST);

		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = new Rectangle(scrSize);
		this.setBounds(bounds);

		this.setVisible(true);

		setContentPane(mainPanel);

		setDefaultCloseOperation(EXIT_ON_CLOSE);// 别忘关闭窗口
	}

	protected void loadBookContents(Book selectBook) {
		new Thread() {
			public void run() {

				ArrayList<Paragraphs> paraList = (ArrayList<Paragraphs>) SqliteAdapter.selectBookContents(selectBook);
				if (paraList == null) {
					return;
				}
				listModelContent.removeAllElements();
				listModelContent.addAll(paraList);
				jlistContent.setModel(listModelContent);

			}
		}.start();

	}

	public void loadBooks() {
		new Thread() {
			public void run() {

				Book book = new Book();
				String chielfName = (String) cbBookName.getSelectedItem();
				String volume = (String) cbVolume.getSelectedItem();

				book.setChiefAlbum(chielfName);
				book.setVolume(volume);

				ArrayList<Book> bookList = (ArrayList<Book>) SqliteAdapter.selectBooks(book);

				listModelBook.removeAllElements();
				listModelBook.addAll(bookList);
				jlistBook.setModel(listModelBook);
			}
		}.start();
	}

	public void loadChiefs() {
		new Thread() {
			public void run() {
				ArrayList<Book> bookList = (ArrayList<Book>) SqliteAdapter.selectAlbum();
				if (bookList == null) {
					return;
				}

				for (int i = 0; i < bookList.size(); i++) {
					cbBookName.addItem(bookList.get(i).getName());
				}

			}
		}.start();
	}

	public void loadVolumns() {
		new Thread() {
			public void run() {

				Book chielf = (Book) cbBookName.getSelectedItem();

				ArrayList<Book> bookList = (ArrayList<Book>) SqliteAdapter.selectVolumes(chielf);
				if (bookList == null) {
					return;
				}

				cbVolume.removeAllItems();

				for (int i = 0; i < bookList.size(); i++) {
					cbVolume.addItem(bookList.get(i).getName());
				}

			}
		}.start();
	}

	public static void main(String[] args) {

		MainFrame m = new MainFrame();

		m.loadChiefs();
	}

}
