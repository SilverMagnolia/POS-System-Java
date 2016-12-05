import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class UI_OrderStatus extends JPanel
{
	JTextArea textArea;
	JTextField customerName;
	JComboBox tableNumber;
	JButton orderBtn;
	JButton cancelBtn;
	JButton paymentBtn;
	
	OrderStatusControll osController;
	
	UI_OrderStatus()
	{
		// �����
		GridBagLayout gbl = new GridBagLayout(); //���̾ƿ�
		GridBagConstraints gbc = new GridBagConstraints(); 
		LineBorder pBorder = new LineBorder(Color.BLACK); // �г� ����
		TitledBorder title = new TitledBorder(pBorder, "�ֹ� ����"); //Ÿ��Ʋ ����
		
		
		JTextArea textArea = new JTextArea(5, 15);  // �ֹ���Ȳ�� �����ݾ� ���
		textArea.setEditable(false);
		JScrollPane sp = new JScrollPane(textArea);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JLabel label_1 = new JLabel("����");
		JLabel label_2 = new JLabel("���̺��");
		JTextField customerName = new JTextField(); //���� �Է� ���� ������Ʈ
		
		String[] tableNum = new String[20];
		for(int i=0;i<tableNum.length; i++)
		{
			tableNum[i] = Integer.toString(i+1);
		}		
		JComboBox<String> tableNumber = new JComboBox<String>(tableNum);  //���̺� �� �޺��ڽ�
		
		JButton orderBtn = new JButton("�ֹ�"); 
		JButton cancelBtn = new JButton("���");
		JButton paymentBtn = new JButton("����");
		
		setLayout(gbl);		
		title.setTitlePosition(TitledBorder.LEFT);		
		setBorder(title);
		
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		//textArea ����
		LineBorder textAreaBorder = new LineBorder(Color.gray);
		textArea.setBorder(textAreaBorder);
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.gridheight = 7;		
		gbc.gridx = 0;
		gbc.gridy = 0;		
		gbl.setConstraints(sp, gbc);
		//add(textArea);	
		add(sp);
		
		//���� ���̺�, �ؽ�Ʈ�ʵ� ����
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbl.setConstraints(label_1, gbc);
		add(label_1);		
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbl.setConstraints(customerName, gbc);
		add(customerName);
		
		//���̺�� ���̺�, ���̺�� �޺��ڽ� ����
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbl.setConstraints(label_2, gbc);
		add(label_2);		
		
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbl.setConstraints(tableNumber, gbc);
		add(tableNumber);
		
		// ��ư ����
		gbc.fill = GridBagConstraints.EAST;
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbl.setConstraints(orderBtn, gbc);
		add(orderBtn);
		
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbl.setConstraints(cancelBtn, gbc);
		add(cancelBtn);
		
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbl.setConstraints(paymentBtn, gbc);
		add(paymentBtn);	
		
		System.out.println("UI_OrderStatus �г� ���� �Ϸ�..");
		
		// ������ ��Ʈ�ѷ� ����
		osController = new OrderStatusControll(textArea, customerName, tableNumber, 
				orderBtn, cancelBtn, paymentBtn);
		// ���
		customerName.addActionListener(osController);
		tableNumber.addActionListener(osController);
		orderBtn.addActionListener(osController);
		cancelBtn.addActionListener(osController);
		paymentBtn.addActionListener(osController);		
	}
	
	public OrderStatusControll getController()
	{
		return this.osController;
	}
}
