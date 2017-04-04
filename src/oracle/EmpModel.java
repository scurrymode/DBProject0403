package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class EmpModel extends AbstractTableModel{
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	String[] column; //�÷��� ���� �迭
	String[][] data; //���ڵ带 ���� �迭
	
	//�����ڿ��� �̸� ������ ������ �ؾ��Ѵ�.
	public EmpModel(Connection con) {
		this.con=con;
		
		//1. ����̹� �ε� 2. ���� 3. ������ ���� 4. ���Ӵݱ�
		try {
			if(con!=null){
				String sql="select * from emp";
				
				//�Ʒ� pstmt�� ���� ��������� rs�� Ŀ���� �����ο� �� �ִ�.
				pstmt=con.prepareStatement(sql, 
						ResultSet.TYPE_SCROLL_INSENSITIVE, 
						ResultSet.CONCUR_READ_ONLY);
				
				//��� ���� ��ȯ
				rs=pstmt.executeQuery();
				
				//�÷��� ���غ���!
				ResultSetMetaData meta=rs.getMetaData();
				int count=meta.getColumnCount();
				column = new String[count];
				for(int i=0;i<column.length;i++){
					column[i]=meta.getColumnName(i+1);
				}
				
				rs.last();//���� ���������� ����
				int total=rs.getRow();//���ڵ� ��ȣ
				rs.beforeFirst();
				
				//�� ���ڵ���� �˾�����, �������迭 �����غ���!
				data = new String[total][column.length];
				
				//���ڵ带 2���� �迭�� data�� ä���ֱ�
				for(int j=0; j<data.length;j++){
					rs.next();
					for(int i=0; i<data[j].length;i++){
						data[j][i]=rs.getString(column[i]);
					}
				}
			}	
		} catch (SQLException e) {
			System.out.println("���� ����");
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
	
	public int getColumnCount() {
		return column.length;
	}
	
	
	public String getColumnName(int index) {
		return column[index];
	}

	
	public int getRowCount() {
		return data.length;
	}

	
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

}
