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
		LineBorder pBorder = new LineBorder(Color.BLACK); // �г� ����
		TitledBorder title = new TitledBorder(pBorder, "���/��ȸ"); //Ÿ��Ʋ ����	
		title.setTitlePosition(TitledBorder.LEFT);		
		setBorder(title);
		setLayout(new BorderLayout());
		
		// �ǿ� �� ������ �г� ����.
		JPanel customerPanel = make_panel("����", "����", "��ȸ");
		JPanel salesPanel = make_panel("�Ⱓ", null, null);		
		JPanel employeePanel = make_panel("������", "�������", "��ȸ");
		JPanel menuPanel = make_panel("�޴���", "�޴����", "��ȸ");		
				
		// �ǿ� �г� �߰�.
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("��", customerPanel);
		tabbedPane.addTab("����", salesPanel);
		tabbedPane.addTab("����", employeePanel);
		tabbedPane.addTab("�޴�", menuPanel);
		
		add(tabbedPane, BorderLayout.CENTER);
		
		System.out.println("UI_Enrollment_Inquiry �г� ���� �Ϸ�..");
		
		eiController = new Enroll_inquiryControll(tabbedPane, customerPanel,
				salesPanel, employeePanel, menuPanel);
		
		//customerPanel �׼� ������ ���
		((JButton)customerPanel.getComponent(2)).addActionListener(eiController);		
		((JButton)customerPanel.getComponent(3)).addActionListener(eiController);	
		
		// salesPanel�� �׼� ������ ���
		((JButton)salesPanel.getComponent(1)).addActionListener(eiController);
	
		// emploPanel �׼� ������ ���
		((JButton)employeePanel.getComponent(2)).addActionListener(eiController);
		((JButton)employeePanel.getComponent(3)).addActionListener(eiController);
		
		// menuPanel �׼� ������ ���
		((JButton)menuPanel.getComponent(2)).addActionListener(eiController);
		((JButton)menuPanel.getComponent(3)).addActionListener(eiController);
		
	}
	
	// �г� ���� �޼ҵ�
	private JPanel make_panel(String labelName, String btnName1, String btnName2)
	{
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(); 		
		JPanel panel = new JPanel();
		panel.setLayout(gbl);	
		
		LineBorder border = new LineBorder(Color.BLACK);
		
		//label�� text area�� �������� ���̴ϱ�.
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
			//label����
			label.setPreferredSize(new Dimension(10, 10));
			label.setHorizontalTextPosition(JLabel.CENTER);
			gbc.gridwidth = 1;
			gbc.gridx = 1;
			gbc.gridy = 1;		
			gbl.setConstraints(label, gbc);
			panel.add(label);
			
			//combo box ����
			JButton cal_btn = new JButton();
			gbc.gridx = 2;
			gbc.gridy = 1;		
			gbl.setConstraints(cal_btn, gbc);
			panel.add(cal_btn);
			
			// �ؽ�Ʈ���� ����			
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
			
			// ���̺� ����
			label.setPreferredSize(new Dimension(3, 3));
			gbc.gridx = 0;
			gbc.gridy = 0 ;		
			gbl.setConstraints(label, gbc);
			panel.add(label);		
			
			// �ؽ�Ʈ�ʵ� ����
			textField.setPreferredSize(new Dimension(3, 3));
			gbc.gridx = 0;
			gbc.gridy = 1 ;		
			gbl.setConstraints(textField, gbc);
			panel.add(textField);				
			
			
			// ��Ϲ�ư ����
			button1.setPreferredSize(new Dimension(5, 3));
			gbc.gridx = 2;
			gbc.gridy = 1 ;		
			gbl.setConstraints(button1, gbc);
			panel.add(button1);				
			
			// ��ȸ��ư ����
			button2.setPreferredSize(new Dimension(3, 3));
			gbc.gridx = 3;
			gbc.gridy = 1 ;		
			gbl.setConstraints(button2, gbc);
			panel.add(button2);
			
			// �ؽ�Ʈ���� ����
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
