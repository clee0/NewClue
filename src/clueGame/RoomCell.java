package clueGame;

public class RoomCell extends BoardCell{

	public enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE};
	
	private DoorDirection doorDirection;
	private boolean room = true;
	private char initial;
	
	public RoomCell(int row, int col, String init) {
		super(row, col);
		if (init.length()==1){
			doorDirection = DoorDirection.NONE;
		}else{
			room = false;
			if(init.charAt(1)=='U'){doorDirection = DoorDirection.UP;
			//System.out.println("Door direction is up " + row + " " + col);
			} else if(init.charAt(1)=='D'){
				doorDirection = DoorDirection.DOWN;
				//System.out.println("Door direction is down " + row + " " + col);
			} else if(init.charAt(1)=='R'){
				doorDirection = DoorDirection.RIGHT;
				//System.out.println("Door direction is right " + row + " " + col);
			} else if(init.charAt(1)=='L'){
				doorDirection = DoorDirection.LEFT;
				//System.out.println("Door direction is left " + row + " " + col);
			}
		}
		
		initial = init.charAt(0);
	}
	
	
	@Override
	public Boolean isRoom() {
		return room;
	}
	
	@Override
	public Boolean isDoorway(){
		return !room;
	}

	public DoorDirection getDoorDirection() {
		//System.out.println("Door direction: " + doorDirection + " at " + row + " " + column);
		return doorDirection;
	}

	public char getInitial() {
		return initial;
	}
}
