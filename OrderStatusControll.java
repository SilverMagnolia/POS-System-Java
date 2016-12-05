import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class OrderStatusControll implements ActionListener{
	
	class Order{
		// 한 테이블의 주문 현황
		final int MAX_ORDER = 50;
		private String tableNumber;
		private String[] menu;
		private int[] price;
		private int totalPrice;
		private int numOfmenu;
		
		Order(String tableNumber)
		{
			this.tableNumber = tableNumber;
			this.menu = new String[MAX_ORDER];
			this.price = new int[MAX_ORDER];
			this.totalPrice = 0;
			this.numOfmenu = 0;
		}
		
		public boolean addMenu(String menuName, int price)
		{
			if(this.numOfmenu >= MAX_ORDER)
				return false;
			
			this.menu[this.numOfmenu] = menuName;
			this.price[this.numOfmenu] = price;
			this.totalPrice += price;
			this.numOfmenu++;
			
			return true;
		}
	
		public String getTableNumber() { return this.tableNumber; }
		
		public int getTotalPrice() { return this.totalPrice; }
		
		public int getNumOfMenu() { return this.numOfmenu; }
		
		public String[] getAllMenuName() { return this.menu; }
		
		public int[] getAllPriceEachMenu() { return this.price;	}
	}

	class TempOrder{
		private String menuName;
		private int price;
		
		TempOrder(String menuName, int price)
		{
			this.menuName = menuName;
			this.price = price;
		}
		
		public String getMenuName()
		{
			return this.menuName;
		}
		
		public int getPrice()
		{
			return this.price;
		}
		
	}
	
	private JTextArea textArea;
	private JTextField customerName;
	private JComboBox<String> tableNumber;
	private JButton orderBtn;
	private JButton cancelBtn;
	private JButton paymentBtn;
	
	private LinkedList<Order> orderList;
	private LinkedBlockingQueue<TempOrder> tempOrderQueue;
	
	private DB_Interface di;	
	
	OrderStatusControll(JTextArea textArea, JTextField customerName, JComboBox<String> tableNumber, 
			JButton orderBtn, JButton cancelBtn, JButton paymentBtn)
	{
		// textArea는 리스너 등록 못함. 나머지는 리스너 등록 ok
		this.textArea = textArea;
		this.customerName = customerName;
		this.tableNumber = tableNumber;
		this.orderBtn = orderBtn;
		this.cancelBtn = cancelBtn;
		this.paymentBtn = paymentBtn;		
		
		orderList = new LinkedList<Order>();
		tempOrderQueue = new LinkedBlockingQueue<TempOrder>();
		
		System.out.println("OrderStatus Controller 생성 완료..");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.tableNumber){
			
			//메뉴를 추가하는 도중 콤보박스에서 이벤트가 발생하면 큐를 비우고
			//텍스트area도 초기화
			this.tempOrderQueue.clear();
			this.textArea.setText("");
			selectTableNumber(e);
			
		}else if(e.getSource() == this.orderBtn){
			
			setOrder();
			
		}else if(e.getSource() == this.cancelBtn){
			
			cancelOrder();
			
		}else if(e.getSource() == this.paymentBtn){
			
			String selectedTableNumber = (String)this.tableNumber.getSelectedItem();
			if(di.isTurnedOn(selectedTableNumber))
				paymentOrder();				
			
		}		
	}
	
	private Order findOrder(String tableNumber){ 
		Order order = null;
		
		ListIterator<Order> iterator = orderList.listIterator();				
		while(iterator.hasNext())
		{
			order = iterator.next();
			if(order.getTableNumber().equals(tableNumber))
				break;
		}
		
		return order;
	}
	
	private void setOrder()
	{
		String selectedTableNumber = (String) this.tableNumber.getSelectedItem();
		Order order = null;
		TempOrder tempOrder;
		int tempQueLength = this.tempOrderQueue.size();
		
		if(tempQueLength == 0)
		{
			// 임시 큐에 저장된 주문이 없다면 팝업창 띄움.
			JOptionPane warning = new JOptionPane();
			JOptionPane.showMessageDialog(null, "먼저 추가할 메뉴를 고르십시오.", "warning",
					warning.WARNING_MESSAGE);	
		}
		else
		{
			// 임시 큐에 저장된 주문들을 주문 리스트에 있는 해당 인스턴스에 추가.
			// 선택된 테이블의 첫 주문 시, 새로운 주문 인스턴스를 만들고, 추가.
			
			if(di.isTurnedOn(selectedTableNumber)){
				
				// 기존에 있던 테이블에 추가 주문하는 거라면
				order = findOrder(selectedTableNumber);
				
				// 임시 큐의 주문 인스턴스를 order에 추가.
				
				for(int i = 0; i < tempQueLength; i++)
				{
					tempOrder = this.tempOrderQueue.poll();
					order.addMenu(tempOrder.getMenuName(), tempOrder.getPrice());					
				}			
				
			}
			else
			{			
				// 새로운 테이블의 첫 주문이라면
				
				order = new Order(selectedTableNumber);		
				
				for(int i = 0; i < tempQueLength; i++)
				{
					tempOrder = this.tempOrderQueue.poll();					
					order.addMenu(tempOrder.getMenuName(), tempOrder.getPrice());					
				}// end for
				
				orderList.add(order);
			}
			// textArea초기화. 주문완료된 상태로 재출력
			textArea.setText("");
			showOrderStatusOnTextArea(order);
			textArea.append("\n\n====================\n");
			textArea.append("총 금액" + "\t" 
					+ Integer.toString(order.getTotalPrice()));
			di.setTableTurnOn(selectedTableNumber);
		}
		
		
	}
	
	private void selectTableNumber(ActionEvent e)
	{
		
		Order order = null;
		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		String selectedTableNumber = (String) cb.getSelectedItem();
		
		// tablenumber에 주문이 들어간 상태라면, 컨텐츠를 텍스트 area에 출력		
		if(di.isTurnedOn(selectedTableNumber))
		{
			
			order = findOrder(selectedTableNumber);
			showOrderStatusOnTextArea(order);
			textArea.append("\n\n====================\n");
			textArea.append("총 금액" + "\t" + Integer.toString(order.getTotalPrice()));
		}		
	}
	
	private void showOrderStatusOnTextArea(Order order)
	{
		//order 인스턴스 내의 모든 주문 현황을 testArea에 출력
		if(order == null)
			return;
		
		String newStr;
		String[] menuName = order.getAllMenuName();
		int[] price = order.getAllPriceEachMenu();
		
		for(int i = 0; i < order.numOfmenu; i++){
			newStr = "";
			newStr += menuName[i];
			newStr += "\t";
			newStr += Integer.toString(price[i]);
			textArea.append(newStr+"\n");
		}
	}

	private void cancelOrder()
	{
		Order order = null;
		String selectedTableNumber = (String)this.tableNumber.getSelectedItem();
		
		// 현재 선택된 테이블 주문현황 인스턴스 찾기
		order = findOrder(selectedTableNumber);
		
		if(this.tempOrderQueue.size() == 0 && order != null){
			// 임시 주문이 없는 경우, 현재 테이블의 모든 주문을 취소.
			// 취소하기 전에 팝업창으로 확인을 받음.
			
			JOptionPane optionPane = new JOptionPane();
			int dialogResult = JOptionPane.showConfirmDialog(null, 
					String.format("%d번 테이블의 모든 주문을 취소하시겠습니까?", 
							Integer.parseInt(selectedTableNumber)));
			if(dialogResult == JOptionPane.YES_OPTION){
				
				// 선택된 테이블의 모든 주문 취소.
				this.orderList.remove(order);
				di.setTableTurnOff(selectedTableNumber);	
				textArea.setText("");
			}			
		}else if(this.tempOrderQueue.size() > 0){
			textArea.setText("");
			
			if(order != null)
			{
				// 임시 주문 큐에 인스턴스가 있다면 그것만 삭제
				textArea.setText("");
				showOrderStatusOnTextArea(order);
				textArea.append("\n\n====================\n");
				textArea.append("총 금액" + "\t" 
						+ Integer.toString(order.getTotalPrice()));
			}
			
			this.tempOrderQueue.clear();
		}
	}
		
	private void paymentOrder()
	{		
		
		String selectedTableNumber;
		String custName;
		String query;
		String[] allMenu;
		Order order = null;
		ResultSet rs = null;
		int id=0;
		int totalPurchasingPrice=0;
	
		String curLoginName = di.getCurrentIDConnectedToDB();
		query = "select count(*) from employee where name = '"
				+ curLoginName
				+ "'";
		rs = di.executeQuery(query);
		try {
			rs.next();
			if(rs.getInt("count(*)") != 1)
			{
				JOptionPane warning = new JOptionPane();
				warning.showMessageDialog(null, "결제는 직원 로그인 후 가능합니다.", "warning",
						warning.WARNING_MESSAGE);
				di.closeStatementResultSet();
				return;
			}
		} catch (SQLException e1) {
			di.closeStatementResultSet();
			e1.printStackTrace();
		}
		di.closeStatementResultSet();		
		
		selectedTableNumber = (String)this.tableNumber.getSelectedItem();
		custName = this.customerName.getText();
		order = findOrder(selectedTableNumber);
		allMenu = order.getAllMenuName();
		
		if(custName.equals(""))
		{
			custName = "비회원";
		}
		else{
			// DB에 등록된 회원인지 체크
			query = "select count(*) from customer where name = '"
					+ custName
					+ "'";
			
			rs = di.executeQuery(query);
			try {
				rs.next();
				
				if(rs.getInt("count(*)") == 0)
				{
					JOptionPane warning = new JOptionPane();
					warning.showMessageDialog(null, "등록되지 않은 회원입니다.", "warning",
							warning.WARNING_MESSAGE);
					di.closeStatementResultSet();
					return;
				}
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}			
		}		
		di.closeStatementResultSet();
		
		rs = di.getTPPAndIdFromDB(custName);
		
		try {			
			rs.next();
			id = rs.getInt("id");
			totalPurchasingPrice = rs.getInt("total_purchasing_price");			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}finally
		{
			di.closeStatementResultSet();
		}
		
		Calendar cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		// order 인스턴스 내의 모든 내역을 Sales 테이블에 삽입
		for(int i=0 ;i<order.getNumOfMenu(); i++)
		{
			di.insertTupleToSales(id, custName, allMenu[i], year, month, day,
									hour, minute);
		}		
		
		// employee table의 total_performance 업데이트
		String currentIDConnectedToDB = di.getCurrentIDConnectedToDB();
		int total_performance;
		
		query = "select count(*) from employee where name = '"
				+ currentIDConnectedToDB
				+ "'";
		rs =  di.executeQuery(query);
		try {
			
			rs.next();
			
			if(rs.getInt("count(*)") != 0)
			{
				di.closeStatementResultSet();

				query = "select total_performance from employee where name = '"
						+ currentIDConnectedToDB + "'";
				rs = di.executeQuery(query);

				rs.next();
				total_performance = rs.getInt("total_performance") + order.getTotalPrice();
				di.closeStatementResultSet();
				di.updateTotalPerformance(total_performance);				
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
		// 등급에 따라 할인율 적용하여, 총구매금액에 합산
		if(totalPurchasingPrice >= 1000000)
			totalPurchasingPrice += (int)((double)order.getTotalPrice()*0.7);
		else if(totalPurchasingPrice >= 500000 )
			totalPurchasingPrice += (int)((double)order.getTotalPrice()*0.8);
		else if(totalPurchasingPrice >= 300000)
			totalPurchasingPrice += (int)((double)order.getTotalPrice()*0.9);
		else 
			totalPurchasingPrice += order.getTotalPrice();		
		
		di.updateTotalPurchasingPrice(custName, totalPurchasingPrice);
		
		// 총구매금액에 따라서 등급 조정
		if (!custName.equals("비회원")) {
			
			if (totalPurchasingPrice >= 1000000)
				di.updateGrade(custName, "Gold");
			else if (totalPurchasingPrice >= 500000)
				di.updateGrade(custName, "Silver");
			else if (totalPurchasingPrice >= 300000)
				di.updateGrade(custName, "Bronze");
		}
		
		
		
		di.setTableTurnOff(selectedTableNumber);
		orderList.remove(order);
		textArea.setText("");
		
		JOptionPane warning = new JOptionPane();
		warning.showMessageDialog(null, "결제되었습니다.", "결제완료",
				warning.DEFAULT_OPTION);

	}
	
	public void setTemporaryOrder(String menuName, int price)
	{	
		// 인자로 받은 menuName과 price는 메뉴 컨트롤러로부터 받음.
		// 주문버튼 누르기 전까지는 임시 큐에 주문을 저장.
		
		//String selectedTableNumber = (String) this.tableNumber.getSelectedItem();
		TempOrder tempOrder = new TempOrder(menuName, price);
		this.tempOrderQueue.add(tempOrder);		
	
		String str = "\n+" + menuName + "\t" + Integer.toString(price);
		textArea.append(str);		
	}	
	
	public void setDBInterface(DB_Interface di)
	{
		this.di = di;
	}		
}
