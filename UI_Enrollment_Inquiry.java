import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class UI_Enrollment_Inquiry extends JPanel
{
	Enroll_inquiryControll eiController;		
	
	UI_Enrollment_Inquiry()
	{
		LineBorder pBorder = new LineBorder(Color.BLACK); // 패널 보더
		TitledBorder title = new TitledBorder(pBorder, "등록/조회"); //타이틀 보더	
		title.setTitlePosition(TitledBorder.LEFT);		
		setBorder(title);
		setLayout(new BorderLayout());
		
		// 탭에 들어갈 각각의 패널 생성.
		JPanel customerPanel = make_panel("고객명", "가입", "조회");
		JPanel salesPanel = make_panel("기간", null, null);		
		JPanel employeePanel = make_panel("직원명", "직원등록", "조회");
		JPanel menuPanel = make_panel("메뉴명", "메뉴등록", "조회");		
				
		// 탭에 패널 추가.
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("고객", customerPanel);
		tabbedPane.addTab("매출", salesPanel);
		tabbedPane.addTab("직원", employeePanel);
		tabbedPane.addTab("메뉴", menuPanel);
		
		add(tabbedPane, BorderLayout.CENTER);
		
		System.out.println("UI_Enrollment_Inquiry 패널 생성 완료..");
		
		eiController = new Enroll_inquiryControll(tabbedPane, customerPanel,
				salesPanel, employeePanel, menuPanel);
		
		//customerPanel 액션 리스너 등록
		((JButton)customerPanel.getComponent(2)).addActionListener(eiController);		
		((JButton)customerPanel.getComponent(3)).addActionListener(eiController);	
		
		// salesPanel에 액션 리스너 등록
		((JButton)salesPanel.getComponent(1)).addActionListener(eiController);
	
		// emploPanel 액션 리스너 등록
		((JButton)employeePanel.getComponent(2)).addActionListener(eiController);
		((JButton)employeePanel.getComponent(3)).addActionListener(eiController);
		
		// menuPanel 액션 리스너 등록
		((JButton)menuPanel.getComponent(2)).addActionListener(eiController);
		((JButton)menuPanel.getComponent(3)).addActionListener(eiController);
		
	}
	
	// 패널 생성 메소드
	private JPanel make_panel(String labelName, String btnName1, String btnName2)
	{
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(); 		
		JPanel panel = new JPanel();
		panel.setLayout(gbl);	
		
		LineBorder border = new LineBorder(Color.BLACK);
		
		//label과 text area는 공통으로 쓰이니까.
		JLabel label = new JLabel(labelName); 
		JTextArea textArea = new JTextArea(10, 15);	
		
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;		
		
		if(btnName1 == null && btnName2 == null)
		{
			//label셋팅
			label.setPreferredSize(new Dimension(10, 10));
			label.setHorizontalTextPosition(JLabel.CENTER);
			gbc.gridwidth = 1;
			gbc.gridx = 1;
			gbc.gridy = 1;		
			gbl.setConstraints(label, gbc);
			panel.add(label);
			
			//combo box 셋팅
			JButton cal_btn = new JButton();
			gbc.gridx = 2;
			gbc.gridy = 1;		
			gbl.setConstraints(cal_btn, gbc);
			panel.add(cal_btn);
			
			// 텍스트영역 셋팅			
			textArea.setBorder(border);
			gbc.gridwidth = 6;
			gbc.gridheight = 7;		
			gbc.gridx = 0;
			gbc.gridy = 3;		
			gbl.setConstraints(textArea, gbc);
			panel.add(textArea);				
		}
		else
		{			
			JTextField textField = new JTextField();
			JButton button1 = new JButton(btnName1);
			JButton button2 = new JButton(btnName2);
			
			// 레이블 셋팅
			label.setPreferredSize(new Dimension(3, 3));
			gbc.gridx = 0;
			gbc.gridy = 0 ;		
			gbl.setConstraints(label, gbc);
			panel.add(label);		
			
			// 텍스트필드 셋팅
			textField.setPreferredSize(new Dimension(3, 3));
			gbc.gridx = 0;
			gbc.gridy = 1 ;		
			gbl.setConstraints(textField, gbc);
			panel.add(textField);				
			
			
			// 등록버튼 셋팅
			button1.setPreferredSize(new Dimension(5, 3));
			gbc.gridx = 2;
			gbc.gridy = 1 ;		
			gbl.setConstraints(button1, gbc);
			panel.add(button1);				
			
			// 조회버튼 셋팅
			button2.setPreferredSize(new Dimension(3, 3));
			gbc.gridx = 3;
			gbc.gridy = 1 ;		
			gbl.setConstraints(button2, gbc);
			panel.add(button2);
			
			// 텍스트영역 셋팅
			textArea.setBorder(border);
			gbc.gridwidth = 4;
			gbc.gridheight = 4;		
			gbc.gridx = 0;
			gbc.gridy = 2 ;		
			gbl.setConstraints(textArea, gbc);
			panel.add(textArea);		
		}			
		return panel;
	}
	
	public Enroll_inquiryControll getController()
	{
		return this.eiController;
	}
}
