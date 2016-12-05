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
		
		System.out.println("Enrollment_inquiry ��Ʈ�ѷ� ���� �Ϸ�..");
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == (JButton)custPanel.getComponent(2)) // �� ���
		{
			enrollCustomer();
			
		}else if(e.getSource() == (JButton)custPanel.getComponent(3)){ // �� ��ȸ
			inquiryCustomer();
			
		}else if(e.getSource() == (JButton)salesPanel.getComponent(1)){ // ����
			
			cal.showCalendar(true);
			 
		}else if(e.getSource() == (JButton)emploPanel.getComponent(2)){ // ���� ���
			enrollEmployee();
			
		}else if(e.getSource() == (JButton)emploPanel.getComponent(3)){ // ���� ��ȸ
			inquiryEmployee();
			
		}else if(e.getSource() == (JButton)menuPanel.getComponent(2)){ // �޴� ���
			String query;
			ResultSet rs = null;
			query = "select count(*) from menu";
			rs = di.executeQuery(query);
			try {
				rs.next();
				if(rs.getInt("count(*)") >= 20){
					JOptionPane warning = new JOptionPane();
					warning.showMessageDialog(null, "�޴��� �ִ� 20�� ���� ����� �� �ֽ��ϴ�.", "��� ����",
							warning.WARNING_MESSAGE);
				}else{
					addMenu();
				}
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		
			
		}else if(e.getSource() == (JButton)menuPanel.getComponent(3)){ //�޴� ��ȸ
			inquiryMenu();
			
		}else{
			System.out.println("erorr");			
		}		
	}
	
	private void enrollCustomer()
	{
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("ȸ�����");
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 100));
		panel.setLayout(new GridLayout(5, 2, 25, 25));
		
		JLabel name = new JLabel("����");
		JLabel birthDay = new JLabel("����(4�ڸ�)");
		JLabel phone = new JLabel("����ó");		
		JTextField name_field = new JTextField();
		JTextField birth_field = new JTextField();
		JTextField phone_field = new JTextField();
		JButton btn1 = new JButton("���Խ�û");
		JButton btn2 = new JButton("���");	
		
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
				
				// �̸��� DB�� �ִ��� üũ.
				query = "select count(*) from customer where name = '"
						+ name
						+ "'";
				rs = di.executeQuery(query);
				try {
					
					rs.next();
					if(rs.getInt("count(*)") == 1)
					{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "�̹� ��ϵ� ȸ���Դϴ�.", "��� ����",
								warning.WARNING_MESSAGE);
						di.closeStatementResultSet();
					}		
					
				} catch (SQLException e1) {
					di.closeStatementResultSet();
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}
				di.closeStatementResultSet();
				
				// birth�� 4�ڸ����� üũ	
				if(birth.length() != 4)
				{
					JOptionPane warning = new JOptionPane();
					warning.showMessageDialog(null, "4�ڸ��� ������ �Է����ֽʽÿ�.", "��� ����",
							warning.WARNING_MESSAGE);
				}
				
				// �μ�Ʈ ����
				if(di.enrollCustomer(name, birth, phone) > -1)
				{
					frame.dispose();
					JOptionPane success = new JOptionPane();
					success.showMessageDialog(null, "��� �Ϸ�.", "Ȯ��",
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
		 
		 //name�� �����̶��
		 if(name.equals("")){
			 JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "�̸��� �Է��Ͻʽÿ�.", "��� ����",
						warning.WARNING_MESSAGE);
				
			return;
		 }
		 
		 
		 // name�� DB�� �ִ��� üũ
		 query = "select count(*) from customer where name = '"
					+ name
					+ "'";
			rs = di.executeQuery(query);
			try {
				
				rs.next();
				if(rs.getInt("count(*)") == 0)
				{
					JOptionPane warning = new JOptionPane();
					warning.showMessageDialog(null, "��ϵ��� ���� ȸ���Դϴ�.", "��� ����",
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
			
		 // ������ �ؽ�Ʈ�ʵ忡 ���� ���
			((JTextArea)this.custPanel.getComponent(4)).setText("");
			query = "select * from customer where name = '"
					+ name
					+ "'";
			rs = di.executeQuery(query);
			try {
				rs.next();
				((JTextArea)this.custPanel.getComponent(4)).append("\n   �� �� �� : " 
													+ rs.getString("name")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   �� �� ID: " 
													+ rs.getString("id")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   ��     �� : " 
													+ rs.getString("name")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   ��ȭ��ȣ : " 
													+ rs.getString("phone")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   ����� : " 
													+ rs.getString("grade")+"\n");
				((JTextArea)this.custPanel.getComponent(4)).append("   �ѱ��ž� : " 
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
		
		// ��� üũ
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
				warning.showMessageDialog(null, "���� ������ supervisor�� ��ȸ�� �� �ֽ��ϴ�.", "warning",
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
		
		// �� ����
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
					.append("\n   �� ���� : " + Integer.toString(rs.getInt("sum(price)")));
			di.closeStatementResultSet();
		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}

		((JTextArea) this.salesPanel.getComponent(2)).append("\n  ====================");

		// ���� ���� ���� �ȸ� �޴�
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
					.append("\n   ���� ���� �ȸ� �޴�\n : " + rs.getString("menu_name"));
			di.closeStatementResultSet();

		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}

		((JTextArea) this.salesPanel.getComponent(2)).append("\n");

		// ���� ���� ���� �ȸ� �޴�
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
					.append("\n   ���� ���� �ȸ� �޴�\n : " + rs.getString("menu_name"));
			di.closeStatementResultSet();

		} catch (SQLException e) {
			di.closeStatementResultSet();
			System.out.println(e.getMessage());
		}

		((JTextArea) this.salesPanel.getComponent(2)).append("\n  ====================");

		// ���� ����
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
			.append("\n   ���� ���� : " + rs.getString("sum(price)"));
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
		frame.setTitle("�������");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 25, 25));
		JLabel name = new JLabel("������");		
		JLabel position = new JLabel("����");
		JTextField name_field = new JTextField();
		String[] str = {"Supervisor", "Staff"};
		JComboBox<String> combobox = new JComboBox<String>(str);
		JButton enroll = new JButton("���");
		JButton cancel = new JButton("���");
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
				
				// �̸��� DB�� �ִ��� üũ.
				query = "select count(*) from employee where name = '"
						+ name
						+ "'";
				rs = di.executeQuery(query);
				try {
					
					rs.next();
					if(rs.getInt("count(*)") == 1)
					{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "�̹� ��ϵ� �����Դϴ�.", "��� ����",
								warning.WARNING_MESSAGE);
						di.closeStatementResultSet();
					}		
					
				} catch (SQLException e1) {
					di.closeStatementResultSet();
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}
				di.closeStatementResultSet();
				
				// �μ�Ʈ ����
				if(di.enrollEmployee(name, position) > -1)
				{
					frame.dispose();
					JOptionPane success = new JOptionPane();
					success.showMessageDialog(null, "��� �Ϸ�.", "Ȯ��",
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

		// ���� Ȯ��
		if (name.equals("")) {
			JOptionPane warning = new JOptionPane();
			warning.showMessageDialog(null, "�̸��� �Է��Ͻʽÿ�.", "��� ����", warning.WARNING_MESSAGE);
			return;
		}

		// name�� DB�� �ִ��� üũ
		query = "select count(*) from employee where name = '" + name + "'";
		rs = di.executeQuery(query);
		try {

			rs.next();
			if (rs.getInt("count(*)") == 0) {
				JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "��ϵ��� ���� �����Դϴ�.", "��� ����", warning.WARNING_MESSAGE);
				di.closeStatementResultSet();
			}

		} catch (SQLException e1) {
			di.closeStatementResultSet();
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		di.closeStatementResultSet();

		// ������ �ؽ�Ʈ�ʵ忡 ���� ���
		((JTextArea) this.emploPanel.getComponent(4)).setText("");
		query = "select * from employee where name = '" + name + "'";
		rs = di.executeQuery(query);
		try {
			rs.next();
			((JTextArea) this.emploPanel.getComponent(4))
					.append("\n   ������ : " + rs.getString("name") + "\n");
			((JTextArea) this.emploPanel.getComponent(4))
					.append("   ��  �� : " + rs.getString("position") + "\n");
			((JTextArea) this.emploPanel.getComponent(4))
					.append("   �ѽ��� : " + Integer.toString(rs.getInt("total_performance"))
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
		frame.setTitle("�޴����");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 25, 25));
		JLabel name = new JLabel("�޴���");		
		JLabel price = new JLabel("����");
		JTextField name_field = new JTextField();
		JTextField price_field = new JTextField();
		JButton enroll = new JButton("���");
		JButton cancel = new JButton("���");
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
				
				// �̸��� DB�� �ִ��� üũ.
				query = "select count(*) from menu where menu_name = '"
						+ name
						+ "'";
				rs = di.executeQuery(query);
				try {
					
					rs.next();
					if(rs.getInt("count(*)") == 1)
					{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "�̹� ��ϵ� �޴��Դϴ�.", "��� ����",
								warning.WARNING_MESSAGE);
						di.closeStatementResultSet();
					}		
					
				} catch (SQLException e1) {
					di.closeStatementResultSet();
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}
				di.closeStatementResultSet();
				
				// �μ�Ʈ ����
				if(di.addMenu(name, price) > -1)
				{
					frame.dispose();
					JOptionPane success = new JOptionPane();
					success.showMessageDialog(null, "��� �Ϸ�.", "Ȯ��",
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

		// ���� üũ
		
		 if(name.equals("")){
			 JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "�޴����� �Է��Ͻʽÿ�.", "��� ����",
						warning.WARNING_MESSAGE);
				
				return;
		 }
		
		// name�� DB�� �ִ��� üũ
		query = "select count(*) from menu where menu_name = '" + name + "'";
		rs = di.executeQuery(query);
		try {

			rs.next();
			if (rs.getInt("count(*)") == 0) {
				JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "��ϵ��� ���� �޴��Դϴ�.", "��ȸ ����", warning.WARNING_MESSAGE);
				di.closeStatementResultSet();
			}

		} catch (SQLException e1) {
			di.closeStatementResultSet();
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		di.closeStatementResultSet();

		// ������ �ؽ�Ʈ�ʵ忡 ���� ���
		((JTextArea) this.menuPanel.getComponent(4)).setText("");
		query = "select * from menu where menu_name = '" + name + "'";
		rs = di.executeQuery(query);
		try {
			rs.next();
			((JTextArea) this.menuPanel.getComponent(4))
					.append("\n   �޴��� : " + rs.getString("menu_name") + "\n");
			((JTextArea) this.menuPanel.getComponent(4))
					.append("   ��  �� : " + rs.getString("price") + "\n");
			
			
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
