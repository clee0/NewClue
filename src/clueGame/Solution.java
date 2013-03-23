package clueGame;

public class Solution {
	
	private String person;
	private String weapon;
	private String room;
	
	public Solution(String person, String weapon, String room) {
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}
	
	
	
	public String getPerson() {
		return person;
	}



	public String getWeapon() {
		return weapon;
	}



	public String getRoom() {
		return room;
	}



	public boolean equals(Solution solution) {
		return (this.person.equals(solution.getPerson()) && 
				this.weapon.equals(solution.getWeapon()) &&
				this.room.equals(solution.getRoom()));
	}
}