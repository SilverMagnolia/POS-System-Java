import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class DB_Interface {
	
	private boolean log_in; // db에 로그인이 된 상태라면  true, 안 되어 있다면 false
	
	private DB_Connection dc;	
	private MenuBarControll mbController;
	private MenuControll mController;
	private Enroll_inquiryControll eiController;
	private OrderStatusControll osController;	
	private TableStatusControll tsController;
	
	DB_Interface()
	{
		dc = new DB_Connection();
		log_in = false;
	}

	// 메뉴 패널 초기화
	public void initMenuPanel()
	{
		
		String query = "select * from menu";
		ResultSet rs = null;
		try {
			rs = dc.executeQuery(query);
			mController.initMenu(rs);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// 파일 데이터 디비에 저장
	public boolean readFile(File file) throws SQLException {

		// DB에 필요한 테이블이 없을 경우, 생성하는 메소드 호출.
		if (create_db_tables()) 
			System.out.println("테이블 생성 완료..");
		else
			return false;		

		if (create_synonyms())
			System.out.println("테이블의 synonym 생성 완료..");
		
		ResultSet rs;
		Scanner scanner = null;
		StringTokenizer st;
		String str, query;
		String name, birth_day, phone, grade, position, menu, price;
		int n, id = 0; // count는 id 만들때, n은 파일에 있는 튜플 수.

		try {			
			dc.setCommitMode(false);

			scanner = new Scanner(file);

			// 고객 정보 입력
			if (scanner.hasNext()) {

				query = "select count(*) from customer";
				rs = dc.executeQuery(query);
				rs.next();
				id = rs.getInt("count(*)");
				dc.closeStatementResultSet();
				
				id += 1000;
				str = scanner.nextLine();
				n = Integer.parseInt(str);
				int tpp = 0; // total_purchasing_price
				for (int i = 0; i < n; i++) {
					scanner.hasNext();
					str = scanner.nextLine();
					st = new StringTokenizer(str);

					name = st.nextToken();
					birth_day = st.nextToken();
					phone = st.nextToken();
					grade = st.nextToken();
					id++;
					
					if(grade.equals("Gold"))
						tpp = 1000000;
					else if(grade.equals("Silver"))
						tpp = 500000;
					else if(grade.equals("Bronze"))
						tpp = 300000;
					else tpp = 0;
					
					if(dc.tableUpdate_customer(id, name, birth_day, phone, tpp, grade) == -1){
						dc.closeStatementResultSet();
						return false;
					}
					dc.closeStatementResultSet();
				}
			}

			// 직원 정보 입력
			if (scanner.hasNextLine()) {

				query = "select count(*) from employee";
				rs = dc.executeQuery(query);
				rs.next();
				id = rs.getInt("count(*)");
				dc.closeStatementResultSet();
				
				id += 2000;				
				str = scanner.nextLine();
				n = Integer.parseInt(str);

				for (int i = 0; i < n; i++) {
					scanner.hasNext();
					str = scanner.nextLine();
					st = new StringTokenizer(str);

					name = st.nextToken();
					position = st.nextToken();

					id++;

					if(dc.tableUpdate_employee(id, name, position, 0) == -1){
						dc.closeStatementResultSet();
						return false;
					}
					dc.closeStatementResultSet();
					
					// 직원 정보 테이블에 삽입 이후, 계정 생성과 계정의 접근 권한 부여.
					if(create_account(name, Integer.toString(id))){
						System.out.println("'"+name+"' 계정 생성 완료..");
						grantPrivilegesToEmployee(name, position);
						System.out.println("'"+name+"' 계정에 권한 부여 완료..");
					
					}
					
				}
			}

			// 메뉴 정보 입력
			if (scanner.hasNextLine()) {

				str = scanner.nextLine();
				n = Integer.parseInt(str);

				for (int i = 0; i < n; i++) {
					scanner.hasNext();
					str = scanner.nextLine();
					st = new StringTokenizer(str);

					menu = st.nextToken();
					price = st.nextToken();

					if(dc.tableUpdate_menu(menu, Integer.parseInt(price)) == -1){
						dc.closeStatementResultSet();
						return false;
					}
					dc.closeStatementResultSet();
				} // end for
			} // end if		
			
			dc.commit();
			dc.setCommitMode(true);
			
			return true;

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se){
			se.printStackTrace();
			System.out.println(se.getMessage());
		}catch(NumberFormatException e){
			System.out.println(e.getMessage());
		}
		finally{
			scanner.close();
			initMenuPanel(); // 메뉴 패널 초기화
		}
		
		return false;
	}
	
	// 테이블 생성 메소드.
	private boolean create_db_tables() {

		String query;
		ResultSet rs = null;
		int n;

		try {
			// 총 다섯개의 테이블 생성. 트랜젝션으로 처리
			
			dc.setCommitMode(false);
			
			// customer 테이블 생성부
			query = "select count(*) from ALL_TABLES where TABLE_NAME = 'CUSTOMER'";
			rs = dc.executeQuery(query);
			rs.next();
			n = rs.getInt("count(*)");
			dc.closeStatementResultSet();
			
			if (n == 1) 
			{
				System.out.println("데이터베이스에  이미 customer table이  있으므로, 새로운 테이블 생성하지 않음..");
				return false;
			} else {
				
				query = "create table customer(	ID  varchar2(4), "
						+ "name  varchar2(20),"
						+ "birth_day  varchar2(10),"
						+ "phone  varchar2(20), "
						+ "total_purchasing_price	numeric(10, 0), "
						+ "grade  varchar2(10), "
						+ "primary key(ID, name))";
				
				if(dc.executeUpdate(query) == -1){
					System.out.println("customer table 생성 실패..");
					dc.closeStatementResultSet();
					return false;
				}

				System.out.println("customer table 생성 완료..");
				dc.closeStatementResultSet();
			}
			
			// employee 테이블 생성부
			query = "select count(*) from ALL_TABLES where TABLE_NAME = 'EMPLOYEE'";
			rs = dc.executeQuery(query);
			rs.next();
			n = rs.getInt("count(*)");
			dc.closeStatementResultSet();
			
			if ( n == 1) 
			{
				System.out.println("데이터베이스에 이미 employee table이 있으므로, 새로운 테이블 생성하지 않음..");
				return false;
			} else {
				query = "create table employee(	ID varchar(4), " + "name varchar(20) not null, "
						+ "position varchar(10) not null, "
						+ "total_performance	numeric(10, 0), primary key(ID, name))";
				
				if(dc.executeUpdate(query) == -1){
					System.out.println("employee table 생성 실패..");
					dc.closeStatementResultSet();
					return false;
				}

				System.out.println("employee table 생성 완료..");
				dc.closeStatementResultSet();
			}

			// menu 테이블 생성부
			query = "select count(*) from ALL_TABLES where TABLE_NAME = 'MENU'";
			rs = dc.executeQuery(query);
			rs.next();
			n = rs.getInt("count(*)");
			dc.closeStatementResultSet();
			if (n == 1)
			{
				System.out.println("데이터베이스에 이미 menu table이 있으므로, 새로운 테이블 생성하지 않음.");
				return false;
			} else {
				query = "create table menu( menu_name varchar(25), " 
						+ "price numeric(20, 0), "
						+ "primary key(menu_name))";
				
				if(dc.executeUpdate(query) == -1){
					System.out.println("menu table 생성 실패..");
					dc.closeStatementResultSet();
					return false;
				}
				
				System.out.println("menu table 생성 완료");
				dc.closeStatementResultSet();
			}

			// sales 테이블 생성부
			query = "select count(*) from ALL_TABLES where TABLE_NAME = 'SALES'";
			rs = dc.executeQuery(query);
			rs.next();
			n = rs.getInt("count(*)");
			dc.closeStatementResultSet();
			if (n == 1) 
			{
				System.out.println("데이터베이스에 이미 sales table이 있으므로, 새로운 테이블 생성하지 않음.");
				return false;
			} else {
				
				query = "alter session set NLS_DATE_FORMAT='YYYY-MM-DD'";
				
				if(dc.executeUpdate(query) == -1){
					System.out.println("DB 데이터 포맷 변경 실패..");
					dc.closeStatementResultSet();
					return false;
				}
				dc.closeStatementResultSet();				
				
				query = "create table sales( ID varchar2(4), name varchar2(20), "
						+ "menu_name  varchar2(25), "
						+ "order_date varchar2(15), "
						+ "hour numeric(2, 0), minute numeric(2, 0), "
						+ "foreign key(menu_name) references menu, "
						+ "foreign key(ID, name) references customer )";
				
				if(dc.executeUpdate(query) == -1){
					System.out.println("menu table 생성 실패..");
					dc.closeStatementResultSet();
					return false;
				}
				
				System.out.println("sales table 생성 완료");
				dc.closeStatementResultSet();
			}
			
			dc.commit();
			dc.setCommitMode(true);			
			
			return true;

		} catch (Exception e) {
			
			System.out.println(e.getStackTrace());
			System.out.println(e.getMessage());
			System.out.println("테이블 생성 실패");
		}
		
		return false;
	}
		
	private boolean create_synonyms()
	{
		String query;
		String curUserIDConnectedToDB = dc.getCurUserIDConnectedToDB();
		
		dc.setCommitMode(false);
		
		query = "create public synonym customer for "
				+ curUserIDConnectedToDB
				+ ".customer";
		
		if(dc.executeUpdate(query) == -1){
			System.out.println("creating public synonym customer for 'userName'.customer failed");
			return false;
		}
		query = "create public synonym employee for "
				+ curUserIDConnectedToDB
				+ ".employee";
		
		if(dc.executeUpdate(query) == -1){
			System.out.println("creating synonym employee for 'userName'.employee failed");
			return false;
		}
		
		query = "create public synonym menu for "
				+ curUserIDConnectedToDB
				+ ".menu";
		
		if(dc.executeUpdate(query) == -1){
			System.out.println("creating synonym menu for 'userName'.menu failed");
			return false;
		}
		
		query = "create public synonym sales for "
				+ curUserIDConnectedToDB
				+ ".sales";
		
		if(dc.executeUpdate(query) == -1){
			System.out.println("creating synonym sales for 'sales'.customer failed");
			return false;
		}
		
		dc.commit();
		dc.setCommitMode(true);
		return true;
	}
	 
	private boolean create_account(String name, String pass){
		String query;
		
		query = "create user "
				+ name
				+ " identified by "
				+ pass;
		
		if(dc.executeUpdate(query) == -1){
			System.out.println("creating account '"+name+"' failed");
			dc.closeStatementResultSet();
			return false;
		}
		
		dc.closeStatementResultSet();
		return true;
	}
		
	private boolean grantPrivilegesToEmployee(String name, String position)
	{
		String query;
		
		
		// grant connect, resource
		query = "grant connect, resource to "
				+ name;		
		if(dc.executeUpdate(query) == -1 ){
			System.out.println("graning connect, resource to "+name+" failed");
			dc.closeStatementResultSet();
			return false;
		}
		dc.closeStatementResultSet();
		
		// other privileges
		if(position.equals("Supervisor")){
				
			query = "grant dba to "
						+ name;
			
			if(dc.executeUpdate(query) == -1){
				System.out.println("granting privileges to "+name+" failed");
				dc.closeStatementResultSet();
				return false;
			}		
			dc.closeStatementResultSet();
			
		}
		else if(position.equals("Staff")){
			
			dc.setCommitMode(false);
			
			// grant select on customer
			query = "grant select on customer to "
					+ name;			
			if(dc.executeUpdate(query) == -1){
				System.out.println("grating select on customer to "+name+" failed");
				dc.closeStatementResultSet();
				return false;
			}
			dc.closeStatementResultSet();
			
			//grant select on employee
			query = "grant select on employee to "
					+ name;			
			if(dc.executeUpdate(query) == -1){
				System.out.println("grating select on employee to "+name+" failed");
				dc.closeStatementResultSet();
				return false;
			}
			dc.closeStatementResultSet();
			
			//grant select on menu
			query = "grant select on menu to "
					+ name;			
			if(dc.executeUpdate(query) == -1){
				System.out.println("grating select on menu to "+name+" failed");
				dc.closeStatementResultSet();
				return false;
			}			
			dc.closeStatementResultSet();
			
			//grant update on customer
			query = "grant update on customer to "
					+ name;			
			if(dc.executeUpdate(query) == -1){
				System.out.println("grating update on customer to "+name+" failed");
				dc.closeStatementResultSet();
				return false;
			}
			dc.closeStatementResultSet();			
						
			//grant insert on sales
			query = "grant insert on sales to "
					+ name;			
			if(dc.executeUpdate(query) == -1){
				System.out.println("grating insert on sales to "+name+" failed");
				dc.closeStatementResultSet();
				return false;
			}
			dc.closeStatementResultSet();
			
			query = "grant update on employee to "
					+ name;			
			if(dc.executeUpdate(query) == -1){
				System.out.println("grating update on employee to "+name+" failed");
				dc.closeStatementResultSet();
				return false;
			}
			dc.closeStatementResultSet();
			
			dc.commit();
			dc.setCommitMode(true);			
		}
		
		return true;
	}
	
	public boolean log_in(String id, String pass)
	{
		if(dc.setDBConnection(id, pass))
		{
			log_in = true; // 로그인 성공
			this.eiController.setTextAreaEmpty();
			return true;  
		}
		
		return false; //로그인 실패
	}
	
	// 현재 디비에 로그인 되어 있나 안되어 있나, 체크하는 메소드
	public boolean getLoginStatus()
	{
		return log_in;
	}
	
	public void setTemporaryOrderOnOrderStatusPanel(String menuName, int price)
	{
		osController.setTemporaryOrder(menuName, price);
	}	
	
	public boolean setTableTurnOn(String tableNumber)
	{
		return this.tsController.setTableOn(tableNumber);
	}
	
	public boolean setTableTurnOff(String tableNumber)
	{
		return this.tsController.setTableOff(tableNumber);
	}

	public boolean isTurnedOn(String tableNumber)
	{
		return this.tsController.isTurnedOn(tableNumber);
	}
	
	public int executeUpdate(String query){
		int n = -1;
		n = dc.executeUpdate(query);
		dc.closeStatementResultSet();			
		return n;
	}
	
	public ResultSet executeQuery(String query)
	{
		ResultSet rs = null;
		try {
			rs =  dc.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public int insertTupleToSales(int id, String name, String menuName, int year,
							int month, int day,	int hour, int minute){
		int n = -1;
		n = dc.insertTupleToSales(id, name, menuName, year, month, day, hour, minute);
		dc.closeStatementResultSet();
		
		return n;
	}
	
	public int updateTotalPurchasingPrice(String custName, int tpp)
	{
		int n = -1;
		n = dc.updateTotalPurchasingPrice(custName, tpp);
		dc.closeStatementResultSet();
		return n;
	}
	
	public int updateGrade(String custName, String grade)
	{
		int n = -1;
		n = dc.updateGrade(custName, grade);
		dc.closeStatementResultSet();
		return n;
	}
	
	public int updateTotalPerformance(int total_price)
	{	
		int n;
		n = dc.updateTotalPerformance(total_price);
		dc.closeStatementResultSet();
		return n;
	}	
	
	public String getCurrentIDConnectedToDB()
	{
		return dc.getCurUserIDConnectedToDB();
	}			
	
	public ResultSet getTPPAndIdFromDB(String custName)
	{
		return dc.getTPPAndIdFromDB(custName);
	}
	
	public void closeStatementResultSet()
	{
		dc.closeStatementResultSet();
	}
	
	public int enrollCustomer(String name, String birth_day, String phone)
	{
		int id = 0, n = -1;
		String query;
		ResultSet rs = null;
		
		query = "select count(*) from customer";
		try {
			rs = dc.executeQuery(query);
			rs.next();
			id = rs.getInt("count(*)") + 1001;
			dc.closeStatementResultSet();
		} catch (SQLException e) {
			dc.closeStatementResultSet();
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		n = dc.tableUpdate_customer(id, name, birth_day, phone, 0, "Normal");
		dc.closeStatementResultSet();
		return n;
	}
	
	public int enrollEmployee(String name, String position)
	{
		int id = 0, n = -1;
		String query;
		ResultSet rs = null;
		
		query = "select count(*) from employee";
		try {
			rs = dc.executeQuery(query);
			rs.next();
			id = rs.getInt("count(*)") + 2001;
			dc.closeStatementResultSet();
		} catch (SQLException e) {
			dc.closeStatementResultSet();
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		n = dc.tableUpdate_employee(id, name, position, 0);
		dc.closeStatementResultSet();
		
		// 새로 생성한 직원 정보로 계정생성 및 권한 부여
		if(n > -1){
			if(create_account(name, Integer.toString(id))){
				grantPrivilegesToEmployee(name, position);
			}
		}
		return n;
	}
	
	public int addMenu(String name, String price)
	{
		int n = -1;
		
		n = dc.tableUpdate_menu(name, Integer.parseInt(price));
		dc.closeStatementResultSet();
		
		return n;
	}
	
	
	// 이하 6개의 메소드는 초기화 담당.
	// 이 밑부터는 초기화 메소드
	public DB_Interface getDBInterface()
	{
		return this;
	}
		
	public void setMenuBarController(MenuBarControll con)
	{
		this.mbController = con;
	}
	
	public void setMenuController(MenuControll con)
	{
		this.mController = con;
	}
	
	public void setEnrollInquiryController(Enroll_inquiryControll con)
	{
		this.eiController = con;
	}
	
	public void setOrderStatusController(OrderStatusControll con)
	{
		this.osController = con;
	}	
	
	public void setTableStatusController(TableStatusControll con)
	{
		this.tsController = con;
	}
	
}
