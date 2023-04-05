package com.free.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.free.bean.Dynasty;
import com.free.bean.Events;
import com.free.bean.Nation;
import com.free.bean.Paragraphs;
import com.free.bean.Person;
import com.free.common.Constants;
import com.free.common.TxStringUtil;
import com.free.common.UIUtility;
import com.free.dao.SqliteAdapter;

public class TxContentDialog extends JDialog {

	private static TxContentDialog oneDialog = null;

	public static TxContentDialog getOneInstance(Paragraphs paragraphs) {

		if (oneDialog == null) {
			oneDialog = new TxContentDialog(paragraphs);
		}

		oneDialog.setParagraphs(paragraphs);
		oneDialog.loadDynasty();
		oneDialog.setTitle(Constants.curBook.toString());

		return oneDialog;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Paragraphs> paraList = new ArrayList<Paragraphs>();
	private Paragraphs paragraphs = null;
	private int TEXT_FLAG = 1;
	private Nation allNation = new Nation();
	private Dynasty allDynasty = new Dynasty();

	JPanel taPanel = new JPanel();
	JTextArea ta = new JTextArea();
	JLabel labPages = new JLabel("当前页");

	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();

	// 人物控件
	JTextField tfName = new TxTextField();

	JTextField tfAddr = new TxTextField();
	JTextField tfTitle = new TxTextField();
	JComboBox<Dynasty> cbDynasty = new JComboBox<Dynasty>();

	JComboBox<Nation> cbNation = new JComboBox<Nation>();
	JComboBox<Person> cbPerson = new JComboBox<Person>();

	JCheckBox ccbSave = new JCheckBox("编辑");

	JTextArea tpDetail = new JTextArea(5, 20);

	JButton jbSave = new JButton("保存新增");
	JButton jbAddRel = new JButton("增加关联");

	JList<Person> jlistPerson = new JList<>();
	DefaultListModel<Person> listModelPerson = new DefaultListModel<Person>();

	JButton jbNext = new JButton(">>");
	JButton jbUp = new JButton("<<");

	// 事件对象
	JTextField tfEventName = new TxTextField();
	JTextField tfEventKeys = new TxTextField();
	JTextArea tpEvtDetail = new JTextArea(5, 20);
	JComboBox<Person> cbEvent = new JComboBox<Person>();
	JButton jbEvSave = new JButton("保存新增");
	JButton jbEvAddRel = new JButton("增加关联");

	JList<Person> jlistEvent = new JList<>();
	DefaultListModel<Person> listModelEvent = new DefaultListModel<Person>();

	private TxContentDialog(Paragraphs paragraphs) {

		this.paragraphs = paragraphs;

		allNation.setId(0);
		allNation.setName("<所有>");

		allDynasty.setId(0);
		allDynasty.setName("<所有>");

		init();
		initAction();

		loadDynasty();
		loadNation();
		loadPerson();
		loadPersonRelation();

	}

	private void init() {

		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		int screenWidth = kit.getScreenSize().width; // 获取屏幕的宽
		int screenHeight = kit.getScreenSize().height; // 获取屏幕的高

		double rate1 = 0.90;
		int thisWidth = (int) (screenWidth * rate1);
		setSize(thisWidth, (int) (screenHeight * rate1));
		int windowWidth = this.getWidth(); // 获得窗口宽
		int windowHeight = this.getHeight(); // 获得窗口高

		this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		contentPane.add(mainPanel, BorderLayout.CENTER);

		JSplitPane jSplitPane = new JSplitPane();
		jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		jSplitPane.setLeftComponent(leftPanel);
		jSplitPane.setRightComponent(rightPanel);
		jSplitPane.setDividerSize(1);// 设置分割线的宽度
		jSplitPane.setDividerLocation(((int) (thisWidth * 0.62)));// 设定分割线的距离左边的位置

		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setFont(UIUtility.TEXT_FONT);
		ta.setBackground(UIUtility.TEXT_BG);
		ta.setEditable(false);
		ta.setForeground(Color.BLACK);
		ta.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

		taPanel.setLayout(new BorderLayout());
		taPanel.add(ta, BorderLayout.CENTER);
		taPanel.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));
		taPanel.setBackground(UIUtility.TEXT_BG);

		JScrollPane sjpContents = new JScrollPane(taPanel);

		JPanel jpPage = new JPanel();
		jpPage.setLayout(new BorderLayout());

		JPanel jpPage1 = new JPanel();
		jpPage1.setLayout(new BorderLayout());

		JPanel jpPage2 = new JPanel();
		jpPage2.setLayout(new BorderLayout());
		jpPage2.add(jbNext, BorderLayout.EAST);

		jpPage1.add(jpPage2, BorderLayout.EAST);

		jpPage1.add(jbUp, BorderLayout.WEST);
		jpPage1.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
		jpPage2.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
		jpPage.add(jpPage1, BorderLayout.EAST);
		jpPage.add(labPages, BorderLayout.WEST);

		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(sjpContents, BorderLayout.CENTER);
		leftPanel.add(jpPage, BorderLayout.SOUTH);
		leftPanel.setBorder(BorderFactory.createEmptyBorder(14, 12, 4, 12));
		initRight();

		UIUtility.setFont16(rightPanel);

		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		mainPanel.add(jSplitPane, BorderLayout.CENTER);

	}

	private void initRight() {

		JTabbedPane tabPanel = new JTabbedPane();

		tabPanel.addTab("人物", createPersonPanel());

		tabPanel.addTab("事件", createEventPanel());

		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(tabPanel, BorderLayout.CENTER);

	}

	private JPanel createPersonPanel() {

		JPanel jpMain = new JPanel();

		Border border = BorderFactory.createEmptyBorder(0, 12, 3, 12);

		jpMain.setLayout(new BorderLayout());

		JPanel jpName = new JPanel();
		jpName.setLayout(new BorderLayout());

		JPanel jpName1 = new JPanel();
		jpName1.setLayout(new BorderLayout());
		JLabel labName = new JLabel("姓名:");
		labName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpName1.add(labName, BorderLayout.WEST);
		jpName1.add(tfName, BorderLayout.CENTER);
		jpName1.setBorder(BorderFactory.createEmptyBorder(8, 12, 3, 12));
		jpName.add(jpName1, BorderLayout.NORTH);

		JPanel jpNation = new JPanel();
		jpNation.setLayout(new BorderLayout());
		JPanel jpNation1 = new JPanel();
		jpNation1.setLayout(new BorderLayout());
		JLabel labNation = new JLabel("国家:");
		labNation.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpNation1.add(labNation, BorderLayout.WEST);
		jpNation1.add(cbNation, BorderLayout.CENTER);
		jpNation1.setBorder(border);
		jpNation.add(jpNation1, BorderLayout.NORTH);

		JPanel jpAddr = new JPanel();
		jpAddr.setLayout(new BorderLayout());
		JPanel jpAddr1 = new JPanel();
		jpAddr1.setLayout(new BorderLayout());
		JLabel labAddr = new JLabel("地址:");
		labAddr.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpAddr1.add(labAddr, BorderLayout.WEST);
		jpAddr1.add(tfAddr, BorderLayout.CENTER);
		jpAddr1.setBorder(border);
		jpAddr.add(jpAddr1, BorderLayout.NORTH);

		JPanel jpTitle = new JPanel();
		jpTitle.setLayout(new BorderLayout());
		JPanel jpTitle1 = new JPanel();
		jpTitle1.setLayout(new BorderLayout());
		JLabel labTitle = new JLabel("字号:");
		labTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpTitle1.add(labTitle, BorderLayout.WEST);
		jpTitle1.add(tfTitle, BorderLayout.CENTER);
		jpTitle1.setBorder(border);
		jpTitle.add(jpTitle1, BorderLayout.NORTH);

		// cbDynasty
		JPanel jpDynasty = new JPanel();
		jpDynasty.setLayout(new BorderLayout());
		JPanel jpDynasty1 = new JPanel();
		jpDynasty1.setLayout(new BorderLayout());
		JLabel labDynasty = new JLabel("朝代:");
		labDynasty.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpDynasty1.add(labDynasty, BorderLayout.WEST);
		jpDynasty1.add(cbDynasty, BorderLayout.CENTER);
		jpDynasty1.setBorder(border);
		jpDynasty.add(jpDynasty1, BorderLayout.NORTH);

		JPanel jpDetail = new JPanel();
		jpDetail.setLayout(new BorderLayout());
		JPanel jpDetail1 = new JPanel();
		jpDetail1.setLayout(new BorderLayout());

		JPanel jpLabDetail = new JPanel();
		jpLabDetail.setLayout(new BorderLayout());

		JLabel labDetail = new JLabel("简介:");
		labDetail.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		jpLabDetail.add(labDetail, BorderLayout.NORTH);
		jpDetail1.add(jpLabDetail, BorderLayout.WEST);
		jpDetail1.add(new JScrollPane(tpDetail), BorderLayout.CENTER);
		jpDetail1.setBorder(border);
		jpDetail.add(jpDetail1, BorderLayout.NORTH);

		tpDetail.setLineWrap(true);
		tpDetail.setWrapStyleWord(true);
		tpDetail.setBorder(BorderFactory.createEtchedBorder());

		// 提交保存 查询
		JPanel jpButton = new JPanel();
		jpButton.setLayout(new BorderLayout());

		JPanel jpBt1 = new JPanel();
		jpBt1.setLayout(new BorderLayout());

		JPanel jpBt2 = new JPanel();
		jpBt2.setLayout(new BorderLayout());

		JPanel jpBtAddRel = new JPanel();
		jpBtAddRel.setLayout(new BorderLayout());
		jpBtAddRel.add(jbAddRel, BorderLayout.EAST);

		JPanel jpBtSave = new JPanel();
		jpBtSave.setLayout(new BorderLayout());

		jpBtSave.add(ccbSave, BorderLayout.WEST);
		jpBtSave.add(jbSave, BorderLayout.EAST);

		JLabel labButton = new JLabel("操作:");
		labButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		labButton.setForeground(labButton.getBackground());

		jpBt2.add(jpBtSave, BorderLayout.EAST);
		jpBt2.add(jpBtAddRel, BorderLayout.WEST);
		jpBt2.setBorder(border);

		jpBt1.add(labButton, BorderLayout.WEST);
		jpBt1.add(jpBt2, BorderLayout.CENTER);
		jpBt1.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

		jpButton.add(jpBt1, BorderLayout.NORTH);

		// 人物列表
		JPanel jpPerson = new JPanel();
		jpPerson.setLayout(new BorderLayout());
		jpPerson.setBorder(new TxTopLineBorder());

		JPanel jpPerson1 = new JPanel();
		jpPerson1.setLayout(new BorderLayout());
		JLabel labPerson = new JLabel("人物:");
		labPerson.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		jpPerson1.add(labPerson, BorderLayout.WEST);
		jpPerson1.add(cbPerson, BorderLayout.CENTER);

		jpPerson1.setBorder(BorderFactory.createEmptyBorder(5, 12, 8, 12));

		JPanel jpBtRel = new JPanel();
		jpBtRel.setLayout(new BorderLayout());

		jpBtRel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

		JLabel labRel = new JLabel("关联:");
		labRel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		labRel.setForeground(labButton.getBackground());

		jpPerson.add(jpPerson1, BorderLayout.NORTH);

		// 关系表
		JPanel jpRel = new JPanel();
		jpRel.setLayout(new BorderLayout());
		JPanel jpRel1 = new JPanel();
		jpRel1.setLayout(new BorderLayout());

		jlistPerson.setCellRenderer(new TxPersonListCellRenderer());
		jlistPerson.setFixedCellWidth(20);

		JScrollPane sjpContents = new JScrollPane(jlistPerson);

		jpRel.add(sjpContents, BorderLayout.CENTER);

		jpName.add(jpNation, BorderLayout.CENTER);
		jpNation.add(jpAddr, BorderLayout.CENTER);
		jpAddr.add(jpTitle, BorderLayout.CENTER);
		jpTitle.add(jpDynasty, BorderLayout.CENTER);
		jpDynasty.add(jpDetail, BorderLayout.CENTER);
		jpDetail.add(jpPerson, BorderLayout.CENTER);
		jpPerson.add(jpButton, BorderLayout.CENTER);
		jpButton.add(jpRel, BorderLayout.CENTER);

		jpMain.add(jpName, BorderLayout.CENTER);

		return jpMain;
	}

	private JPanel createEventPanel() {
		JPanel mainPanel = new JPanel();

		Border border = BorderFactory.createEmptyBorder(0, 12, 3, 12);

		mainPanel.setLayout(new BorderLayout());

		JPanel jpName = new JPanel();
		jpName.setLayout(new BorderLayout());

		JPanel jpName1 = new JPanel();
		jpName1.setLayout(new BorderLayout());
		JLabel labName = new JLabel("事件名:");
		labName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpName1.add(labName, BorderLayout.WEST);
		jpName1.add(tfEventName, BorderLayout.CENTER);
		jpName1.setBorder(BorderFactory.createEmptyBorder(8, 12, 3, 12));
		jpName.add(jpName1, BorderLayout.NORTH);

		// 标签关键字
		JPanel jpFlag = new JPanel();
		jpFlag.setLayout(new BorderLayout());
		JPanel jpFlag1 = new JPanel();
		jpFlag1.setLayout(new BorderLayout());
		JLabel labFlag = new JLabel("关键字:");
		labFlag.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		jpFlag1.add(labFlag, BorderLayout.WEST);
		jpFlag1.add(tfEventKeys, BorderLayout.CENTER);
		jpFlag1.setBorder(border);
		jpFlag.add(jpFlag1, BorderLayout.NORTH);

		// 描述
		JPanel jpDetail = new JPanel();
		jpDetail.setLayout(new BorderLayout());
		JPanel jpDetail1 = new JPanel();
		jpDetail1.setLayout(new BorderLayout());

		JPanel jpLabDetail = new JPanel();
		jpLabDetail.setLayout(new BorderLayout());

		JLabel labDetail = new JLabel("简描述:");
		labDetail.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		jpLabDetail.add(labDetail, BorderLayout.NORTH);
		jpDetail1.add(jpLabDetail, BorderLayout.WEST);
		jpDetail1.add(new JScrollPane(tpEvtDetail), BorderLayout.CENTER);
		jpDetail1.setBorder(border);
		jpDetail.add(jpDetail1, BorderLayout.NORTH);

		tpEvtDetail.setLineWrap(true);
		tpEvtDetail.setWrapStyleWord(true);
		tpEvtDetail.setBorder(BorderFactory.createEtchedBorder());

		// 事件列表
		JPanel jpEvent = new JPanel();
		jpEvent.setLayout(new BorderLayout());
		jpEvent.setBorder(new TxTopLineBorder());

		JPanel jpEvent1 = new JPanel();
		jpEvent1.setLayout(new BorderLayout());
		JLabel labEvent = new JLabel("事件表:");
		labEvent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		jpEvent1.add(labEvent, BorderLayout.WEST);
		jpEvent1.add(cbEvent, BorderLayout.CENTER);

		jpEvent1.setBorder(BorderFactory.createEmptyBorder(5, 12, 8, 12));
		jpEvent.add(jpEvent1, BorderLayout.NORTH);

		// 操作按钮
		JPanel jpButton = new JPanel();
		jpButton.setLayout(new BorderLayout());

		JPanel jpBt1 = new JPanel();
		jpBt1.setLayout(new BorderLayout());

		JPanel jpBt2 = new JPanel();
		jpBt2.setLayout(new BorderLayout());

		JPanel jpBtAddRel = new JPanel();
		jpBtAddRel.setLayout(new BorderLayout());
		jpBtAddRel.add(jbEvAddRel, BorderLayout.EAST);

		JPanel jpBtSave = new JPanel();
		jpBtSave.setLayout(new BorderLayout());
		jpBtSave.add(jbEvSave, BorderLayout.WEST);

		JLabel labButton = new JLabel("操作区:");
		labButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

		labButton.setForeground(labButton.getBackground());

		jpBt2.add(jpBtSave, BorderLayout.EAST);
		jpBt2.add(jpBtAddRel, BorderLayout.WEST);
		jpBt2.setBorder(border);

		jpBt1.add(labButton, BorderLayout.WEST);
		jpBt1.add(jpBt2, BorderLayout.CENTER);
		jpBt1.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		jpButton.add(jpBt1, BorderLayout.NORTH);

		// 关系表
		JPanel jpRel = new JPanel();
		jpRel.setLayout(new BorderLayout());
		JPanel jpRel1 = new JPanel();
		jpRel1.setLayout(new BorderLayout());

		jlistEvent.setCellRenderer(new TxPersonListCellRenderer());
		jlistEvent.setFixedCellWidth(20);

		JScrollPane sjpContents = new JScrollPane(jlistEvent);
		jpRel.add(sjpContents, BorderLayout.CENTER);

		jpName.add(jpFlag, BorderLayout.CENTER);
		jpFlag.add(jpDetail, BorderLayout.CENTER);
		jpDetail.add(jpEvent, BorderLayout.CENTER);
		jpEvent.add(jpButton, BorderLayout.CENTER);
		jpButton.add(jpRel, BorderLayout.CENTER);

		mainPanel.add(jpName, BorderLayout.CENTER);

		return mainPanel;
	}

	private void initAction() {

		jbSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				savePerson();
			}

		});

		jbAddRel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addPersonRelation();
				loadPersonRelation();
			}

		});

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

		taPanel.addMouseListener(ma);
		ta.addMouseListener(ma);

		DocumentListener documentListener = new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				queryPerson();

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				queryPerson();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				queryPerson();
			}
		};

		tfName.getDocument().addDocumentListener(documentListener);
		tfAddr.getDocument().addDocumentListener(documentListener);
		tfTitle.getDocument().addDocumentListener(documentListener);
		tpDetail.getDocument().addDocumentListener(documentListener);

		ItemListener itl = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					queryPerson();
				}
			}
		};

		this.cbDynasty.addItemListener(itl);
		this.cbNation.addItemListener(itl);

		jbNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = paraList.indexOf(paragraphs);
				if (index < paraList.size() - 1) {
					paragraphs = paraList.get(index + 1);
					setParagraphs(paragraphs);
					loadPersonRelation();
					setPagesInfo();
				}
			}

		});

		jbUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = paraList.indexOf(paragraphs);
				if (index > 0) {
					paragraphs = paraList.get(index - 1);
					setParagraphs(paragraphs);
					loadPersonRelation();
					setPagesInfo();
				}
			}

		});

		ccbSave.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				setEditPerson();
			}

		});

		cbPerson.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
//					ccbSave.setSelected(false);
				}
			}
		});

		ta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String str = ta.getSelectedText();
				if (!TxStringUtil.isEmpty(str)) {
					tfName.setText(str);
				}
			}
		});

		jbEvSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveEvent();
			}

		});
	}

	private void setEditPerson() {

		if (ccbSave.isSelected()) {

			Object p = cbPerson.getSelectedItem();
			if (p == null) {
				return;
			}

			Person person = (Person) p;

			tfName.setText(person.getName());
			tfTitle.setText(person.getTitle());
			tfAddr.setText(person.getAddr());
			tpDetail.setText(person.getDetail());

			Dynasty dynasty = new Dynasty();
			dynasty.setId(person.getDynasty());
			dynasty.setName(person.getDynastyName());

			cbDynasty.setSelectedItem(dynasty);

			Nation nation = new Nation();
			nation.setId(person.getNation());
			nation.setName(person.getNationName());

			cbNation.setSelectedItem(nation);
		} else {
			tfName.setText("");
			tfTitle.setText("");
			tfAddr.setText("");
			tpDetail.setText("");
		}

	}

	protected void queryPerson() {

		Person p = new Person();

		String name = tfName.getText();

		if (!TxStringUtil.isEmpty(name)) {
			p.setName(name);
		}

		String title = tfTitle.getText();
		if (!TxStringUtil.isEmpty(name)) {
			p.setTitle(title);
		}

		String addr = tfAddr.getText();
		if (!TxStringUtil.isEmpty(name)) {
			p.setAddr(addr);
		}

		String detail = tpDetail.getText();
		if (!TxStringUtil.isEmpty(name)) {
			p.setDetail(detail);
		}

		Dynasty dy = (Dynasty) cbDynasty.getSelectedItem();
		if (dy != null) {
			p.setDynasty(dy.getId());
		}

		Nation nation = (Nation) cbNation.getSelectedItem();
		if (nation != null) {
			p.setNation(nation.getId());
		}

		ArrayList<Person> dList = (ArrayList<Person>) SqliteAdapter.selectPersons(p);
		if (dList == null) {
			return;
		}

		cbPerson.removeAllItems();

		for (int i = 0; i < dList.size(); i++) {
			cbPerson.addItem(dList.get(i));
		}

	}

	public void setParagraphs(Paragraphs p) {
		this.paragraphs = p;
		ta.setText(paragraphs.toString());
	}

	private void loadDynasty() {

		cbDynasty.removeAllItems();

		ArrayList<Dynasty> dList = (ArrayList<Dynasty>) SqliteAdapter.selectDynasty();
		if (dList == null) {
			return;
		}

		cbDynasty.addItem(allDynasty);

		for (int i = 0; i < dList.size(); i++) {
			cbDynasty.addItem(dList.get(i));
		}

	}

	private void loadNation() {

		ArrayList<Nation> dList = (ArrayList<Nation>) SqliteAdapter.selectNation();
		if (dList == null) {
			return;
		}

		cbNation.removeAllItems();

		cbNation.addItem(allNation);

		for (int i = 0; i < dList.size(); i++) {
			cbNation.addItem(dList.get(i));
		}

	}

	private void loadPerson() {

		ArrayList<Person> personList = (ArrayList<Person>) SqliteAdapter.selectPersons();
		if (personList == null) {
			return;
		}

		cbPerson.removeAllItems();

		for (int i = 0; i < personList.size(); i++) {
			cbPerson.addItem(personList.get(i));
		}

		listModelEvent.removeAllElements();
		listModelEvent.addAll(personList);
		jlistEvent.setModel(listModelEvent);

	}

	protected void saveEvent() {

		String name = tfEventName.getText();

		if (TxStringUtil.isEmpty(name)) {
			tfEventName.grabFocus();
			return;
		}

		String evtKeys = tfEventKeys.getText();

		if (TxStringUtil.isEmpty(evtKeys)) {
			tfEventKeys.grabFocus();
			return;
		}

		String detail = tpEvtDetail.getText();
		if (TxStringUtil.isEmpty(detail)) {
			tpEvtDetail.grabFocus();
			return;
		}

		Events event = new Events();
		event.setName(name);
		event.setKeys(evtKeys);
		event.setDetails(detail);

		SqliteAdapter.addEvent(event);
	}

	private void savePerson() {

		String name = tfName.getText();

		if (name == null || name.trim().length() == 0) {
			tfName.grabFocus();
			return;
		}

		String title = tfTitle.getText();

		if (title == null || title.trim().length() == 0) {
			tfTitle.grabFocus();
			return;
		}

		String addr = tfAddr.getText();
		if (addr == null || addr.trim().length() == 0) {
			tfAddr.grabFocus();
			return;
		}

		String detail = tpDetail.getText();
		if (detail == null || detail.trim().length() == 0) {
			tpDetail.grabFocus();
			return;
		}

		Dynasty dy = (Dynasty) cbDynasty.getSelectedItem();
		if (dy == null) {
			cbDynasty.grabFocus();
			return;
		}

		Nation nation = (Nation) cbNation.getSelectedItem();
		if (nation == null) {
			cbNation.grabFocus();
			return;
		}

		Person p = new Person();
		p.setName(name);
		p.setAddr(addr);
		p.setDetail(detail);

		p.setTitle(title);
		p.setDynasty(dy.getId());
		p.setNation(nation.getId());

		if (ccbSave.isSelected()) {

			Person person = (Person) cbPerson.getSelectedItem();
			person.setAddr(p.getAddr());
			person.setName(p.getName());
			person.setNation(p.getNation());
			person.setDetail(p.getDetail());
			person.setTitle(p.getTitle());
			person.setDynasty(p.getDynasty());

			SqliteAdapter.savePerson(person);
		} else {

			SqliteAdapter.addPerson(p);

			tfTitle.setText("");
			tfAddr.setText("");
			tpDetail.setText("");

			cbDynasty.setSelectedItem(allDynasty);
			cbNation.setSelectedItem(allNation);
		}

		ccbSave.setSelected(false);

		queryPerson();

	}

	private void addPersonRelation() {
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

	private void loadPersonRelation() {

		if (paragraphs == null) {
			return;
		}

		ArrayList<Person> personList = (ArrayList<Person>) SqliteAdapter
				.selectPersonWithParagraphs(paragraphs.getGid());
		if (personList == null) {
			return;
		}

		listModelPerson.removeAllElements();
		listModelPerson.addAll(personList);
		jlistPerson.setModel(listModelPerson);

	}

	public void setParagraphList(ArrayList<Paragraphs> paraList2) {
		paraList.clear();
		paraList.addAll(paraList2);
		setPagesInfo();
	}

	private void setPagesInfo() {
		int index = paraList.indexOf(paragraphs);
		if (index < paraList.size() && index >= 0) {
			index = index + 1;
			labPages.setText("第" + index + "段/共" + paraList.size() + "段");
		}
	}

}
