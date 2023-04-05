package com.free;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import com.free.bean.Book;
import com.free.bean.Paragraphs;
import com.free.common.Constants;
import com.free.common.UIUtility;
import com.free.comp.TxContentDialog;
import com.free.comp.TxListCellRenderer;
import com.free.comp.TxPersonFrame;
import com.free.dao.SqliteAdapter;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1499761171388441237L;
	Container contentPane;

	JList<Book> jlistBook = new JList<>();
	DefaultListModel<Book> listModelBook = new DefaultListModel<Book>();

	JList<Paragraphs> jlistContent = new JList<>();
	DefaultListModel<Paragraphs> listModelContent = new DefaultListModel<Paragraphs>();

	JPanel jpCenter = new JPanel();

	JPanel jpContent = new JPanel();
	JLabel lbBookName = new JLabel();

	JComboBox<Book> cbBookName = new JComboBox<Book>();
	JComboBox<Book> cbVolume = new JComboBox<Book>();

	int curMaxPid = 0;

	JDesktopPane desktopPane = new JDesktopPane();

	public MainFrame() {

		JMenuBar menuBar = new JMenuBar();
		JMenu mainMenu = new JMenu("主菜单");
		JMenuItem miBooks = new JMenuItem("人物列表");
		mainMenu.add(miBooks);
		menuBar.add(mainMenu);

		miBooks.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TxPersonFrame vv = new TxPersonFrame();

				vv.setExtendedState(JFrame.MAXIMIZED_BOTH);

				vv.setVisible(true);

			}
		});

		setContentPane(desktopPane);

//		TxPersonFrame internalFrame = new TxPersonFrame();
//		internalFrame.setVisible(true);
////		internalFrame.setMaximum(true);
//		internalFrame.setMaximizable(true);
//		desktopPane.add(internalFrame);

		this.setJMenuBar(menuBar);

		Border border = BorderFactory.createEmptyBorder(0, 12, 10, 12);

		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		contentPane.add(mainPanel, BorderLayout.CENTER);

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

		jpLeft.setBorder(border);

		UIUtility.setFont16(jpLeft);

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
		jpContent.add(sjpContents, BorderLayout.CENTER);
		jlistContent.setFont(UIUtility.TEXT_FONT);
		jlistContent.setCellRenderer(new TxListCellRenderer());
		jlistContent.setFixedCellWidth(20);

		jpContent.setBackground(UIUtility.TEXT_BG);
		sjpContents.setBackground(UIUtility.TEXT_BG);

		jlistContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int clickTis = e.getClickCount();
				if (clickTis < 2) {
					return;
				}

				showParagraphDialog1();

			}

			private void showParagraphDialog1() {

				Paragraphs para = jlistContent.getSelectedValue();

				if (para == null) {
					return;
				}

				TxContentDialog t = TxContentDialog.getOneInstance(para);
				t.setVisible(true);

				Enumeration<Paragraphs> paraList = (Enumeration<Paragraphs>) listModelContent.elements();

				ArrayList<Paragraphs> pList = new ArrayList<Paragraphs>();
				while (paraList.hasMoreElements()) {
					pList.add(paraList.nextElement());
				}

				t.setParagraphList(pList);
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
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(false);
		this.setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);// 别忘关闭窗口
	}

	protected void loadBookContents(Book selectBook) {

		ArrayList<Paragraphs> paraList = (ArrayList<Paragraphs>) SqliteAdapter.selectBookContents(selectBook);
		if (paraList == null) {
			return;
		}
		listModelContent.removeAllElements();
		listModelContent.addAll(paraList);
		jlistContent.setModel(listModelContent);

	}

	public void loadBooks() {

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

	public static void main(String[] args) {

		MainFrame m = new MainFrame();

		m.loadChiefs();
	}

}
