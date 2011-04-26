package life;


import java.awt.*;
import javax.swing.*;

import life.Model.Celltype;

public class UICell extends JButton {
	private static final long serialVersionUID = 1L;
	
	int i = 0;
	int j = 0;
	
	public UICell(int _i, int _j) {
		super();
		
		i = _i;
		j = _j;
		
		this.setBackground(View.mapColor(Celltype.DEAD));
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.setOpaque(true);
	}
	

}
