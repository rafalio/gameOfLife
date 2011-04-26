package life;

import java.util.HashSet;

public class Model {
	
	private int boardsize = 0;
	private int iterations = 0;
	private int speed = 1;
	
	private Celltype[][] board; 
	
	private HashSet<ToUpdate> toUpdate = new HashSet<ToUpdate>();
	
	public class ToUpdate{
		int i;
		int j;
		Celltype t;
		
		public ToUpdate(int _i, int _j, Celltype _t){
			i = _i;
			j = _j;
			t = _t;
		}
	}      
	
	              
	public enum Celltype{
		RED, GREEN, DEAD;
	}
	
	public Model(int size) {
		boardsize = size;
		board = new Celltype[size][size];
		clear(); // initiate all cells to dead
		
	}
	
	public HashSet<ToUpdate> getToUpdate(){
		return toUpdate;
	}
	
	public int getBoardSize(){
		return this.boardsize;
	}
	
	public int getIteration(){
		return this.iterations;
	}
	
	public int getSpeed(){
		return this.speed;
	}
	
	public void setSpeed(int s){
		this.speed = s;
	}
	
	public void setIteration(int i){
		this.iterations = i;
	}
	
	public Celltype getCell(int i, int j){
		return board[i][j];
	}
	
	public Celltype[][] getBoard(){
		return this.board;
	}
	
	public void setCell(int i, int j, Celltype t){
		board[i][j] = t;
	}
	
	// destroys all the cells
	public void clear(){
		
		toUpdate.clear();
		
		for(int i = 0; i < boardsize; i++){
			for(int j = 0; j < boardsize; j++){
				board[i][j] = Celltype.DEAD;
				toUpdate.add(new ToUpdate(i,j,Celltype.DEAD));
			}
		}
		
		iterations = 0;
	}
	
	private boolean isAlive(int i, int j){
		return board[i][j] != Celltype.DEAD;
	}
	
	public void doStep(){
		for(int i = 0; i < boardsize; i++){
			for(int j = 0; j < boardsize; j++){
				Neighbours n = getNeighbours(i, j);
				
				if( (n.total < 2 || n.total > 3 ) && isAlive(i,j) ){
					//toDo[i][j] = Celltype.DEAD;
					toUpdate.add(new ToUpdate(i,j,Celltype.DEAD));
				}
				else if( n.total == 3 && !isAlive(i,j) ){
					if(n.green > n.red){
						//toDo[i][j] = Celltype.GREEN;
						toUpdate.add(new ToUpdate(i,j,Celltype.GREEN));
					}
					else{
						//toDo[i][j] = Celltype.RED;
						toUpdate.add(new ToUpdate(i,j,Celltype.RED));
					}
				}
			}
		}
		
		for(ToUpdate t : toUpdate){
			board[t.i][t.j] = t.t;
		}
		
		iterations++;
	}
	
	private class Neighbours{
		int red;
		int green;
		int total;
		
		public Neighbours(int r, int g) {
			red = r;
			green = g;
			total = r+g;
		}
	}

	// mod function
	private static int mod(int x, int mod) {
	      while ( x < 0 ) {
	          x = x + mod;
	      }
	      return x % mod;
	}
	
	// returns sanitized digit
	private int sanDigit(int x){
		return mod(x,boardsize);
	}

	// returns the live neighbours around cell located at i,j
	private Neighbours getNeighbours(int i, int j){		
		int red = 0;
		int green = 0;
		
		for(int a = -1; a <= 1; a++){
			for(int b = -1; b <=1; b++){
				int xcor = sanDigit(j+b);
				int ycor = sanDigit(i+a);
				
				if(ycor == i && xcor == j){
					continue; // we do not traverse through ourselves
				}
				
				Celltype t = getCell(ycor, xcor);
				
				switch(t){
					case RED: red++; break;
					case GREEN: green++; break;
				}
			}
		}
		
		return new Neighbours(red,green);
		
	}
	
	
	
}
