import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class UI_TableStatus extends JPanel
{	
	private JLabel[] label;
	private TableStatusControll tsController;
	
	UI_TableStatus()
	{
		setLayout(new GridLayout(4, 5, 7, 7));
		LineBorder pBorder = new LineBorder(Color.BLACK); // 패널 보더
		TitledBorder title = new TitledBorder(pBorder, "테이블 현황"); //타이틀 보더	
		LineBorder lBorder = new LineBorder(Color.BLACK, 2);
		title.setTitlePosition(TitledBorder.LEFT);		
		setBorder(title);		
		
		label = new JLabel[20];
		
		for(int i=0; i<20; i++)
		{
			label[i] = new JLabel();
			label[i].setOpaque(true);			
			label[i].setBackground(Color.white);
			label[i].setForeground(Color.black);
			label[i].setText(Integer.toString(i+1));
			label[i].setHorizontalAlignment(label[i].CENTER);
			label[i].setBorder(lBorder);	
			add(label[i]);		
		}
		
		System.out.println("UI_TableStatus 패널 생성 완료..");
		this.tsController = new TableStatusControll(label);
	}
	
	public TableStatusControll getController()
	{
		return this.tsController;
	}
}
