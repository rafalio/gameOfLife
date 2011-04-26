package life;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import life.Model.Celltype;
import life.Model.ToUpdate;


public class View {
	private Model m;
	
	UICell[][] cells;
	
	final JLabel turn = new JLabel("0",SwingConstants.CENTER);	
	
	final MyListener listen = new MyListener();
	final ChangeColor colorListener = new ChangeColor();
	final ChangeSpeed sliderListener = new ChangeSpeed();
	final TimerListener timerListener = new TimerListener();
	
	Timer timer;
	
	final JButton clear = new JButton("Clear");
	final JButton step = new JButton("Step");
	final JButton run = new JButton("Run");
	final JButton stop = new JButton("Stop");
	
	final JSlider slider = new JSlider(JSlider.VERTICAL,1,10,1);
	
	boolean running = false;
	
	public View(Model _m) {
		this.m = _m;
	}
	
	public void gui(){
		
		cells =  new UICell[m.getBoardSize()][m.getBoardSize()];
		timer = new Timer(2000/m.getSpeed(),timerListener);
		timer.setInitialDelay(0);
		

		
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		
		final JFrame frame = new JFrame("Game of Life");
		frame.setSize(800,800);
		
		final Box menubox = new Box(BoxLayout.X_AXIS);
		
		/* Initialization of playing arena */
		
		final JPanel arena = new JPanel();
		arena.setLayout(new GridLayout(m.getBoardSize(),m.getBoardSize()));
		
		for(int i = 0; i < m.getBoardSize(); i++){
			for(int j = 0; j < m.getBoardSize(); j++){
				cells[i][j] = new UICell(i,j);
				arena.add(cells[i][j]); // add the button to the playing arena
			}
		}
		
		/* End of menu arena init */
		
		menubox.add(clear);
		menubox.add(step);
		menubox.add(run);
		menubox.add(stop);
		
		
		frame.add(turn,BorderLayout.NORTH);
		frame.add(menubox,BorderLayout.SOUTH);
		frame.add(slider,BorderLayout.EAST);
		frame.add(arena,BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		clear.addActionListener(listen);
		step.addActionListener(listen);
		run.addActionListener(listen);
		stop.addActionListener(listen);
		
		slider.addChangeListener(sliderListener);
		
		// listen to all the buttons in the playing area
		
		for(UICell[] cArr : cells){
			for(UICell c : cArr){
				c.addMouseListener(colorListener);
			}
		}
		
		m.clear();
		boardUpdate();
	}
	


	
	
	private void oneCellUpdate(int i, int j){
		cells[i][j].setBackground(mapColor(m.getCell(i,j)));
	}
	
	// Synchronizes the internal representation of the model
	// with what we see on the screen
	
	private void boardUpdate(){
		
		// synchronize board state
		
//		for(int i = 0; i < m.getBoardSize(); i++){
//			for(int j = 0; j < m.getBoardSize(); j++){
//				oneCellUpdate(i,j);
//			}
//		} 
		
		for(ToUpdate t : m.getToUpdate()){
			oneCellUpdate(t.i,t.j);
		}
		m.getToUpdate().clear();
		
		// synchronize iteration text
		turn.setText(Integer.toString(m.getIteration())); 
	}
	
	// maps the enum value to a javax Color
	
	public static Color mapColor(Celltype t){
		switch(t){
			case DEAD:
				return Color.DARK_GRAY;
			case RED:
				return Color.RED;
			case GREEN:
				return Color.GREEN;
			default:
				System.out.println("ERROR");
		}
		System.out.println("This shouldn't happen");
		return Color.DARK_GRAY; // should never get here		
	} 
	
	
	// Inner listeners
	
	class MyListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event) {
			
			final JButton sent = (JButton)event.getSource();
			final String label = sent.getText();
			
			if(!running){
				
				if(label == "Clear"){
					m.clear();
					boardUpdate();
				}
				else if(label == "Step"){
					m.doStep();
					boardUpdate();
				}
				
				else if(label == "Run"){
					timer.setDelay(2000/m.getSpeed());
					timer.start();
					sent.setText("Pause");
					step.setEnabled(false);
					clear.setEnabled(false);
					stop.setEnabled(false);
					running = true;
				}
				
				else if(label == "Stop"){
					// Quit the program
					System.exit(0);
				}	
			}
			
			
			else if(label == "Pause"){
				timer.stop();
				step.setEnabled(true);
				clear.setEnabled(true);
				stop.setEnabled(true);
				sent.setText("Run");
				running = false;
			}
				
		}
		
	}
	
	class TimerListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			m.doStep();
			boardUpdate();
		}
	}
	
	class ChangeSpeed implements ChangeListener{

		public void stateChanged(ChangeEvent e) {
			final JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting()){
				m.setSpeed((int)source.getValue());
				timer.setDelay(2000/m.getSpeed());
			}
			
		}
	}
	
	// For changing the color of the board pieces
	
	class ChangeColor extends MouseAdapter{
		
		public void mouseEntered(MouseEvent e){
			if(!running){
				JButton b = (JButton)e.getSource();
				b.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			}
		}
		
		public void mouseExited(MouseEvent e){
			if(!running){
				JButton b = (JButton)e.getSource();
				b.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			}
		}
		
		public void mouseClicked(MouseEvent e){
			UICell b = (UICell)e.getSource();
			
			if(!running){
				if(SwingUtilities.isLeftMouseButton(e)){
					m.setCell(b.i, b.j,Celltype.RED);
				}
				else if(SwingUtilities.isRightMouseButton(e)){
					m.setCell(b.i, b.j,Celltype.GREEN);
				}
				else if(SwingUtilities.isMiddleMouseButton(e)){
					m.setCell(b.i, b.j,Celltype.DEAD);
				}
				
				// only update one cell
				oneCellUpdate(b.i,b.j);
			}

		}
	}
	
}
