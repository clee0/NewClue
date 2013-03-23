package clueGame;

import java.util.*;

public class ComputerPlayer extends Player {
	
	private char lastRoomVisited;
	private ArrayList<Card> seen;
	private Solution suggestion;
	
	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
		this.lastRoomVisited = 0;
		this.seen = new ArrayList<Card>();
		this.suggestion = null;
	}
	
	public int pickLocation(Board board) {
		ArrayList<Integer> targets = board.getTargets();
		Iterator<Integer> itr = targets.iterator();
		while(itr.hasNext()) {
			int i = itr.next();
			if(board.getCells().get(i).isDoorway()) {
				RoomCell room = (RoomCell) board.getCells().get(i);
				if(room.getInitial() != this.lastRoomVisited)
					return i;
				else
					itr.remove();
			}
		}
		return targets.get(new Random().nextInt(targets.size()));
	}
	
	public void createSuggestion() {
		
	}
	
	public void updateSeen(Card seen) {
		
	}

	public char getLastRoomVisited() {
		return lastRoomVisited;
	}

	public void setLastRoomVisited(char lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}

	public Solution getSuggestion() {
		return suggestion;
	}

	public ArrayList<Card> getSeen() {
		return seen;
	}
}