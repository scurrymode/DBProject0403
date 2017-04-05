/*
 * JTable이 얻혀질 패널
 * */

package book;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GridPanel extends JPanel {
	private Connection con;
	ArrayList<Book> list = new ArrayList<Book>();

	public GridPanel() {
		this.setBackground(Color.RED);
		this.setVisible(false);
		setPreferredSize(new Dimension(650, 550));
	}

	public void setConnection(Connection con) {
		this.con = con;
		// 기존의 어레이리스트를 싹 지운다.
		list.removeAll(list);
		// 기존에 올려놓은 페널도 다 지운다.
		removeAll();

		init();
		updateUI();// 재로딩
	}

	public void init() {
		String sql = "select * from book order by book_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			// 맵핑과정
			while (rs.next()) {
				Book dto = new Book();

				dto.setBook_id(rs.getInt("book_id"));
				dto.setBook_name(rs.getString("book_name"));
				dto.setPrice(rs.getInt("price"));
				dto.setImg(rs.getString("img"));
				dto.setSubcategory_id(rs.getInt("subcategory_id"));

				list.add(dto);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		String path = "C:/java_workspace2/DBProject3/data/";
		Image img = null;

		for (int i = 0; i < list.size(); i++) {
			try {
				img = ImageIO.read(new File(path + list.get(i).getImg()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			BookItem item = new BookItem(img, list.get(i).getBook_name(), Integer.toString(list.get(i).getPrice()));
			add(item);
		}
	}
}
