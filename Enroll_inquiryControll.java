import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Enroll_inquiryControll implements ActionListener{

	private JTabbedPane tabbedPane; 
	private JPanel custPanel;
	private JPanel salesPanel;
	private JPanel emploPanel;
	private JPanel menuPanel;	
	
	private DB_Interface di;
	private UI_Calendar cal;
	
	Enroll_inquiryControll(JTabbedPane tabbedPane, JPanel panel1, JPanel panel2,
			JPanel panel3, JPanel panel4)
	{		
		this.tabbedPane = tabbedPane;		
		this.custPanel = panel1;
		this.salesPanel = panel2;
		this.emploPanel = panel3;
		this.menuPanel = panel4;	
		
		this.cal = new UI_Calendar();
		this.cal.setController(this);
		
		System.out.println("Enrollment_inquiry 컨트롤러 생성 완료..");
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == (JButton)custPanel.getComponent(2)) // 고객 등록
		{
			enrollCustomer();
			
		}else if(e.getSource() == (JButton)custPanel.getComponent(3)){ // 고객 조회
			inquiryCustomer();
			
		}else if(e.getSource() == (JButton)salesPanel.getComponent(1)){ // 매출
			
			cal.showCalendar(true);
			 
		}else if(e.getSource() == (JButton)emploPanel.getComponent(2)){ // 직원 등록
			enrollEmployee();
			
		}else if(e.getSource() == (JButton)emploPanel.getComponent(3)){ // 직원 조회
			inquiryEmployee();
			
		}else if(e.getSource() == (JButton)menuPanel.getComponent(2)){ // 메뉴 등록
			String query;
			ResultSet rs = null;
			query = "select count(*) from menu";
			rs = di.executeQuery(query);
			try {
				rs.next();
				if(rs.getInt("count(*)") >= 20){
					JOptionPane warning = new JOptionPane();
					warning.showMessageDialog(null, "메뉴는 최대 20개 까지 등록할 수 있습니다.", "등록 실패",
							warning.WARNING_MESSAGE);
				}else{
					addMenu();
				}
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		
			
		}else if(e.getSource() == (JButton)menuPanel.getComponent(3)){ //메뉴 조회
			inquiryMenu();
			
		}else{
			System.out.println("erorr");			
		}		
	}
	
	private void enrollCustomer()
	{
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("회원등록");
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 100));
		panel.setLayout(new GridLayout(5, 2, 25, 25));
		
		JLabel name = new JLabel("고객명");
		JLabel birthDay = new JLabel("생일(4자리)");
		JLabel phone = new JLabel("연락처");		
		JTextField name_field = new JTextField();
		JTextField birth_field = new JTextField();
		JTextField phone_field = new JTextField();
		JButton btn1 = new JButton("가입신청");
		JButton btn2 = new JButton("취소");	
		
		panel.add(name);
		panel.add(name_field);
		panel.add(birthDay);
		panel.add(birth_field);
		panel.add(phone);
		panel.add(phone_field);	
		panel.add(btn1);
		panel.add(btn2);
		 
		JPanel emptyPanel1 = new JPanel();
		JPanel emptyPanel2 = new JPanel();
		JPanel emptyPanel3 = new JPanel();
		emptyPanel1.setPreferredSize(new Dimension(30, 10));
		emptyPanel2.setPreferredSize(new Dimension(30, 10));
		emptyPanel3.setPreferredSize(new Dimension(50, 30));
		frame.add(panel, BorderLayout.CENTER);
		frame.add(emptyPanel3, BorderLayout.PAGE_START);
		frame.add(emptyPanel1, BorderLayout.LINE_START);
		frame.add(emptyPanel2, BorderLayout.LINE_END);
		frame.setSize(300, 300);
		frame.setVisible(true);
	
		
		btn1.addActionListener(new ActionListener(){

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String name = name_field.getText();
				String birth = birth_field.getText();
				String phone = phone_field.getText();
				String query;
				ResultSet rs = null;
				
				// 이름이 DB에 있는지 체크.
				query = "select count(*) from customer where name = '"
						+ name
						+ "'";
				rs = di.executeQuery(query);
				try {
					
					rs.next();
					if(rs.getInt("count(*)") == 1)
					{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "이미 등록된 회원입니다.", "등록 실패",
								warning.WARNING_MESSAGE);
						di.closeStatementResultSet();
					}		
					
				} catch (SQLException e1) {
					di.closeStatementResultSet();
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}
				di.closeStatementResultSet();
				
				// birth가 4자리인지 체크	
				if(birth.length() != 4)
				{
					JOptionPane warning = new JOptionPane();
					warning.showMessageDialog(null, "4자리의 생일을 입력해주십시오.", "등록 실패",
							warning.WARNING_MESSAGE);
				}
				
				// 인서트 쿼리
				if(di.enrollCustomer(name, birth, phone) > -1)
				{
					frame.dispose();
					JOptionPane success = new JOptionPane();
					success.showMessageDialog(null, "등록 완료.", "확인",
							success.DEFAULT_OPTION);
				}	
			}			
		});		
		btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
			
		});
	}
	
	@SuppressWarnings("static-access")
	private void inquiryCustomer()
	{
		 String name = ((JTextField)this.custPanel.getComponent(1)).getText();
		 String query;
		 ResultSet rs = null;
		 
		 //name이 공백이라면
		 if(name.equals("")){
			 JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "이름을 입력하십시오.", "등록 실패",
						warning.WARNING_MESSAGE);
				
			return;
		 }
		 
		 
		 // name이 DB에 있는지 체크
		 query = "select count(*) from customer where name = '"
					+ name
					+ "'";
			rs = di.executeQuery(query);
			try {
				
				rs.next();
				if(rs.getInt("count(*)") == 0)
				{
					JOptionPane warning = new JOptionPane();
					warning.showMessageDialog(null, "등록되지 않은 회원입니다.", "등록 실패",
							warning.WARNING_MESSAGE);
					di.closeStatementResultSet();
					return;
				}		
				
			} catch (SQLException e1) {
				di.closeStatementResultSet();
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			}
			di.closeStatementResultSet();
			
		 // 있으면 텍스트필드에 정보 출력
			((JTextArea)this.custPanel.getComponent(4)).setText("");
			query = "select * from customer where name = '"
					+ name
					+ "'";
			rs = di.executeQuery(query);
			try {
				rs.next();
				((JTextArea)this.custPanel.getComponent(4)).append("\n   고 객 명 : " 
													+ rs.getString("name")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   고 객 ID: " 
													+ rs.getString("id")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   생     일 : " 
													+ rs.getString("name")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   전화번호 : " 
													+ rs.getString("phone")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   고객등급 : " 
													+ rs.getString("grade")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   총구매액 : " 
													+ Integer.toString(rs.getInt("total_purchasing_price"))
													+"\n");
				
				di.closeStatementResultSet();
			} catch (SQLException e) {
				di.closeStatementResultSet();
				e.printStackTrace();
			}
			
	}
	
	public void inquirySales(int year, int month, int day) {
				
		((JButton) this.salesPanel.getComponent(1)).setText(year + " - " + month + " - " + day);
		((JButton) this.salesPanel.getComponent(1)).setFont(new Font("Arial", Font.PLAIN, 25));
		String query;
		ResultSet rs = null;		
		
		// 등급 체크
		String curLoginName = di.getCurrentIDConnectedToDB();
		query = "select position from employee where name = '"
				+ curLoginName
				+ "'";
		rs = di.executeQuery(query);
		try {
			rs.next();
			if(rs.getString("position").equals("Staff"))
			{
				JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "매출 정보는 supervisor만 조회할 수 있습니다.", "warning",
						warning.WARNING_MESSAGE);
				di.closeStatementResultSet();
				cal.showCalendar(false);
				return;
			}
		} catch (SQLException e1) {
			di.closeStatementResultSet();
		}
		di.closeStatementResultSet();			
		
		
		((JTextArea) this.salesPanel.getComponent(2)).setText("");
		
		// 일 매출
		query = "select sum(price) " + "from sales natural join menu " 
				+"where order_date = to_date('"
				+ Integer.toString(year)
				+ "-"
				+ Integer.toString(month)
				+ "-"
				+ Integer.toString(day)
				+ "', 'YYYY-MM-DD')"; 		
		rs = di.executeQuery(query);
		try {
			rs.next();
			((JTextArea) this.salesPanel.getComponent(2))
					.append("\n   일 매출 : " + Integer.toString(rs.getInt("sum(price)")));
			di.closeStatementResultSet();
		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}

		((JTextArea) this.salesPanel.getComponent(2)).append("\n  ====================");

		// 당일 가장 많이 팔린 메뉴
		query = "select menu_name from ( select menu_name, count(*) as count "
				+ "from sales natural join menu " 
				+"where order_date = to_date('"
				+ Integer.toString(year)
				+ "-"
				+ Integer.toString(month)
				+ "-"
				+ Integer.toString(day)
				+ "', 'YYYY-MM-DD')"
				+ " group by menu_name order by count(*) desc ) " + "where rownum = 1";

		rs = di.executeQuery(query);
		try {
			rs.next();
			((JTextArea) this.salesPanel.getComponent(2))
					.append("\n   가장 많이 팔린 메뉴\n : " + rs.getString("menu_name"));
			di.closeStatementResultSet();

		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}

		((JTextArea) this.salesPanel.getComponent(2)).append("\n");

		// 당일 가장 적게 팔린 메뉴
		query = "select menu_name from ( select menu_name, count(*) as count "
				+ "from sales natural join menu "
				+"where order_date = to_date('"
				+ Integer.toString(year)
				+ "-"
				+ Integer.toString(month)
				+ "-"
				+ Integer.toString(day)
				+ "','YYYY-MM-DD')"
				+ " group by menu_name order by count(*) asc) " + "where rownum = 1";

		rs = di.executeQuery(query);
		try {
			rs.next();
			((JTextArea) this.salesPanel.getComponent(2))
					.append("\n   가장 적게 팔린 메뉴\n : " + rs.getString("menu_name"));
			di.closeStatementResultSet();

		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}

		((JTextArea) this.salesPanel.getComponent(2)).append("\n  ====================");

		// 누적 매출
		query = "select sum(price) from menu natural join sales "
				+ "where order_date <= to_date('"
				+ Integer.toString(year)
				+ "-"
				+ Integer.toString(month)
				+ "-"
				+ Integer.toString(day)
				+ "' ,'YYYY-MM-DD')";
		rs = di.executeQuery(query);
		try {
			rs.next();
			((JTextArea) this.salesPanel.getComponent(2))
			.append("\n   누적 매출 : " + rs.getString("sum(price)"));
			di.closeStatementResultSet();
			
		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}
		this.cal.showCalendar(false);
	}
	
	private void enrollEmployee()
	{
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("직원등록");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 25, 25));
		JLabel name = new JLabel("직원명");		
		JLabel position = new JLabel("직급");
		JTextField name_field = new JTextField();
		String[] str = {"Supervisor", "Staff"};
		JComboBox<String> combobox = new JComboBox<String>(str);
		JButton enroll = new JButton("등록");
		JButton cancel = new JButton("취소");
		panel.add(name);
		panel.add(name_field);
		panel.add(position);
		panel.add(combobox);
		panel.add(enroll);
		panel.add(cancel);
		
		JPanel emptyPanel1 = new JPanel();
		JPanel emptyPanel2 = new JPanel();
		JPanel emptyPanel3 = new JPanel();
		JPanel emptyPanel4 = new JPanel();
		emptyPanel1.setPreferredSize(new Dimension(0, 30)); // top
		emptyPanel2.setPreferredSize(new Dimension(30, 0)); // left
		emptyPanel3.setPreferredSize(new Dimension(30, 0)); // right
		emptyPanel4.setPreferredSize(new Dimension(0, 30)); // bottom
		
		frame.add(emptyPanel1, BorderLayout.PAGE_START);
		frame.add(emptyPanel2, BorderLayout.LINE_START);
		frame.add(emptyPanel3, BorderLayout.LINE_END);
		frame.add(emptyPanel4, BorderLayout.PAGE_END);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setSize(300, 250);
		
		enroll.addActionListener(new ActionListener(){

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = name_field.getText();
				String position = (String)combobox.getSelectedItem();
				String query;
				ResultSet rs = null;
				
				// 이름이 DB에 있는지 체크.
				query = "select count(*) from employee where name = '"
						+ name
						+ "'";
				rs = di.executeQuery(query);
				try {
					
					rs.next();
					if(rs.getInt("count(*)") == 1)
					{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "이미 등록된 직원입니다.", "등록 실패",
								warning.WARNING_MESSAGE);
						di.closeStatementResultSet();
					}		
					
				} catch (SQLException e1) {
					di.closeStatementResultSet();
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}
				di.closeStatementResultSet();
				
				// 인서트 쿼리
				if(di.enrollEmployee(name, position) > -1)
				{
					frame.dispose();
					JOptionPane success = new JOptionPane();
					success.showMessageDialog(null, "등록 완료.", "확인",
							success.DEFAULT_OPTION);
				}
			}
			
		});
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
			
		});
		
	}
	
	@SuppressWarnings("static-access")
	private void inquiryEmployee()
	{

		String name = ((JTextField) this.emploPanel.getComponent(1)).getText();
		String query;
		ResultSet rs = null;

		// 공백 확인
		if (name.equals("")) {
			JOptionPane warning = new JOptionPane();
			warning.showMessageDialog(null, "이름을 입력하십시오.", "등록 실패", warning.WARNING_MESSAGE);
			return;
		}

		// name이 DB에 있는지 체크
		query = "select count(*) from employee where name = '" + name + "'";
		rs = di.executeQuery(query);
		try {

			rs.next();
			if (rs.getInt("count(*)") == 0) {
				JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "등록되지 않은 직원입니다.", "등록 실패", warning.WARNING_MESSAGE);
				di.closeStatementResultSet();
			}

		} catch (SQLException e1) {
			di.closeStatementResultSet();
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		di.closeStatementResultSet();

		// 있으면 텍스트필드에 정보 출력
		((JTextArea) this.emploPanel.getComponent(4)).setText("");
		query = "select * from employee where name = '" + name + "'";
		rs = di.executeQuery(query);
		try {
			rs.next();
			((JTextArea) this.emploPanel.getComponent(4))
					.append("\n   직원명 : " + rs.getString("name") + "\n");
			((JTextArea) this.emploPanel.getComponent(4))
					.append("   직  급 : " + rs.getString("position") + "\n");
			((JTextArea) this.emploPanel.getComponent(4))
					.append("   총실적 : " + Integer.toString(rs.getInt("total_performance"))
					+ "\n");
			
			di.closeStatementResultSet();
		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}
	}

	private void addMenu()
	{
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("메뉴등록");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 25, 25));
		JLabel name = new JLabel("메뉴명");		
		JLabel price = new JLabel("가격");
		JTextField name_field = new JTextField();
		JTextField price_field = new JTextField();
		JButton enroll = new JButton("등록");
		JButton cancel = new JButton("취소");
		panel.add(name);
		panel.add(name_field);
		panel.add(price);
		panel.add(price_field);
		panel.add(enroll);
		panel.add(cancel);
		
		JPanel emptyPanel1 = new JPanel();
		JPanel emptyPanel2 = new JPanel();
		JPanel emptyPanel3 = new JPanel();
		JPanel emptyPanel4 = new JPanel();
		emptyPanel1.setPreferredSize(new Dimension(0, 30)); // top
		emptyPanel2.setPreferredSize(new Dimension(30, 0)); // left
		emptyPanel3.setPreferredSize(new Dimension(30, 0)); // right
		emptyPanel4.setPreferredSize(new Dimension(0, 30)); // bottom
		
		frame.add(emptyPanel1, BorderLayout.PAGE_START);
		frame.add(emptyPanel2, BorderLayout.LINE_START);
		frame.add(emptyPanel3, BorderLayout.LINE_END);
		frame.add(emptyPanel4, BorderLayout.PAGE_END);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setSize(300, 250);
		
		enroll.addActionListener(new ActionListener(){

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = name_field.getText();
				String price = price_field.getText();
				String query;
				ResultSet rs = null;
				
				// 이름이 DB에 있는지 체크.
				query = "select count(*) from menu where menu_name = '"
						+ name
						+ "'";
				rs = di.executeQuery(query);
				try {
					
					rs.next();
					if(rs.getInt("count(*)") == 1)
					{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "이미 등록된 메뉴입니다.", "등록 실패",
								warning.WARNING_MESSAGE);
						di.closeStatementResultSet();
					}		
					
				} catch (SQLException e1) {
					di.closeStatementResultSet();
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}
				di.closeStatementResultSet();
				
				// 인서트 쿼리
				if(di.addMenu(name, price) > -1)
				{
					frame.dispose();
					JOptionPane success = new JOptionPane();
					success.showMessageDialog(null, "등록 완료.", "확인",
							success.DEFAULT_OPTION);
					di.initMenuPanel();
				}
			}
			
		});
		
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
			
		});
		
	}
	
	@SuppressWarnings("static-access")
	private void inquiryMenu()
	{
		String name = ((JTextField) this.menuPanel.getComponent(1)).getText();
		String query;
		ResultSet rs = null;

		// 공백 체크
		
		 if(name.equals("")){
			 JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "메뉴명을 입력하십시오.", "등록 실패",
						warning.WARNING_MESSAGE);
				
				return;
		 }
		
		// name이 DB에 있는지 체크
		query = "select count(*) from menu where menu_name = '" + name + "'";
		rs = di.executeQuery(query);
		try {

			rs.next();
			if (rs.getInt("count(*)") == 0) {
				JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "등록되지 않은 메뉴입니다.", "조회 실패", warning.WARNING_MESSAGE);
				di.closeStatementResultSet();
			}

		} catch (SQLException e1) {
			di.closeStatementResultSet();
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		di.closeStatementResultSet();

		// 있으면 텍스트필드에 정보 출력
		((JTextArea) this.menuPanel.getComponent(4)).setText("");
		query = "select * from menu where menu_name = '" + name + "'";
		rs = di.executeQuery(query);
		try {
			rs.next();
			((JTextArea) this.menuPanel.getComponent(4))
					.append("\n   메뉴명 : " + rs.getString("menu_name") + "\n");
			((JTextArea) this.menuPanel.getComponent(4))
					.append("   가  격 : " + rs.getString("price") + "\n");
			
			
			di.closeStatementResultSet();
		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}}

	public void setTextAreaEmpty()
	{
		((JTextArea) this.salesPanel.getComponent(2)).setText("");
	}
	
	
	public void setDBInterface(DB_Interface di)
	{
		this.di = di;
	}
	
}
