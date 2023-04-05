package com.free;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.free.bean.Paragraphs;
import com.free.bean.Person;
import com.free.common.Constants;
import com.free.common.UIUtility;
import com.free.dao.SqliteAdapter;
import com.free.msg.TxMessage;
import com.free.msg.TxMessageAdapter;
import com.free.msg.TxMessageListener;

public class TxParagraphDialog extends JDialog implements TxMessageListener {

	private static TxParagraphDialog oneDialog = null;

	private int TEXT_FLAG = 1;

	public static TxParagraphDialog getOneInstance(Paragraphs paragraphs) {
		if (oneDialog == null) {
			oneDialog = new TxParagraphDialog(paragraphs);
		} else {
			oneDialog.setParagraphs(paragraphs);
		}

		oneDialog.setTitle(Constants.curBook.toString());

		return oneDialog;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Paragraphs paragraphs = null;
	Container contentPane;
	JTextArea ta = new JTextArea();
	JTextArea taRel = new JTextArea(10, 40);

	JComboBox<Person> cbPerson = new JComboBox<Person>();

	private TxParagraphDialog(Paragraphs paragraphs) {
		this.paragraphs = paragraphs;
		init();
		loadPerson();
		loadPersonRelation();
		TxMessageAdapter.addMessageListener(TxMessageAdapter.MSTYPE_ADDPERSON, this);
		TxMessageAdapter.addMessageListener(TxMessageAdapter.MSTYPE_ADDPERSONRELATION, this);
	}

	public void setParagraphs(Paragraphs paragraphs) {
		this.paragraphs = paragraphs;
		TEXT_FLAG = 1;
		ta.setText(paragraphs.toString());
		loadPersonRelation();
	}

	private void init() {

		cbPerson.setFont(UIUtility.TEXT_FONT);

		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setFont(UIUtility.TEXT_FONT);
		ta.setBackground(UIUtility.TEXT_BG);
		ta.setEditable(false);
		ta.setText(paragraphs.toString());

		JPanel compPanel = new JPanel();
		compPanel.setLayout(new BorderLayout());

		JPanel taPanel = new JPanel();
		taPanel.setLayout(new BorderLayout());

		JPanel taPanel1 = new JPanel();
		taPanel1.setLayout(new BorderLayout());
		taPanel1.add(ta, BorderLayout.CENTER);
		taPanel1.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
		taPanel1.setBackground(UIUtility.TEXT_BG);

		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() < 2) {
					return;
				}

				TEXT_FLAG = TEXT_FLAG + 1;
				if (e.getButton() == MouseEvent.BUTTON1) {

					if (TEXT_FLAG % 2 == 1) {
						ta.setText(paragraphs.toString());
					} else {
						ta.setText(paragraphs.getTranscation());
					}
				}

			}
		};
		taPanel1.addMouseListener(ma);
		ta.addMouseListener(ma);

		taPanel.add(taPanel1, BorderLayout.CENTER);

		compPanel.add(taPanel, BorderLayout.NORTH);

		JButton btAddRe = new JButton("关联对象");
		JButton btAdd = new JButton("新增对象");

		btAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				TxPersonDialog dj = TxPersonDialog.getOneInstance();
				dj.setPerson(null);
				dj.setVisible(true);

			}
		});

		btAddRe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addPersonRelation();
			}
		});

		JLabel jLabelRel = new JLabel("关联对象：");
		jLabelRel.setFont(UIUtility.TEXT_FONT);
		JPanel persPanel = new JPanel();
		persPanel.setLayout(new BorderLayout());
		persPanel.add(jLabelRel, BorderLayout.WEST);
		persPanel.add(cbPerson, BorderLayout.CENTER);

		JPanel jpBtAdd = new JPanel();
		jpBtAdd.setLayout(new BorderLayout());

		JPanel jpBtAddRe = new JPanel();
		jpBtAddRe.setLayout(new BorderLayout());
		jpBtAddRe.add(btAddRe, BorderLayout.WEST);
		jpBtAddRe.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		jpBtAdd.add(jpBtAddRe, BorderLayout.WEST);
		jpBtAdd.add(btAdd, BorderLayout.EAST);

		persPanel.add(jpBtAdd, BorderLayout.EAST);
		persPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

		taPanel.add(persPanel, BorderLayout.SOUTH);

		// taRel
		JPanel jpRelation = new JPanel();
		jpRelation.setLayout(new BorderLayout());

		JPanel jpRelation1 = new JPanel();
		jpRelation1.setLayout(new BorderLayout());
		jpRelation1.add(taRel, BorderLayout.CENTER);
		jpRelation1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		jpRelation.add(compPanel, BorderLayout.NORTH);
		jpRelation.add(jpRelation1, BorderLayout.CENTER);
		taRel.setLineWrap(true);
		taRel.setWrapStyleWord(true);
		taRel.setFont(UIUtility.TEXT_FONT);

		taRel.setBackground(mainPanel.getBackground());
		taRel.setEditable(false);

		mainPanel.add(jpRelation, BorderLayout.CENTER);

		contentPane.add(mainPanel, BorderLayout.CENTER);

		mainPanel.setBorder(BorderFactory.createEtchedBorder());

		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		int screenWidth = kit.getScreenSize().width; // 获取屏幕的宽
		int screenHeight = kit.getScreenSize().height; // 获取屏幕的高

		double rate1 = 0.86;
		setSize((int) (screenWidth * rate1), (int) (screenHeight * rate1));
		int windowWidth = this.getWidth(); // 获得窗口宽
		int windowHeight = this.getHeight(); // 获得窗口高

		this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示
	}

	protected void addPersonRelation() {
		Object p = cbPerson.getSelectedItem();
		if (p == null) {
			return;
		}

		if (paragraphs == null) {
			return;
		}

		Person person = (Person) p;

		SqliteAdapter.addRelation(paragraphs.getGid(), person.getPid());

	}

	private void loadPerson() {
		new Thread() {
			public void run() {

				ArrayList<Person> dList = (ArrayList<Person>) SqliteAdapter.selectPersons();
				if (dList == null) {
					return;
				}

				cbPerson.removeAllItems();

				for (int i = 0; i < dList.size(); i++) {
					cbPerson.addItem(dList.get(i));
				}

			}
		}.start();

	}

	private void loadPersonRelation() {
		new Thread() {
			public void run() {

				ArrayList<Person> dList = (ArrayList<Person>) SqliteAdapter
						.selectPersonWithParagraphs(paragraphs.getGid());
				if (dList == null) {
					return;
				}

				taRel.setText("");

				for (int i = 0; i < dList.size(); i++) {
					taRel.append(dList.get(i).toString());
					taRel.append("\n");
				}
			}
		}.start();

	}

	@Override
	public void notiyMessage(String messageType, TxMessage message) {
		if (TxMessageAdapter.MSTYPE_ADDPERSON.equals(messageType)) {
			loadPerson();
		} else if (TxMessageAdapter.MSTYPE_ADDPERSONRELATION.equals(messageType)) {
			this.loadPersonRelation();
		}
	}

}
