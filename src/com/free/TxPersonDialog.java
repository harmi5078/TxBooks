package com.free;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.free.bean.Dynasty;
import com.free.bean.Nation;
import com.free.bean.Person;
import com.free.comp.TxTextField;
import com.free.dao.SqliteAdapter;

public class TxPersonDialog extends JDialog {

	private static TxPersonDialog oneDialog = null;

	public static TxPersonDialog getOneInstance() {
		if (oneDialog == null) {
			oneDialog = new TxPersonDialog();
		}

		return oneDialog;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container contentPane;

	JTextField tfName = new TxTextField();

	JTextField tfAddr = new TxTextField();
	JTextField tfTitle = new TxTextField();
	JComboBox<Dynasty> cbDynasty = new JComboBox<Dynasty>();

	JComboBox<Nation> cbNation = new JComboBox<Nation>();

	JTextArea tpDetail = new JTextArea(10, 20);

	private Person person = null;

	private TxPersonDialog() {

		init();
		loadDynasty();
		loadNation();
	}

	private void loadDynasty() {

		ArrayList<Dynasty> dList = (ArrayList<Dynasty>) SqliteAdapter.selectDynasty();
		if (dList == null) {
			return;
		}

		cbDynasty.removeAllItems();

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

		for (int i = 0; i < dList.size(); i++) {
			cbNation.addItem(dList.get(i));
		}

	}

	private void init() {

		this.setTitle("新增人物");

		Font font = new Font("黑体", 0, 18);

		cbDynasty.setFont(font);
		cbNation.setFont(font);

		JPanel jpMain = new JPanel();

		Border border = BorderFactory.createEmptyBorder(0, 12, 10, 12);

		jpMain.setLayout(new BorderLayout());

		JPanel jpName = new JPanel();
		jpName.setLayout(new BorderLayout());

		JPanel jpName1 = new JPanel();
		jpName1.setLayout(new BorderLayout());
		JLabel labName = new JLabel("姓名:");
		labName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		labName.setFont(font);
		tfName.setPreferredSize(new Dimension(200, 30));
		jpName1.add(labName, BorderLayout.WEST);
		jpName1.add(tfName, BorderLayout.CENTER);
		jpName1.setBorder(BorderFactory.createEmptyBorder(14, 12, 10, 12));
		jpName.add(jpName1, BorderLayout.NORTH);

		JPanel jpNation = new JPanel();
		jpNation.setLayout(new BorderLayout());
		JPanel jpNation1 = new JPanel();
		jpNation1.setLayout(new BorderLayout());
		JLabel labNation = new JLabel("国家:");
		labNation.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		labNation.setFont(font);
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
		labAddr.setFont(font);
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
		labTitle.setFont(font);
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
		labDynasty.setFont(font);
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
		labDetail.setFont(font);
		jpLabDetail.add(labDetail, BorderLayout.NORTH);
		jpDetail1.add(jpLabDetail, BorderLayout.WEST);
		jpDetail1.add(tpDetail, BorderLayout.CENTER);
		jpDetail1.setBorder(border);
		jpDetail.add(jpDetail1, BorderLayout.NORTH);

		tpDetail.setLineWrap(true);
		tpDetail.setWrapStyleWord(true);
		tpDetail.setFont(new Font("黑体", 0, 18));
		tpDetail.setBorder(BorderFactory.createEtchedBorder());

		// 提交保存
		JPanel jpButton = new JPanel();
		jpButton.setLayout(new BorderLayout());
		JPanel jpBt2 = new JPanel();
		jpBt2.setLayout(new BorderLayout());
		JButton jbSave = new JButton("保存");

		JButton jbCancel = new JButton("取消");
		JPanel jpBtCancel = new JPanel();
		jpBtCancel.setLayout(new BorderLayout());
		jpBtCancel.add(jbCancel, BorderLayout.EAST);
		jpBtCancel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

		JPanel jpBtSave = new JPanel();
		jpBtSave.setLayout(new BorderLayout());
		jpBtSave.add(jbSave, BorderLayout.EAST);

		jpBt2.add(jpBtCancel, BorderLayout.WEST);
		jpBt2.add(jpBtSave, BorderLayout.EAST);
		jpBt2.setBorder(border);
		jpButton.add(jpBt2, BorderLayout.EAST);

		jpName.add(jpNation, BorderLayout.CENTER);
		jpNation.add(jpAddr, BorderLayout.CENTER);
		jpAddr.add(jpTitle, BorderLayout.CENTER);
		jpTitle.add(jpDynasty, BorderLayout.CENTER);
		jpDynasty.add(jpDetail, BorderLayout.CENTER);
		jpDetail.add(jpButton, BorderLayout.CENTER);

		jpMain.add(jpName, BorderLayout.NORTH);

		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(jpMain, BorderLayout.CENTER);

		setSize(490, 530);

		this.setResizable(false);

		int windowWidth = this.getWidth(); // 获得窗口宽

		int windowHeight = this.getHeight(); // 获得窗口高

		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包

		int screenWidth = kit.getScreenSize().width; // 获取屏幕的宽

		int screenHeight = kit.getScreenSize().height; // 获取屏幕的高

		this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		jbSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}

		});

	}

	public void setPerson(Person p) {
		this.person = p;

		if (person == null) {

			this.setTitle("新增");

			tfName.setText("");

			tfAddr.setText("");
			tfTitle.setText("");
			tpDetail.setText("");
			return;
		}

		this.setTitle(person.getName());

		tfName.setText(person.getName());

		tfAddr.setText(person.getAddr());
		tfTitle.setText(person.getTitle());
		tpDetail.setText(person.getDetail());

		Dynasty dynasty = new Dynasty();
		dynasty.setId(person.getDynasty());
		dynasty.setName(person.getDynastyName());
		cbDynasty.setSelectedItem(dynasty);

		Nation nation = new Nation();
		nation.setId(person.getNation());
		cbNation.setSelectedItem(nation);

	}

	private void save() {

		String name = tfName.getText();

		if (name == null || name.trim().length() == 0) {
			return;
		}

		String title = tfTitle.getText();

		if (title == null || title.trim().length() == 0) {
			return;
		}

		String addr = tfAddr.getText();
		if (addr == null || addr.trim().length() == 0) {
			return;
		}

		String detail = tpDetail.getText();
		if (detail == null || detail.trim().length() == 0) {
			return;
		}

		Dynasty dy = (Dynasty) cbDynasty.getSelectedItem();
		if (dy == null) {

			return;
		}

		Nation nation = (Nation) cbNation.getSelectedItem();
		if (nation == null) {
			return;
		}

		Person p = new Person();
		p.setName(name);
		p.setAddr(addr);
		p.setDetail(detail);

		p.setTitle(title);
		p.setDynasty(dy.getId());
		p.setNation(nation.getId());

		if (person != null) {
			savePerson(p);
		} else {
			SqliteAdapter.addPerson(p);
		}

		setVisible(false);
	}

	private void savePerson(Person p) {
		person.setAddr(p.getAddr());
		person.setName(p.getName());
		person.setNation(p.getNation());
		person.setDetail(p.getDetail());
		person.setTitle(p.getTitle());
		person.setDynasty(p.getDynasty());

		SqliteAdapter.savePerson(person);
	}

}
