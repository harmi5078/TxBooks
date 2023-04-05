package com.free.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.free.bean.Book;
import com.free.bean.Dynasty;
import com.free.bean.Events;
import com.free.bean.Nation;
import com.free.bean.Paragraphs;
import com.free.bean.Person;
import com.free.common.Constants;
import com.free.common.TxStringUtil;
import com.free.msg.TxMessageAdapter;

public class SqliteAdapter {

	private static Connection connection = null;

	private static void initConn() throws Exception {
		if (connection == null) {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:D:\\Books\\books_ex.db");
			connection.setAutoCommit(true);
		}
	}

	public static ArrayList<Paragraphs> selectBookContents(Book book) {

		if (book == null) {
			return null;
		}

		ArrayList<Paragraphs> bookList = new ArrayList<Paragraphs>();

		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM txparagraphs " + "where bid=" + book.getBid());
			while (rs.next()) {
				Paragraphs paragraphs = new Paragraphs();
				paragraphs.setBid(rs.getInt("bid"));
				paragraphs.setGid(rs.getInt("gid"));
				paragraphs.setContent(rs.getString("content"));
				paragraphs.setTranscation(rs.getString("trans"));
				bookList.add(paragraphs);

				Constants.CurMaxContentId = paragraphs.getGid();
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return bookList;
	}

	public static ArrayList<Book> selectBooks(Book book) {

		ArrayList<Book> bookList = new ArrayList<Book>();

		Statement stmt = null;
		try {

			initConn();

			String sql = "select txbooks.bid,txbooks.bookname,txbooks.details, txalbum.bid as albumid, "
					+ "txalbum.bookname as ChiefAlbum,  txvolume.vid as volumeid, "
					+ "txvolume.bookname as volume  from txbooks "
					+ "inner join txalbum on txbooks.ChiefAlbum = txalbum.bid "
					+ "inner join txvolume on txbooks.volume1 = txvolume.vid where 1=1";
			if (book != null && book.getAlbumId() != 0) {
				sql = sql + " and txalbum.bid='" + book.getAlbumId() + "'";
			}

			if (book != null && book.getVolumeId() != 0) {
				sql = sql + " and txvolume.vid ='" + book.getVolumeId() + "'";
			}

			sql = sql + " order by txbooks.sqindex ";

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Book retBook = new Book();
				retBook.setBid(rs.getInt("bid"));
				retBook.setName(rs.getString("bookname"));
				retBook.setAlbumId(rs.getInt("albumid"));
				retBook.setChiefAlbum(rs.getString("ChiefAlbum"));
				retBook.setVolumeId(rs.getInt("volumeid"));
				retBook.setVolume(rs.getString("volume"));
				bookList.add(retBook);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return bookList;
	}

	public static ArrayList<Book> selectAlbum() {

		ArrayList<Book> bookList = new ArrayList<Book>();

		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from txalbum ");
			while (rs.next()) {
				Book book = new Book();
				book.setBid(rs.getInt("bid"));
				book.setAlbumId(rs.getInt("bid"));
				book.setStatus(rs.getInt("status"));
				book.setName(rs.getString("bookname"));
				book.setChiefAlbum(rs.getString("bookname"));
				bookList.add(book);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return bookList;
	}

	public static ArrayList<Dynasty> selectDynasty() {

		ArrayList<Dynasty> dList = new ArrayList<Dynasty>();

		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();

			String sql = "SELECT * FROM txDynasty " + "where bgtime>=" + Constants.bgtime + " and  endtime<="
					+ Constants.endtime;

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Dynasty dynasty = new Dynasty();
				dynasty.setId(rs.getInt("id"));
				dynasty.setName(rs.getString("name"));

				dynasty.setBgTime(rs.getInt("bgtime"));
				dynasty.setEndTime(rs.getInt("endtime"));
				dynasty.setCreator(rs.getString("creator"));

				dList.add(dynasty);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return dList;
	}

	public static ArrayList<Person> selectPersons() {

		ArrayList<Person> pList = new ArrayList<Person>();

		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select person.id,person.name,person.location,person.nation, nation.name as nationName, "
							+ "person.title,dynasty.id as dynasty," + "dynasty.name as dynastyName,person.details  "
							+ "from txperson as person  inner join txdynasty as dynasty  on person.dynasty = dynasty.id   "
							+ "inner join txnation as nation on person.nation = nation.id  ");
			while (rs.next()) {

				Person person = new Person();
				person.setPid(rs.getInt("id"));
				person.setName(rs.getString("name"));
				person.setTitle(rs.getString("title"));
				person.setDynastyName(rs.getString("dynastyName"));
				person.setDetail(rs.getString("details"));
				person.setDynasty(rs.getInt("dynasty"));
				person.setAddr(rs.getString("location"));
				person.setNation(rs.getInt("nation"));
				person.setNationName(rs.getString("nationName"));

				pList.add(person);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return pList;
	}

	public static void addRelation(int gid, int pid) {
		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();

			String time = TxStringUtil.getNowSortPlusTime();

			String sql = "INSERT INTO txgprelation (gid,pid,edittime ) " + "VALUES (" + gid + "," + pid + ",'" + time
					+ "' );";
			stmt.executeUpdate(sql);

			stmt.close();

			TxMessageAdapter.notiyMessage(TxMessageAdapter.MSTYPE_ADDPERSONRELATION, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void addPerson(Person p) {
		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();

			String sql = "INSERT INTO txperson (name,nation,location,dynasty,title,details ) " + "VALUES ('"
					+ p.getName() + "','" + p.getNation() + "','" + p.getAddr() + "'," + p.getDynasty() + ",'"
					+ p.getTitle() + "','" + p.getDetail() + "' );";
			stmt.executeUpdate(sql);

			stmt.close();

			TxMessageAdapter.notiyMessage(TxMessageAdapter.MSTYPE_ADDPERSON, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addEvent(Events event) {
		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();

			String time = TxStringUtil.getNowSortPlusTime();

			String sql = "INSERT INTO txevents (name,keys,details, edittime) " + "VALUES ('" + event.getName() + "','"
					+ event.getKeys() + "','" + event.getDetails() + "','" + time + "' );";

			stmt.executeUpdate(sql);

			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Person> selectPersonWithParagraphs(int gid) {

		ArrayList<Person> pList = new ArrayList<Person>();

		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();
			ResultSet rs = stmt
					.executeQuery("select person.id,person.name,person.title,dynasty.name as dynasty,person.details "
							+ "from txperson as person "
							+ "inner join txdynasty as dynasty on person.dynasty = dynasty.id "
							+ "inner join txgprelation  rela on rela.pid = person.id " + "where rela.gid = " + gid);
			while (rs.next()) {
				Person person = new Person();
				person.setPid(rs.getInt("id"));
				person.setName(rs.getString("name"));
				person.setTitle(rs.getString("title"));
				person.setDynastyName(rs.getString("dynasty"));
				person.setDetail(rs.getString("details"));
				pList.add(person);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return pList;
	}

	public static ArrayList<Book> selectVolumes(Book album) {

		ArrayList<Book> bookList = new ArrayList<Book>();

		Statement stmt = null;
		try {

			initConn();

			String sql = "select  txalbum.bid as albumid,  txalbum.bookname as ChiefAlbum, "
					+ "txvolume.vid as volumeid, txvolume.bookname as volume  from txvolume "
					+ "inner join txalbum on txvolume.bid = txalbum.bid where 1=1 ";
			if (album != null && album.getBid() > 0) {
				sql = sql + " and txvolume.bid =" + album.getBid();
			}
			sql = sql + " order by txvolume.sqindex";

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Book book = new Book();
				book.setBid(rs.getInt("volumeid"));
				book.setName(rs.getString("volume"));
				book.setVolumeId(rs.getInt("volumeid"));

				book.setVolume(book.getName());

				book.setAlbumId(rs.getInt("albumid"));
				book.setChiefAlbum(rs.getString("ChiefAlbum"));

				bookList.add(book);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return bookList;

	}

	public static void savePerson(Person p) {
		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();

			String sql = "update txperson set name = '" + p.getName() + "'," + "nation = '" + p.getNation() + "',"
					+ "location = '" + p.getAddr() + "'," + "dynasty = " + p.getDynasty() + "," + "title = '"
					+ p.getTitle() + "'," + "details = '" + p.getDetail() + "' where id=" + p.getPid();
			stmt.executeUpdate(sql);

			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Nation> selectNation() {
		ArrayList<Nation> dList = new ArrayList<Nation>();

		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM txNation");

			Nation nation = new Nation();

			while (rs.next()) {
				nation = new Nation();
				nation.setId(rs.getInt("id"));
				nation.setName(rs.getString("name"));

				nation.setBgTime(rs.getInt("bgtime"));
				nation.setEndTime(rs.getInt("endtime"));
				nation.setCreator(rs.getString("creator"));

				dList.add(nation);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return dList;
	}

	public static ArrayList<Person> selectPersons(Person p) {

		ArrayList<Person> pList = new ArrayList<Person>();

		Statement stmt = null;
		try {

			initConn();

			String sql = "select person.id,person.name,person.location,person.nation, nation.name as nationName, "
					+ "person.title,dynasty.id as dynasty," + "dynasty.name as dynastyName,person.details  "
					+ "from txperson as person inner join txdynasty as dynasty on person.dynasty = dynasty.id   "
					+ "inner join txnation as nation on person.nation = nation.id where 1=1 ";

			if (!TxStringUtil.isEmpty(p.getName())) {
				sql = sql + " and person.name like '%" + p.getName() + "%'";
			}

			if (!TxStringUtil.isEmpty(p.getAddr())) {
				sql = sql + " and person.location like '%" + p.getAddr() + "%'";
			}

			if (!TxStringUtil.isEmpty(p.getTitle())) {
				sql = sql + " and person.title like '%" + p.getTitle() + "%'";
			}

			if (!TxStringUtil.isEmpty(p.getDetail())) {
				sql = sql + " and person.details like '%" + p.getDetail() + "%'";
			}

			if (p.getDynasty() != 0 || p.getNation() != 0) {
				sql = sql + " and ( person.dynasty =  " + p.getDynasty() + "  or person.nation =   " + p.getNation()
						+ ")";
			}

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				Person person = new Person();
				person.setPid(rs.getInt("id"));
				person.setName(rs.getString("name"));
				person.setTitle(rs.getString("title"));
				person.setDynastyName(rs.getString("dynastyName"));
				person.setDetail(rs.getString("details"));
				person.setDynasty(rs.getInt("dynasty"));
				person.setAddr(rs.getString("location"));
				person.setNation(rs.getInt("nation"));
				person.setNationName(rs.getString("nationName"));

				pList.add(person);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return pList;
	}

	public static ArrayList<Events> selectEvents() {

		ArrayList<Events> dList = new ArrayList<Events>();

		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM txevents ");

			Events events;

			while (rs.next()) {
				events = new Events();
				events.setId(rs.getInt("id"));
				events.setName(rs.getString("name"));

				events.setDetails(rs.getString("details"));

				dList.add(events);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return dList;
	}

	public static void addEventRelation(int gid, int eid) {
		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();

			String time = TxStringUtil.getNowSortPlusTime();

			String sql = "INSERT INTO txgerelation (gid,eid,edittime ) " + "VALUES (" + gid + "," + eid + ",'" + time
					+ "' );";
			stmt.executeUpdate(sql);

			stmt.close();

			TxMessageAdapter.notiyMessage(TxMessageAdapter.MSTYPE_ADDPERSONRELATION, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<Events> selectEventsWithParagraphs(int gid) {
		ArrayList<Events> pList = new ArrayList<Events>();

		Statement stmt = null;
		try {

			initConn();

			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select evt.id,evt.name,evt.details from txevents as evt "
					+ "	 inner join txgerelation  rela on rela.eid = evt.id " + "  where rela.gid = " + gid);
			while (rs.next()) {
				Events event = new Events();
				event.setId(rs.getInt("id"));
				event.setName(rs.getString("name"));
				event.setDetails(rs.getString("details"));
				pList.add(event);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return pList;
	}

}
