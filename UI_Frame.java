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
	
	//프레임에 박힐 다섯개의 패널 선언	
	private UI_TableStatus tableStatusPanel;
	private UI_OrderStatus orderStatusPanel;
	private UI_MenuStatus menuStatusPanel;
	private UI_Enrollment_Inquiry enrollment_inquiry;
	private UI_MenuBar menuBar;
	
	// 식당 주문 관리 레이블
	private JLabel title_label;
	
	UI_Frame(){		
		// 레이아웃 선언 및 생성
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl); //현재 프레임에 레이아웃 셋팅
		
		/* 식당 주문 관리 label 셋팅*/
		title_label = new JLabel("식당 주문 관리");
		title_label.setHorizontalAlignment(title_label.CENTER);
		Font font = new Font("Times", Font.BOLD, 50);
		title_label.setBorder(new LineBorder(Color.BLACK));
		title_label.setFont(font);
		
		// 다섯 개의 패널 생성
		tableStatusPanel = new UI_TableStatus();
		orderStatusPanel = new UI_OrderStatus();
		menuStatusPanel = new UI_MenuStatus();
		enrollment_inquiry = new UI_Enrollment_Inquiry();
		
		/* 레이아웃 설정 (그리드백레이아웃에서만 쓰는거) */
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;
		
		/* 최상단 label 셋팅 */
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbl.setConstraints(title_label, gbc);
		add(title_label); // 식당주문관리 레이블 프레임에 셋팅
		
		/* 테이블 현황 셋팅 */
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbl.setConstraints(tableStatusPanel,  gbc);
		add(tableStatusPanel); // 테이블 현황 패널 프레임에 셋팅
		
		/* 주문내역 셋팅 */
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbl.setConstraints(orderStatusPanel,  gbc);
		add(orderStatusPanel); // 주문현황 패널 프레임에 셋팅
		
		/* 메뉴 셋팅 */
		gbc.weighty = 3;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbl.setConstraints(menuStatusPanel,  gbc);
		add(menuStatusPanel); //메뉴현황 패널 프레임에 셋팅
		
		/* 등록/조회 셋팅 */
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbl.setConstraints(enrollment_inquiry,  gbc);
		add(enrollment_inquiry); //프레임에 셋팅
		
		/* 프레임에 메뉴바 추가 */
		menuBar = new UI_MenuBar();
		setJMenuBar(menuBar); // 메뉴바 셋팅		
		
		pack(); // 그리드백 레이아웃에서만 쓰는거.
		setSize(700, 800); //프레임 사이즈 셋팅
		setVisible(true); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		System.out.println("프레임 생성 완료..");
		
		init_DBInterface();
	}
	
	// 디비 인터페이스 초기화 메소드. 프로그램 실행시 한 번 호출 됨.
	private void init_DBInterface()
	{
		System.out.println(">>> 각 컨트롤러와 디비 인터페이스 연결 ");
		DB_Interface di = new DB_Interface();
		
		// 각 컨트롤러의 인스턴스 가져오기
		MenuBarControll mbController = this.menuBar.getController();
		OrderStatusControll osController = this.orderStatusPanel.getController();
		MenuControll mController = this.menuStatusPanel.getController();
		Enroll_inquiryControll eiController = this.enrollment_inquiry.getController();
		TableStatusControll tsController = this.tableStatusPanel.getController();
		
		// DB 인터페이스에 각 컨트롤러를 셋 하기.
		di.setMenuBarController(mbController);
		di.setOrderStatusController(osController);
		di.setMenuController(mController);
		di.setEnrollInquiryController(eiController);
		di.setTableStatusController(tsController);
		
		// 각 컨트롤러에게 di 인스턴스 주기
		mbController.setDBInterface(di);
		osController.setDBInterface(di);
		mController.setDBInterface(di);
		eiController.setDBInterface(di);
		tsController.setDBInterface(di);
		
		System.out.println("연결 완료...");
	}
			
}
