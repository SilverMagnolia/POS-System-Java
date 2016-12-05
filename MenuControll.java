import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.JButton;

public class MenuControll implements ActionListener{
	
	class MenuList {
		
		class Menu{
			
			private String menuName;
			private int price;
			
			Menu(String menuName, int price){
				this.menuName = menuName;
				this.price = price;
			}
			
			public String getMenuName(){ return this.menuName; }
			public int getPrice(){ return this.price; }
		}		
		
		private Menu[] menuList;
		private int numOfMenu;
		
		MenuList()
		{
			this.menuList = new Menu[20];
			this.numOfMenu = 0;
		}
		
		public boolean addMenuToList(String menuName, int price)
		{
			if(this.numOfMenu >= 20)
				return false;
			
			this.menuList[this.numOfMenu] = new Menu(menuName, price);
			this.numOfMenu++;
			
			return true;
		}
		
		public Menu deleteMenuFromList(String menuName)
		{
			if(this.numOfMenu <= 0)
				return null;
			
			Menu dMenu = null;
			
			for(int i=0; i<this.numOfMenu; i++)
			{
				if(menuList[i].getMenuName().equals(menuName))
				{
					dMenu = menuList[i];
					for(int j = i; j < this.numOfMenu - 1; j++)
						menuList[j] = menuList[j+1];
					
					break;
				}
			}
			this.numOfMenu--;
			return dMenu;
		}
		
		public int getPriceOfMenu(String menuName)
		{
			int price = -1;
			
			for(int i=0; i<this.numOfMenu; i++)
			{
				if(menuList[i].getMenuName().equals(menuName))
				{
					price = menuList[i].getPrice();
					break;
				}				
			}
			
			return price;
		}
		
		public String getMenu(int i) { return menuList[i].getMenuName(); }
	
		public int getNumOfMenu(){ return this.numOfMenu; }
	}
	
	private JButton[] button;	
	private MenuList menuList; 
	private DB_Interface di;
	
	public MenuControll(JButton[] button2) {
		
		this.button = new JButton[20];
		this.button = button2;
		this.menuList = null;
		System.out.println("Menu Controller 생성 완료.. ");		
	}

	public void actionPerformed(ActionEvent e) {
		
		JButton tempBtn = (JButton) e.getSource();
		String menuName = tempBtn.getText();
		int price;
		
		// 이벤트 발생한 버튼이 메뉴가 등록되지 않은 버튼이라면 걍 건너뜀.
		if(menuName.length() != 0){			
			price = menuList.getPriceOfMenu(menuName);
			di.setTemporaryOrderOnOrderStatusPanel(menuName, price);
		}		
	}	
	
	public void initMenu(ResultSet rs) throws SQLException{
		
		String menuName;
		this.menuList = new MenuList();
		int price;
		int btnIdx = 0;		
		
		while(rs.next())
		{
			menuName = rs.getString("menu_name");
			price = rs.getInt("price");
			
			if(menuList.addMenuToList(menuName, price)){
				button[btnIdx].setText(menuName);
				btnIdx++;
			}			
		}	
	}
	
	public void setDBInterface(DB_Interface di)	{
		this.di = di;
	}
}
