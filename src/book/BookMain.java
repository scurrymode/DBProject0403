package book;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener, ActionListener {
	DBManager manager;
	Connection con;

	JPanel p_west; // 좌측 등록폼
	JPanel p_content; // 우측 영역 전체
	JPanel p_north; // 우측 선택 모드 영역
	JPanel p_center; // 우측 하단 패널 flow layout
	JPanel p_table; // JTable이 붙을 패널
	JPanel p_grid; // 그리드 방식으로 보여질

	Choice ch_top;
	Choice ch_sub;
	JTextField t_name;
	JTextField t_price;
	Canvas can;
	JButton bt_regist;

	CheckboxGroup group;
	Checkbox cb_table, cb_grid;

	Toolkit kit = Toolkit.getDefaultToolkit();
	Image img;
	JFileChooser chooser;
	File file; // 내가 선택한 이미지 파일

	// html option과는 다르므로 Choice 컴포넌트의 값을 미리 배열로 받아놓자!!
	// 우리가 만든 클래스를 자료형으로 삼았다!
	ArrayList<SubCategory> subcategory = new ArrayList<SubCategory>();

	// 이미지 복사를 위한 스트림
	FileInputStream fis;
	FileOutputStream fos;

	public BookMain() {
		p_west = new JPanel();
		p_content = new JPanel();
		p_north = new JPanel();
		p_center = new JPanel();
		p_table = new TablePanel();
		p_grid = new GridPanel();
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField(10);
		t_price = new JTextField(10);

		// ImageIO.read로 자원 가져오기!
		URL url = this.getClass().getResource("/default.jpg");

		try {
			img = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		can = new Canvas() {
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 140, 140, this);
			}
		};
		bt_regist = new JButton("등록");
		group = new CheckboxGroup();
		cb_table = new Checkbox("테이블로 보기", group, true);
		cb_grid = new Checkbox("이미지로 보기", group, false);
		// 파일 추서 올리기
		chooser = new JFileChooser("c:/html_workspace/images");

		ch_top.setPreferredSize(new Dimension(130, 45));
		ch_sub.setPreferredSize(new Dimension(130, 45));

		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can);
		p_west.add(bt_regist);
		p_west.setPreferredSize(new Dimension(150, 600));
		can.setPreferredSize(new Dimension(140, 140));

		p_north.add(cb_table);
		p_north.add(cb_grid);

		p_center.add(p_table);
		p_center.add(p_grid);
		p_center.setBackground(Color.PINK);

		p_content.setLayout(new BorderLayout());
		p_content.add(p_north, BorderLayout.NORTH);
		p_content.add(p_center);

		add(p_west, BorderLayout.WEST);
		add(p_content);

		// 카테고리 불러오기
		init();

		// 테이블 패널과 그리드 패널에서 Connection전달
		((TablePanel) p_table).setConnection(con);
		((GridPanel) p_grid).setConnection(con);

		// 아이템 리스너 붙이기
		ch_top.addItemListener(this);
		// 이미지 마우스리스너붙이기
		can.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openFile();
			}
		});

		// 버튼에 액션 붙이기
		bt_regist.addActionListener(this);

		// 초이스 컴포넌트와 리스너 연결
		cb_table.addItemListener(this);
		cb_grid.addItemListener(this);

		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void init() {
		// 초이스 컴포넌트의 최상위 목록 보이기!!
		manager = DBManager.getInstance();
		con = manager.getConnection();
		String sql = "select * from topcategory order by topcategory_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ch_top.add(rs.getString("category_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void getSub(String v) {
		// 기존에 이미 채워진 item이 있다면 먼저 지워버려
		ch_sub.removeAll();
		subcategory.removeAll(subcategory);

		StringBuffer sb = new StringBuffer();
		sb.append("select * from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append("select topcategory_id from");
		sb.append(" topcategory where category_name ='" + v + "') order by subcategory_id asc");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			// 서브 카테고리의 정보를 2차원 배열에 담기 + ch_sub에 추가
			// rs에 담겨진 레코드 1개는 SubCategory 클래스의 인스턴스 1개로 받자!!

			while (rs.next()) {
				SubCategory dto = new SubCategory();
				// 이게 매핑
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));

				subcategory.add(dto); // 컬렉션에 담기
				ch_sub.add(dto.getCategory_name());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 상품 등록 메서드
	public void regist() {

		// 내가 지금 선택한 서브 카테고리 초이스의 index를 구해서 그 index로 ArrayList를 접근해서 객체를 반환받아서
		// 그 정보를 갖다 쓴다.
		int index = ch_sub.getSelectedIndex();
		SubCategory dto = subcategory.get(index);

		String book_name = t_name.getText();// 입력한 책 이름
		int price = Integer.parseInt(t_price.getText());// 입력한 가격
		String img = file.getName();// 파일명

		StringBuffer sb = new StringBuffer();
		sb.append("insert into book(book_id, subcategory_id, book_name, price, img)");
		sb.append(" values(seq_book.nextval, " + dto.getSubcategory_id() + ",'" + book_name + "'," + price + ",'" + img
				+ "')");

		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			// SQL문이 DML(insert, delete, update)일때, executeUpdate();
			int result = pstmt.executeUpdate();
			// result는 숫자값을 반환한다 이 쿼리로 영향받는 레코드 숫자 그래서 항상 1
			if (result != 0) {
				copy();
				JOptionPane.showMessageDialog(this, book_name + "등록성공");
				((TablePanel) p_table).setConnection(con);
				((GridPanel) p_grid).setConnection(con);

			} else {
				JOptionPane.showMessageDialog(this, book_name + "등록실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 하위 카테고리 가져오기!
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if (obj == ch_top) {
			Choice ch = (Choice) e.getSource();
			getSub(ch.getSelectedItem());
		} else if (obj == cb_table) {
			// System.out.println("테이블?");
			p_table.setVisible(true);
			p_grid.setVisible(false);
		} else if (obj == cb_grid) {
			// System.out.println("그리드??");
			p_table.setVisible(false);
			p_grid.setVisible(true);
		}
	}

	// 그림파일 불러오기!
	public void openFile() {
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {// 긍정적 답변을 줬다면!
			// 선택한 이미지를 캔버스에 그릴거다!
			file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}

	// 그림파일 복사하기! 유저가 선택한 이미지를 개발자가 지정한 위치로 복사를 해놓자!
	public void copy() {
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream("c:/java_workspace2/DBProject3/data/" + file.getName());

			int data = 0;
			byte[] b = new byte[1024];
			while (true) {
				data = fis.read(b);
				if (data == -1)
					break;
				fos.write(b);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		regist();
	}

	public static void main(String[] args) {
		new BookMain();

	}

}
