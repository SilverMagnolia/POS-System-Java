import java.awt.Color;

import javax.swing.JLabel;

public class TableStatusControll {
	
	private DB_Interface di;
	private JLabel[] label;
	private boolean[] onoff;
	
	TableStatusControll(JLabel[] label)
	{
		this.label = label;
		this.onoff = new boolean[21];
		
		for(int i=0; i<21; i++)
		{
			onoff[i] = false;
		}
		System.out.println("TableStauts Controller 생성 완료..");		
	}
	
	public boolean setTableOn(String tableNumber)
	{
		if(onoff[Integer.parseInt(tableNumber)])
			return false;
			
		this.onoff[Integer.parseInt(tableNumber)] = true;	
		turnOn(tableNumber);
		return true;
	}
	
	public boolean setTableOff(String tableNumber)
	{
		if(!onoff[Integer.parseInt(tableNumber)])
			return false;
			
		this.onoff[Integer.parseInt(tableNumber)] = false;
		turnOff(tableNumber);
		return true;
	}
	
	public boolean isTurnedOn(String tableNumber)
	{
		return onoff[Integer.parseInt(tableNumber)];
	}

	private void turnOn(String tableNumber)
	{
		int labelIdx = Integer.parseInt(tableNumber)-1;
		this.label[labelIdx].setBackground(Color.YELLOW);	
	}
	
	private void turnOff(String tableNumber)
	{
		int labelIdx = Integer.parseInt(tableNumber)-1;
		this.label[labelIdx].setBackground(Color.WHITE);
	}
	
	
	public void setDBInterface(DB_Interface di)
	{
		this.di = di;
	}
}
