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
		// 선언부
		GridBagLayout gbl = new GridBagLayout(); //레이아웃
		GridBagConstraints gbc = new GridBagConstraints(); 
		LineBorder pBorder = new LineBorder(Color.BLACK); // 패널 보더
		TitledBorder title = new TitledBorder(pBorder, "주문 내역"); //타이틀 보더
		
		
		JTextArea textArea = new JTextArea(5, 15);  // 주문현황과 결제금액 출력
		textArea.setEditable(false);
		JScrollPane sp = new JScrollPane(textArea);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JLabel label_1 = new JLabel("고객명");
		JLabel label_2 = new JLabel("테이블명");
		JTextField customerName = new JTextField(); //고객명 입력 받은 컴포넌트
		
		String[] tableNum = new String[20];
		for(int i=0;i<tableNum.length; i++)
		{
			tableNum[i] = Integer.toString(i+1);
		}		
		JComboBox<String> tableNumber = new JComboBox<String>(tableNum);  //테이블 명 콤보박스
		
		JButton orderBtn = new JButton("주문"); 
		JButton cancelBtn = new JButton("취소");
		JButton paymentBtn = new JButton("결제");
		
		setLayout(gbl);		
		title.setTitlePosition(TitledBorder.LEFT);		
		setBorder(title);
		
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		//textArea 셋팅
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
		
		//고객명 레이블, 텍스트필드 셋팅
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
		
		//테이블명 레이블, 테이블명 콤보박스 셋팅
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbl.setConstraints(label_2, gbc);
		add(label_2);		
		
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbl.setConstraints(tableNumber, gbc);
		add(tableNumber);
		
		// 버튼 셋팅
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
		
		System.out.println("UI_OrderStatus 패널 생성 완료..");
		
		// 유아이 컨트롤러 생성
		osController = new OrderStatusControll(textArea, customerName, tableNumber, 
				orderBtn, cancelBtn, paymentBtn);
		// 등록
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
