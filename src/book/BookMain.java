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
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener, ActionListener{
	DBManager manager;
	Connection con;
	
	JPanel p_west; //���� �����
	JPanel p_content; //���� ���� ��ü
	JPanel p_north; //���� ���� ��� ����
	JPanel p_table; //JTable�� ���� �г�
	JPanel p_grid; //�׸��� ������� ������
	
	Choice ch_top;
	Choice ch_sub;
	JTextField t_name;
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	
	CheckboxGroup group;
	Checkbox cb_table, cb_grid;

	Toolkit kit=Toolkit.getDefaultToolkit();
	Image img;
	JFileChooser chooser;
	
	public BookMain() {
		p_west = new JPanel();
		p_content = new JPanel();
		p_north = new JPanel();
		p_table = new JPanel();
		p_grid = new JPanel();
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField(10);
		t_price = new JTextField(10);
		
		//ImageIO.read�� �ڿ� ��������!
		URL url = this.getClass().getResource("/default.jpg");
		
		try {
			img=ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		can= new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 140, 140, this);
			}
		};
		bt_regist = new JButton("���");
		group = new CheckboxGroup();
		cb_table = new Checkbox("���̺��� ����", group, true);
		cb_grid = new Checkbox("�̹����� ����", group, false);
		//���� �߼� �ø���
		chooser = new JFileChooser("c:/html_workspace/images");
		
		
		ch_top.setPreferredSize(new Dimension(130, 45));
		ch_sub.setPreferredSize(new Dimension(130, 45));
		
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can);
		p_west.add(bt_regist);
		p_west.setBackground(Color.YELLOW);
		p_west.setPreferredSize(new Dimension(150,600));
		can.setPreferredSize(new Dimension(140,140));
		
		p_north.add(cb_table);
		p_north.add(cb_grid);

		p_content.setLayout(new BorderLayout());
		p_content.add(p_north, BorderLayout.NORTH);
		p_content.add(p_table);
		p_content.add(p_grid);
		
		p_grid.setVisible(false);
		
		add(p_west, BorderLayout.WEST);
		add(p_content);
		
		//ī�װ��� �ҷ�����
		init();
		//������ ������ ���̱�
		ch_top.addItemListener(this);
		//�̹��� �׼Ǻ��̱�
		can.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openFile();
			}
		});
		
		setSize(800, 600);		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void init(){
		//���̽� ������Ʈ�� �ֻ��� ��� ���̱�!!
		manager=DBManager.getInstance();
		con=manager.getConnection();
		String sql="select * from topcategory";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ch_top.add(rs.getString("category_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if(pstmt!=null){
				try {
					pstmt.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void getSub(String v){
		//������ �̹� ä���� item�� �ִٸ� ���� ��������
		ch_sub.removeAll();
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append("select topcategory_id from");
		sb.append(" topcategory where category_name ='"+v+"')");
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			while(rs.next()){
				ch_sub.add(rs.getString("category_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if(pstmt!=null){
				try {
					pstmt.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//���� ī�װ��� ��������!
	public void itemStateChanged(ItemEvent e) {
		Choice ch=(Choice)e.getSource();
		getSub(ch.getSelectedItem());		
	}
	
	//�׸����� �ҷ�����!
	public void openFile(){
		int result=chooser.showOpenDialog(this);
		if(result== JFileChooser.APPROVE_OPTION){//������ �亯�� ��ٸ�!
			//������ �̹����� ĵ������ �׸��Ŵ�!
			File file=chooser.getSelectedFile();
			img=kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
		
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("��ư �ҷ���?");		
	}
	
	
	public static void main(String[] args) {
		new BookMain();
		

	}

}