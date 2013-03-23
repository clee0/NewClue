package clueGame;

public class BoardCell {

	protected int column;
	protected int row;

	public BoardCell(int row, int col) {
		this.row = row;
		this.column = col;
	}
	
	public Boolean isWalkway() {
		return false;
	}

	public Boolean isRoom() {
		return false;
	}

	public Boolean isDoorway() {
		return false;
	}
	
	
	
}
