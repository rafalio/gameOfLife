package life;

public class Life {
	
	public static void main(String[] args) {
		
		
		Model m = null;
		int size = 30;
		
		
		if(args.length >= 1){
			size = Integer.parseInt(args[0]);
			
			if(size < 4){
				System.out.println("Error, board size has to be greater than 4.");
				System.exit(1);
			}
		}
		
		
		m = new Model(size);
		
		final View v = new View(m);
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				v.gui();
			}
		});
	}
	
   
   
}
