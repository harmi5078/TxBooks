package com.free.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.free.TxPersonDialog;
import com.free.bean.Person;
import com.free.common.UIUtility;
import com.free.dao.SqliteAdapter;

public class TxPersonFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5660228650116347564L;

	JList<Person> jlistPerson = new JList<>();
	DefaultListModel<Person> listModelPerson = new DefaultListModel<Person>();

	JPanel jpCenter = new JPanel();

	JPanel jpPerson = new JPanel();

	public TxPersonFrame() {
		initCompo();
		loadPerson();
	}

	private void initCompo() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		// 人物列表区域
		jlistPerson.setFont(UIUtility.TEXT_FONT);
		jlistPerson.setCellRenderer(new TxPersonListCellRenderer());
		jlistPerson.setFixedCellWidth(20);

		jlistPerson.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int clickTis = e.getClickCount();
				if (clickTis < 2) {
					return;
				}

				Person para = jlistPerson.getSelectedValue();

				if (para == null) {
					return;
				}

				TxPersonDialog jd = TxPersonDialog.getOneInstance();
				jd.setPerson(para);
				jd.setVisible(true);

			}
		});

		JScrollPane sjpContents = new JScrollPane(jlistPerson);

		jpPerson.setLayout(new BorderLayout());

		jpPerson.add(sjpContents, BorderLayout.CENTER);

		jpCenter.setLayout(new BorderLayout());
		jpCenter.add(jpPerson, BorderLayout.CENTER);
		mainPanel.add(jpCenter, BorderLayout.CENTER);

		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = new Rectangle(scrSize);
		this.setBounds(bounds);

		this.setVisible(true);

		setContentPane(mainPanel);
	}

	private void loadPerson() {
		new Thread() {
			public void run() {

				ArrayList<Person> personList = (ArrayList<Person>) SqliteAdapter.selectPersons();
				if (personList == null) {
					return;
				}

				listModelPerson.removeAllElements();
				listModelPerson.addAll(personList);
				jlistPerson.setModel(listModelPerson);

			}
		}.start();

	}
}
