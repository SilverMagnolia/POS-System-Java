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
		// �� ���̺��� �ֹ� ��Ȳ
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
		// textArea�� ������ ��� ����. �������� ������ ��� ok
		this.textArea = textArea;
		this.customerName = customerName;
		this.tableNumber = tableNumber;
		this.orderBtn = orderBtn;
		this.cancelBtn = cancelBtn;
		this.paymentBtn = paymentBtn;		
		
		orderList = new LinkedList<Order>();
		tempOrderQueue = new LinkedBlockingQueue<TempOrder>();
		
		System.out.println("OrderStatus Controller ���� �Ϸ�..");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.tableNumber){
			
			//�޴��� �߰��ϴ� ���� �޺��ڽ����� �̺�Ʈ�� �߻��ϸ� ť�� ����
			//�ؽ�Ʈarea�� �ʱ�ȭ
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
			// �ӽ� ť�� ����� �ֹ��� ���ٸ� �˾�â ���.
			JOptionPane warning = new JOptionPane();
			JOptionPane.showMessageDialog(null, "���� �߰��� �޴��� ���ʽÿ�.", "warning",
					warning.WARNING_MESSAGE);	
		}
		else
		{
			// �ӽ� ť�� ����� �ֹ����� �ֹ� ����Ʈ�� �ִ� �ش� �ν��Ͻ��� �߰�.
			// ���õ� ���̺��� ù �ֹ� ��, ���ο� �ֹ� �ν��Ͻ��� �����, �߰�.
			
			if(di.isTurnedOn(selectedTableNumber)){
				
				// ������ �ִ� ���̺� �߰� �ֹ��ϴ� �Ŷ��
				order = findOrder(selectedTableNumber);
				
				// �ӽ� ť�� �ֹ� �ν��Ͻ��� order�� �߰�.
				
				for(int i = 0; i < tempQueLength; i++)
				{
					tempOrder = this.tempOrderQueue.poll();
					order.addMenu(tempOrder.getMenuName(), tempOrder.getPrice());					
				}			
				
			}
			else
			{			
				// ���ο� ���̺��� ù �ֹ��̶��
				
				order = new Order(selectedTableNumber);		
				
				for(int i = 0; i < tempQueLength; i++)
				{
					tempOrder = this.tempOrderQueue.poll();					
					order.addMenu(tempOrder.getMenuName(), tempOrder.getPrice());					
				}// end for
				
				orderList.add(order);
			}
			// textArea�ʱ�ȭ. �ֹ��Ϸ�� ���·� �����
			textArea.setText("");
			showOrderStatusOnTextArea(order);
			textArea.append("\n\n====================\n");
			textArea.append("�� �ݾ�" + "\t" 
					+ Integer.toString(order.getTotalPrice()));
			di.setTableTurnOn(selectedTableNumber);
		}
		
		
	}
	
	private void selectTableNumber(ActionEvent e)
	{
		
		Order order = null;
		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		String selectedTableNumber = (String) cb.getSelectedItem();
		
		// tablenumber�� �ֹ��� �� ���¶��, �������� �ؽ�Ʈ area�� ���		
		if(di.isTurnedOn(selectedTableNumber))
		{
			
			order = findOrder(selectedTableNumber);
			showOrderStatusOnTextArea(order);
			textArea.append("\n\n====================\n");
			textArea.append("�� �ݾ�" + "\t" + Integer.toString(order.getTotalPrice()));
		}		
	}
	
	private void showOrderStatusOnTextArea(Order order)
	{
		//order �ν��Ͻ� ���� ��� �ֹ� ��Ȳ�� testArea�� ���
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
		
		// ���� ���õ� ���̺� �ֹ���Ȳ �ν��Ͻ� ã��
		order = findOrder(selectedTableNumber);
		
		if(this.tempOrderQueue.size() == 0 && order != null){
			// �ӽ� �ֹ��� ���� ���, ���� ���̺��� ��� �ֹ��� ���.
			// ����ϱ� ���� �˾�â���� Ȯ���� ����.
			
			JOptionPane optionPane = new JOptionPane();
			int dialogResult = JOptionPane.showConfirmDialog(null, 
					String.format("%d�� ���̺��� ��� �ֹ��� ����Ͻðڽ��ϱ�?", 
							Integer.parseInt(selectedTableNumber)));
			if(dialogResult == JOptionPane.YES_OPTION){
				
				// ���õ� ���̺��� ��� �ֹ� ���.
				this.orderList.remove(order);
				di.setTableTurnOff(selectedTableNumber);	
				textArea.setText("");
			}			
		}else if(this.tempOrderQueue.size() > 0){
			textArea.setText("");
			
			if(order != null)
			{
				// �ӽ� �ֹ� ť�� �ν��Ͻ��� �ִٸ� �װ͸� ����
				textArea.setText("");
				showOrderStatusOnTextArea(order);
				textArea.append("\n\n====================\n");
				textArea.append("�� �ݾ�" + "\t" 
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
				warning.showMessageDialog(null, "������ ���� �α��� �� �����մϴ�.", "warning",
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
			custName = "��ȸ��";
		}
		else{
			// DB�� ��ϵ� ȸ������ üũ
			query = "select count(*) from customer where name = '"
					+ custName
					+ "'";
			
			rs = di.executeQuery(query);
			try {
				rs.next();
				
				if(rs.getInt("count(*)") == 0)
				{
					JOptionPane warning = new JOptionPane();
					warning.showMessageDialog(null, "��ϵ��� ���� ȸ���Դϴ�.", "warning",
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
		
		// order �ν��Ͻ� ���� ��� ������ Sales ���̺� ����
		for(int i=0 ;i<order.getNumOfMenu(); i++)
		{
			di.insertTupleToSales(id, custName, allMenu[i], year, month, day,
									hour, minute);
		}		
		
		// employee table�� total_performance ������Ʈ
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
		
		
		// ��޿� ���� ������ �����Ͽ�, �ѱ��űݾ׿� �ջ�
		if(totalPurchasingPrice >= 1000000)
			totalPurchasingPrice += (int)((double)order.getTotalPrice()*0.7);
		else if(totalPurchasingPrice >= 500000 )
			totalPurchasingPrice += (int)((double)order.getTotalPrice()*0.8);
		else if(totalPurchasingPrice >= 300000)
			totalPurchasingPrice += (int)((double)order.getTotalPrice()*0.9);
		else 
			totalPurchasingPrice += order.getTotalPrice();		
		
		di.updateTotalPurchasingPrice(custName, totalPurchasingPrice);
		
		// �ѱ��űݾ׿� ���� ��� ����
		if (!custName.equals("��ȸ��")) {
			
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
		warning.showMessageDialog(null, "�����Ǿ����ϴ�.", "�����Ϸ�",
				warning.DEFAULT_OPTION);

	}
	
	public void setTemporaryOrder(String menuName, int price)
	{	
		// ���ڷ� ���� menuName�� price�� �޴� ��Ʈ�ѷ��κ��� ����.
		// �ֹ���ư ������ �������� �ӽ� ť�� �ֹ��� ����.
		
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
