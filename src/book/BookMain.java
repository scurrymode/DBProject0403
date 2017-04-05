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

	JPanel p_west; // ���� �����
	JPanel p_content; // ���� ���� ��ü
	JPanel p_north; // ���� ���� ��� ����
	JPanel p_center; // ���� �ϴ� �г� flow layout
	JPanel p_table; // JTable�� ���� �г�
	JPanel p_grid; // �׸��� ������� ������

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
	File file; // ���� ������ �̹��� ����

	// html option���� �ٸ��Ƿ� Choice ������Ʈ�� ���� �̸� �迭�� �޾Ƴ���!!
	// �츮�� ���� Ŭ������ �ڷ������� ��Ҵ�!
	ArrayList<SubCategory> subcategory = new ArrayList<SubCategory>();

	// �̹��� ���縦 ���� ��Ʈ��
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

		// ImageIO.read�� �ڿ� ��������!
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
		bt_regist = new JButton("���");
		group = new CheckboxGroup();
		cb_table = new Checkbox("���̺�� ����", group, true);
		cb_grid = new Checkbox("�̹����� ����", group, false);
		// ���� �߼� �ø���
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

		// ī�װ� �ҷ�����
		init();

		// ���̺� �гΰ� �׸��� �гο��� Connection����
		((TablePanel) p_table).setConnection(con);
		((GridPanel) p_grid).setConnection(con);

		// ������ ������ ���̱�
		ch_top.addItemListener(this);
		// �̹��� ���콺�����ʺ��̱�
		can.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openFile();
			}
		});

		// ��ư�� �׼� ���̱�
		bt_regist.addActionListener(this);

		// ���̽� ������Ʈ�� ������ ����
		cb_table.addItemListener(this);
		cb_grid.addItemListener(this);

		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void init() {
		// ���̽� ������Ʈ�� �ֻ��� ��� ���̱�!!
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
		// ������ �̹� ä���� item�� �ִٸ� ���� ��������
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

			// ���� ī�װ��� ������ 2���� �迭�� ��� + ch_sub�� �߰�
			// rs�� ����� ���ڵ� 1���� SubCategory Ŭ������ �ν��Ͻ� 1���� ����!!

			while (rs.next()) {
				SubCategory dto = new SubCategory();
				// �̰� ����
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));

				subcategory.add(dto); // �÷��ǿ� ���
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

	// ��ǰ ��� �޼���
	public void regist() {

		// ���� ���� ������ ���� ī�װ� ���̽��� index�� ���ؼ� �� index�� ArrayList�� �����ؼ� ��ü�� ��ȯ�޾Ƽ�
		// �� ������ ���� ����.
		int index = ch_sub.getSelectedIndex();
		SubCategory dto = subcategory.get(index);

		String book_name = t_name.getText();// �Է��� å �̸�
		int price = Integer.parseInt(t_price.getText());// �Է��� ����
		String img = file.getName();// ���ϸ�

		StringBuffer sb = new StringBuffer();
		sb.append("insert into book(book_id, subcategory_id, book_name, price, img)");
		sb.append(" values(seq_book.nextval, " + dto.getSubcategory_id() + ",'" + book_name + "'," + price + ",'" + img
				+ "')");

		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			// SQL���� DML(insert, delete, update)�϶�, executeUpdate();
			int result = pstmt.executeUpdate();
			// result�� ���ڰ��� ��ȯ�Ѵ� �� ������ ����޴� ���ڵ� ���� �׷��� �׻� 1
			if (result != 0) {
				copy();
				JOptionPane.showMessageDialog(this, book_name + "��ϼ���");
				((TablePanel) p_table).setConnection(con);
				((GridPanel) p_grid).setConnection(con);

			} else {
				JOptionPane.showMessageDialog(this, book_name + "��Ͻ���");
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

	// ���� ī�װ� ��������!
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if (obj == ch_top) {
			Choice ch = (Choice) e.getSource();
			getSub(ch.getSelectedItem());
		} else if (obj == cb_table) {
			// System.out.println("���̺�?");
			p_table.setVisible(true);
			p_grid.setVisible(false);
		} else if (obj == cb_grid) {
			// System.out.println("�׸���??");
			p_table.setVisible(false);
			p_grid.setVisible(true);
		}
	}

	// �׸����� �ҷ�����!
	public void openFile() {
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {// ������ �亯�� ��ٸ�!
			// ������ �̹����� ĵ������ �׸��Ŵ�!
			file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}

	// �׸����� �����ϱ�! ������ ������ �̹����� �����ڰ� ������ ��ġ�� ���縦 �س���!
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
