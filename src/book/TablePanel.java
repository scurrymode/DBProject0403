/*
 * JTable�� ������ �г�
 * */

package book;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TablePanel extends JPanel{
	JTable table;
	JScrollPane scroll;
	TableModel model;
	
	//Vector�� ArrayList�� ����. �������� ����ȭ ��������!
	Vector<Vector> list= new Vector<Vector>();
	String[] columnName={"book_id", "book_name", "price", "img", "subcategory_id"};
	
	Connection con;
	
	int cols;
	
	public TablePanel() {
		table = new JTable();	
		scroll = new JScrollPane(table);
		
		setLayout(new BorderLayout());
		this.add(scroll);
		
		this.setBackground(Color.CYAN);
		setPreferredSize(new Dimension(650, 550));
	}
	
	public void setConnection(Connection con){
		this.con=con;
		
		//������ Ÿ�ֿ̹��� con�� �������� ����, "���� �޼��带 �������!"
		init();
		
		//���� ���̺� ����
		model=new AbstractTableModel() {
			public int getRowCount() {
				return list.size();
			}
			public int getColumnCount() {
				return cols;
			}

			public String getColumnName(int column) {
				return columnName[column];
			}
			
			public Object getValueAt(int row, int col) {
				Vector vec=(Vector)list.elementAt(row);
				return vec.elementAt(col);
			}
		};

		table.setModel(model);
	}
	
	
	//book���̺��� ���ڵ� ��������
	public void init(){
		String sql = "select * from book order by book_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		
		try {
			pstmt= con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			cols = meta.getColumnCount();
			
			//���� ����Ҷ� ������ ����� ���� ����!
			list.removeAll(list);
			
			//rs�� ������ �÷����� DTO������ �Űܴ���!!
			while(rs.next()){
				Vector<String> data = new Vector<String>();
				data.add(Integer.toString(rs.getInt("book_id")));
				data.add(rs.getString("book_name"));
				data.add(Integer.toString(rs.getInt("price")));
				data.add(rs.getString("img"));
				data.add(Integer.toString(rs.getInt("subcategory_id")));
				list.add(data);
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
