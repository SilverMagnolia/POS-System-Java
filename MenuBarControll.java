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
		
		System.out.println("MenuBar Controller ���� �Ϸ�..");
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
		// �α��� â ���
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JLabel idLabel = new JLabel("�����");
		JLabel pwdLabel = new JLabel("�����ȣ");
		JTextField idInput = new JTextField();
		JPasswordField pwdInput = new JPasswordField();
		JButton loginButton = new JButton("�α���");

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
		frame.setTitle("���� �α���");
		frame.setSize(320, 130);
		frame.setVisible(true);

		// �α��� ��ư�� ������ ���
		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
				if (e.getSource() == loginButton) {
					
					String id, pass;
					id = idInput.getText();
					pass = new String(pwdInput.getPassword());

					// ���� ����,���� ���̺��� �̹� �����Ѵٸ�, �޴���Ȳ �ʱ�ȭ
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
	
					} else // ���� ����
					{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "���� �����̰ų� ��й�ȣ�� Ʋ�Ƚ��ϴ�.", "�α��� ����",
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
			
			file = chooser.getSelectedFile(); // ����Ž���⿡ ���õ� ����.

			// ���õ� ������ Ȯ���ڰ� txt���� �˻�
			
			String fileName = file.getName();
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

			// �α��� �ؾ� ���� ��ǲ ����. �α��� �ȵǾ� ������ ���â ���.
			if(di.getLoginStatus()){
	
				if (ext.equals("txt"))
				{
					
					if(di.readFile(file)){
							System.out.println("�����ͺ��̽� ���̺� ��� ������ �Է� ����..");
	
					}else{
						JOptionPane warning = new JOptionPane();
						warning.showMessageDialog(null, "�Է� ����. \n1. ���� ���� �����Ͱ� ������ ������ ������ �ʰų�,"
								+ "\n2. ���̺�� �����Ͱ� �̹� �����ͺ��̽��� ������. ", 
								"warning", warning.WARNING_MESSAGE);
					}
					
				}
				else 
				{	
					
					JOptionPane warning = new JOptionPane();
					JOptionPane.showMessageDialog(null, "Ȯ���ڰ� txt�� ���ϸ� �� �� �ֽ��ϴ�.", "warning",
							warning.WARNING_MESSAGE);				
				}				
			}
			
			else{
				JOptionPane warning = new JOptionPane();
				JOptionPane.showMessageDialog(null, "���� �α����� �Ϸ�����.", "warning",
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
