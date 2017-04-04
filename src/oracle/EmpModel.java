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
	
	String[] column; //컬럼을 넣을 배열
	String[][] data; //레코드를 넣을 배열
	
	//생성자에서 미리 데이터 연동을 해야한다.
	public EmpModel(Connection con) {
		this.con=con;
		
		//1. 드라이버 로드 2. 접속 3. 쿼리문 수행 4. 접속닫기
		try {
			if(con!=null){
				String sql="select * from emp";
				
				//아래 pstmt에 의해 만들어지는 rs는 커서가 자유로울 수 있다.
				pstmt=con.prepareStatement(sql, 
						ResultSet.TYPE_SCROLL_INSENSITIVE, 
						ResultSet.CONCUR_READ_ONLY);
				
				//결과 집합 반환
				rs=pstmt.executeQuery();
				
				//컬럼을 구해보자!
				ResultSetMetaData meta=rs.getMetaData();
				int count=meta.getColumnCount();
				column = new String[count];
				for(int i=0;i<column.length;i++){
					column[i]=meta.getColumnName(i+1);
				}
				
				rs.last();//제일 마지막으로 보냄
				int total=rs.getRow();//레코드 번호
				rs.beforeFirst();
				
				//총 레코드수를 알았으니, 이차원배열 생성해보자!
				data = new String[total][column.length];
				
				//레코드를 2차원 배열인 data에 채워넣기
				for(int j=0; j<data.length;j++){
					rs.next();
					for(int i=0; i<data[j].length;i++){
						data[j][i]=rs.getString(column[i]);
					}
				}
			}	
		} catch (SQLException e) {
			System.out.println("접속 실패");
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
