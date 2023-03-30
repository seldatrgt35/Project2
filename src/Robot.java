package playy;

public class Robot {
	private char symbol='X';
	private boolean live;
	public Robot (){
		
	}
	public Robot( boolean live) {
		
		this.live = live;
	}
	public char getSymbol() {
		return symbol;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	

}
