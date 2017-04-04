package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	static private ConnectionManager instance;
	
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="batman";
	String password="1234";
	
	Connection con;
	
	//�����ڰ� �����ϴ� ��� �̿��� ������ �ƿ� ��������! new ����!
	private ConnectionManager() {
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//�ν��Ͻ��� �������̵�, �ܺο��� �޼��带 ȣ���Ͽ� �� ��ü�� �ν��Ͻ��� ������ �� �ֵ��� getter�� ��������!
	static public ConnectionManager getInstance(){
		if(instance==null){
			instance=new ConnectionManager();
		}
		return instance;
	}
	
	//�ε�� ����̹� ������ �����~
	public Connection getConnection(){
		return con;
	}
	
	//Ŀ�ؼ��� �� ����� �ݱ�
	public void disConnect(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
