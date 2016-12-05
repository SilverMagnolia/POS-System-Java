import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MenuBarControll implements ActionListener {

	private JMenuItem open;
	private JMenuItem log_in;
	
	private DB_Interface di;
	
	MenuBarControll(JMenuItem open, JMenuItem log_in)
	{
		this.open = open;
		this.log_in = log_in;
		
		System.out.println("MenuBar Controller 생성 완료..");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == open)
			try {
				open_file();
			} catch (HeadlessException | SQLException e1) {
				e1.printStackTrace();
			}
		else if(e.getSource() == log_in)
			log_in();
	}
	
	private void log_in() {
		// 로그인 창 띄움
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JLabel idLabel = new JLabel("사원명");
		JLabel pwdLabel = new JLabel("사원번호");
		JTextField idInput = new JTextField();
		JPasswordField pwdInput = new JPasswordField();
		JButton loginButton = new JButton("로그인");

		panel.setLayout(null);
		idLabel.setBounds(20, 10, 60, 30);
		pwdLabel.setBounds(20, 50, 60, 30);
		idInput.setBounds(100, 10, 80, 30);
		pwdInput.setBounds(100, 50, 80, 30);
		loginButton.setBounds(200, 25, 80, 35);

		panel.add(idLabel);
		panel.add(pwdLabel);
		panel.add(idInput);
		panel.add(pwdInput);
		panel.add(loginButton);

		frame.add(panel);
		frame.setTitle("직원 로그인");
		frame.setSize(320, 130);
		frame.setVisible(true);

		// 로그인 버튼에 리스너 등록
		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
				if (e.getSource() == loginButton) {
					
					String id, pass;
					id = idInput.getText();
					pass = new String(pwdInput.getPassword());

					// 접속 성공,관련 테이블이 이미 존재한다면, 메뉴현황 초기화
					if (di.log_in(id, pass)) {
						frame.dispose();
						
						String query = "select count(*) from ALL_TABLES where TABLE_NAME = 'MENU'";
						ResultSet rs = di.executeQuery(query);
						int n = 0 ;						
						try {
							rs.next();
							n = rs.getInt("count(*)");
							if (n == 1)
							{
								di.initMenuPanel();							
							}
						} catch (SQLException e1) {

							di.closeStatementResultSet();
							System.out.println(e1.getMessage());
						}
						di.closeStatementResultSet();
	
					} else // 접속 실패
					{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "없는 계정이거나 비밀번호가 틀렸습니다.", "로그인 실패",
								warning.WARNING_MESSAGE);
					}
				}
				
			}
		});
	}
		
	@SuppressWarnings({ "static-access" })
	private void open_file() throws HeadlessException, SQLException {
		
		File file;
		JFileChooser chooser;
		
		chooser = new JFileChooser();
		
		int n = chooser.showOpenDialog(null);
		
		if (n == JFileChooser.APPROVE_OPTION) {
			
			file = chooser.getSelectedFile(); // 파일탐색기에 선택된 파일.

			// 선택된 파일의 확장자가 txt인지 검사
			
			String fileName = file.getName();
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

			// 로그인 해야 파일 인풋 가능. 로그인 안되어 있으면 경고창 띄움.
			if(di.getLoginStatus()){
	
				if (ext.equals("txt"))
				{
					
					if(di.readFile(file)){
							System.out.println("데이터베이스 테이블에 모든 데이터 입력 성공..");
	
					}else{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "입력 실패. \n1. 파일 내의 데이터가 정해진 포맷을 따르지 않거나,"
								+ "\n2. 테이블과 데이터가 이미 데이터베이스에 존재함. ", 
								"warning", warning.WARNING_MESSAGE);
					}
					
				}
				else 
				{	
					
					JOptionPane warning = new JOptionPane();
					JOptionPane.showMessageDialog(null, "확장자가 txt인 파일만 열 수 있습니다.", "warning",
							warning.WARNING_MESSAGE);				
				}				
			}
			
			else{
				JOptionPane warning = new JOptionPane();
				JOptionPane.showMessageDialog(null, "먼저 로그인을 하려무나.", "warning",
						warning.WARNING_MESSAGE);
			}//else
			
		}
		
	}

	public DB_Interface getDBInterface() {
		return this.di;
	}
	
	public void setDBInterface(DB_Interface di)
	{
		this.di = di;
	}
}
