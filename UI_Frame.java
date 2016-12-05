import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class UI_Frame extends JFrame{
	
	//�����ӿ� ���� �ټ����� �г� ����	
	private UI_TableStatus tableStatusPanel;
	private UI_OrderStatus orderStatusPanel;
	private UI_MenuStatus menuStatusPanel;
	private UI_Enrollment_Inquiry enrollment_inquiry;
	private UI_MenuBar menuBar;
	
	// �Ĵ� �ֹ� ���� ���̺�
	private JLabel title_label;
	
	UI_Frame(){		
		// ���̾ƿ� ���� �� ����
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl); //���� �����ӿ� ���̾ƿ� ����
		
		/* �Ĵ� �ֹ� ���� label ����*/
		title_label = new JLabel("�Ĵ� �ֹ� ����");
		title_label.setHorizontalAlignment(title_label.CENTER);
		Font font = new Font("Times", Font.BOLD, 50);
		title_label.setBorder(new LineBorder(Color.BLACK));
		title_label.setFont(font);
		
		// �ټ� ���� �г� ����
		tableStatusPanel = new UI_TableStatus();
		orderStatusPanel = new UI_OrderStatus();
		menuStatusPanel = new UI_MenuStatus();
		enrollment_inquiry = new UI_Enrollment_Inquiry();
		
		/* ���̾ƿ� ���� (�׸���鷹�̾ƿ������� ���°�) */
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;
		
		/* �ֻ�� label ���� */
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbl.setConstraints(title_label, gbc);
		add(title_label); // �Ĵ��ֹ����� ���̺� �����ӿ� ����
		
		/* ���̺� ��Ȳ ���� */
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbl.setConstraints(tableStatusPanel,  gbc);
		add(tableStatusPanel); // ���̺� ��Ȳ �г� �����ӿ� ����
		
		/* �ֹ����� ���� */
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbl.setConstraints(orderStatusPanel,  gbc);
		add(orderStatusPanel); // �ֹ���Ȳ �г� �����ӿ� ����
		
		/* �޴� ���� */
		gbc.weighty = 3;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbl.setConstraints(menuStatusPanel,  gbc);
		add(menuStatusPanel); //�޴���Ȳ �г� �����ӿ� ����
		
		/* ���/��ȸ ���� */
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbl.setConstraints(enrollment_inquiry,  gbc);
		add(enrollment_inquiry); //�����ӿ� ����
		
		/* �����ӿ� �޴��� �߰� */
		menuBar = new UI_MenuBar();
		setJMenuBar(menuBar); // �޴��� ����		
		
		pack(); // �׸���� ���̾ƿ������� ���°�.
		setSize(700, 800); //������ ������ ����
		setVisible(true); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		System.out.println("������ ���� �Ϸ�..");
		
		init_DBInterface();
	}
	
	// ��� �������̽� �ʱ�ȭ �޼ҵ�. ���α׷� ����� �� �� ȣ�� ��.
	private void init_DBInterface()
	{
		System.out.println(">>> �� ��Ʈ�ѷ��� ��� �������̽� ���� ");
		DB_Interface di = new DB_Interface();
		
		// �� ��Ʈ�ѷ��� �ν��Ͻ� ��������
		MenuBarControll mbController = this.menuBar.getController();
		OrderStatusControll osController = this.orderStatusPanel.getController();
		MenuControll mController = this.menuStatusPanel.getController();
		Enroll_inquiryControll eiController = this.enrollment_inquiry.getController();
		TableStatusControll tsController = this.tableStatusPanel.getController();
		
		// DB �������̽��� �� ��Ʈ�ѷ��� �� �ϱ�.
		di.setMenuBarController(mbController);
		di.setOrderStatusController(osController);
		di.setMenuController(mController);
		di.setEnrollInquiryController(eiController);
		di.setTableStatusController(tsController);
		
		// �� ��Ʈ�ѷ����� di �ν��Ͻ� �ֱ�
		mbController.setDBInterface(di);
		osController.setDBInterface(di);
		mController.setDBInterface(di);
		eiController.setDBInterface(di);
		tsController.setDBInterface(di);
		
		System.out.println("���� �Ϸ�...");
	}
			
}
