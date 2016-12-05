import javax.swing.JMenuBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UI_MenuBar extends JMenuBar
{
	private JMenu menu;
	private JMenuItem open;
	private JMenuItem log_in;
	
	private MenuBarControll mbController;
	
	UI_MenuBar()
	{
		menu = new JMenu("Login");	
		open = new JMenuItem("open");
		log_in = new JMenuItem("log-in");
		menu.add(open);
		menu.add(log_in);
		add(menu);				
		
		System.out.println("UI_MenuBar 생성 완료..");
		
		// 컨트롤러 생성 및 각 컴포넌트의 이벤트 리스너로 등록.
		mbController = new MenuBarControll(open, log_in);
		open.addActionListener(mbController);		
		log_in.addActionListener(mbController);
	}
	
	public MenuBarControll getController()
	{
		return this.mbController;
	}
}
