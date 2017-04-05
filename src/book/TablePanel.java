/*
 * JTable이 얻혀질 패널
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
	
	//Vector와 ArrayList는 같다. 차이점은 동기화 지원여부!
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
		
		//생성자 타이밍에는 con을 가져오기 빨라서, "따로 메서드를 만들었다!"
		init();
		
		//모델을 테이블에 적용
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
	
	
	//book테이블의 레코드 가져오기
	public void init(){
		String sql = "select * from book order by book_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		
		try {
			pstmt= con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			cols = meta.getColumnCount();
			
			//새로 등록할때 기존꺼 지우고 새로 쓰자!
			list.removeAll(list);
			
			//rs의 정보를 컬렉션의 DTO로으로 옮겨담자!!
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
