import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DB_Connection {
	
	private Connection db;
	private String curUserIDConnectedToDB;
	private PreparedStatement ps;
	private ResultSet rs;
	
	public boolean setDBConnection(String id, String pass)
	{
		this.curUserIDConnectedToDB = id;
		
		try {
			
			Class.forName("oracle.jdbc.OracleDriver");
			this.db = DriverManager.getConnection("jdbc:oracle:thin:" + "@localhost:1521:XE", id, pass);
			System.out.println("데이터베이스접속 성공 - id: " + id);
			return true;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception:" + e);
		}

		return false;
	}
	
	@SuppressWarnings("static-access")
	public int tableUpdate_employee( int id, String name, String position,
			int total_performance)
	{
		int n = -1;
		String query = "insert into employee values(?, ?, ?, ?)";
		ps = null;
				
		try {
			
			ps = db.prepareStatement(query);
			ps.setString(1, Integer.toString(id));
			ps.setString(2, name);
			ps.setString(3, position);
			ps.setInt(4, total_performance);
			n = ps.executeUpdate();
			
		} catch (SQLException e) {			
			System.out.println(e.getMessage());
			JOptionPane warning = new JOptionPane();
			warning.showMessageDialog(null, "직원등록은 Supervisor만 할 수 있습니다.", "등록 실패",
					warning.WARNING_MESSAGE);
		}
		return n;
	}

	@SuppressWarnings("static-access")
	public int tableUpdate_customer(int id, String name, String birth_day,
			String phone, int tpp, String grade)
	{
		int n = -1;
		String query = "insert into customer values(?, ?, ?, ?, ?, ?)";
		ps = null;
				
		try {
			
			ps = db.prepareStatement(query);
			ps.setString(1, Integer.toString(id));
			ps.setString(2, name);
			ps.setString(3, birth_day);
			ps.setString(4, phone);
			ps.setInt(5, tpp);
			ps.setString(6, grade);			
			n = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			JOptionPane warning = new JOptionPane();
			warning.showMessageDialog(null, "회원등록은 Supervisor만 할 수 있습니다.", "등록 실패",
					warning.WARNING_MESSAGE);

		}
		
		return n;
	}

	public int tableUpdate_menu(String menuName, int price)
	{
		int n = -1;
		String query = "insert into menu values(?, ?)";
		ps = null;
				
		try {
			
			ps = db.prepareStatement(query);
			ps.setString(1, menuName);
			ps.setInt(2, price);	
			n = ps.executeUpdate();
			
		} catch (SQLException e) {			
			System.out.println(e.getMessage());
			JOptionPane warning = new JOptionPane();
			warning.showMessageDialog(null, "메뉴등록은 Supervisor만 할 수 있습니다.", "등록 실패",
					warning.WARNING_MESSAGE);
		}
		
		return n;
	}
	
	public int tableUpdate_sales(String custID, String menuName, 
			int order_quantity, int year, int month, 
			int day, int hour, int minute)
	{
		int n = -1;
		String query = "insert into menu values(?, ?, ?, ?, ?, ?, ?, ?)";
		ps = null;
				
		try {
			
			ps = db.prepareStatement(query);
			ps.setString(1, custID);
			ps.setString(2, menuName);
			ps.setInt(3, order_quantity);
			ps.setInt(4, year);
			ps.setInt(5, month);
			ps.setInt(6, day);
			ps.setInt(7, hour);
			ps.setInt(8, minute);
			n = ps.executeUpdate();
			
		} catch (SQLException e) {			
			System.out.println(e.getMessage());
		}
		
		return n;
	}
	
	public int insertTupleToSales(int id, String name, String menuName, int year,
										int month, int day,	int hour, int minute){
		
		int n = -1;
		String query = "insert into sales values(?, ?, ?, ?, ?, ?)";
		ps = null;
		String date = Integer.toString(year)+"-"+Integer.toString(month)
											+"-"+Integer.toString(day);
		
		try {
			ps = db.prepareStatement(query);
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setString(3, menuName);
			ps.setDate(4, java.sql.Date.valueOf(date));
			ps.setInt(5, hour);
			ps.setInt(6, minute);
			n = ps.executeUpdate();
			
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return n;
	}
	
	public int updateTotalPurchasingPrice(String custName, int tpp)
	{
		int n = -1;
		String query = "update customer set total_purchasing_price = ? "
				+ "where name = ?";
		ps = null;
		
		try {
			
			ps = db.prepareStatement(query);
			ps.setInt(1, tpp);
			ps.setString(2, custName);
			n = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return n;
	}
	
	public int updateGrade(String custName, String grade)
	{
		int n = -1;
		String query = "update customer set grade = ? "
				+ "where name = ?";
		ps = null;
		
		try {
			
			ps = db.prepareStatement(query);
			ps.setString(1, grade);
			ps.setString(2, custName);
			n = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return n;
	}
	
	public int updateTotalPerformance(int total_price)
	{
		//update table set column = '변경값' where 조건
		String query = "update employee set total_performance = ? where name = ?";
		ps = null;
		int n = -1;
		
		try {
			ps = db.prepareStatement(query);
			ps.setInt(1, total_price);
			ps.setString(2, this.curUserIDConnectedToDB);			
			n = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.getMessage();
			e.printStackTrace();
		}
		
		return n;
	}	
	
	public ResultSet getTPPAndIdFromDB(String custName)
	{
		ps = null;
		rs = null;
		String query = "select total_purchasing_price, id from customer "
				+ "where name = ?";
		
		try {
			ps = db.prepareStatement(query);
			ps.setString(1, custName);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
		
		
	}
	
	public String getCurUserIDConnectedToDB()
	{
		return this.curUserIDConnectedToDB;
	}
	
	public int executeUpdate(String query)
	{
		ps = null;
		int n = -1;
		try{
			
			ps = db.prepareStatement(query);
			n = ps.executeUpdate();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
		return n;
	}
	
	public ResultSet executeQuery(String query) throws SQLException
	{		
		rs = null;
		ps = null;		
		
		try{			
			ps = db.prepareStatement(query);
			rs = ps.executeQuery();			
		}catch(SQLException se)	{
			System.out.println(se.getMessage());
	
		}
		return rs;
	}
	
	public void closeStatementResultSet()
	{
		try{
			if(this.ps != null) this.ps.close();
			if(this.rs != null) this.rs.close();
		}catch(SQLException e)
		{
			e.getMessage();
		}
	}
	
	public void commit()
	{
		try {
			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setCommitMode(boolean set)
	{
		try {			
			db.setAutoCommit(set);			
		} catch (SQLException e) {			
			e.printStackTrace();		
		}
	}
}
