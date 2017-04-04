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
	
	//개발자가 제공하는 방법 이외의 접근은 아예 차단하자! new 막자!
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
	
	//인스턴스의 생성없이도, 외부에서 메서드를 호출하여 이 객체의 인스턴스를 가져갈 수 있도록 getter를 제공하자!
	static public ConnectionManager getInstance(){
		if(instance==null){
			instance=new ConnectionManager();
		}
		return instance;
	}
	
	//로드된 드라이버 정보를 줘야해~
	public Connection getConnection(){
		return con;
	}
	
	//커넥션을 다 사용후 닫기
	public void disConnect(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
