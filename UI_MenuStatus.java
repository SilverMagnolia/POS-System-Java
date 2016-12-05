import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class UI_MenuStatus extends JPanel
{
	JButton[] button;
	MenuControll mController;
	
	UI_MenuStatus()
	{
		
		button = new JButton[20];		
		
		LineBorder pBorder = new LineBorder(Color.BLACK); // 패널 보더
		TitledBorder title = new TitledBorder(pBorder, "메뉴"); //타이틀 보더	
		title.setTitlePosition(TitledBorder.LEFT);		
		setBorder(title);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(); 
		setLayout(gbl);
		
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		
		int i;
		for(i = 0; i < 10; i++)
		{
			button[i] = new JButton();
			button[i].setPreferredSize(new Dimension(10, 3));
			gbc.gridx = 0;
			gbc.gridy = i;
			gbl.setConstraints(button[i], gbc);
			add(button[i]);		
		}
		
		for(; i < button.length; i++)
		{
			button[i] = new JButton();
			button[i].setPreferredSize(new Dimension(10, 3));
			
			if(i >= 10)
			{
				gbc.gridx = 1;
				gbc.gridy = i - 10;
			}
			else
			{
				gbc.gridx = 0;
				gbc.gridy = i;
			}
			gbl.setConstraints(button[i], gbc);
			add(button[i]);				
		}		
		System.out.println("UI_MenuStatus 패널 생성 완료..");
		
		// 컨트롤러 생성 및, 액션 리스너로 각 버튼에 등록.
		mController = new MenuControll(button);
		for(i=0; i<20; i++)
			button[i].addActionListener(mController);		
	}
	
	public MenuControll getController()
	{
		return this.mController;
	}
}
