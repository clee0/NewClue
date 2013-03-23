package clueGame;

import java.util.*;

public class Player {
	
	private String name;
	private String color;
	private ArrayList<Card> cards;
	private int row;
	private int column;
	private char room;
	
	public Player(String name, String color, int row, int column) {
		this.name = name;
		this.color = color;
		this.cards = new ArrayList<Card>();
		this.row = row;
		this.column = column;
		this.room = 0;
	}
	
	public char getRoom() {
		return room;
	}

	public void setRoom(char room) {
		this.room = room;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}

	public Card disproveSuggestion(String person, String room, String weapon) {
		ArrayList<Card> potentialCards = new ArrayList<Card>();
		for(Card c : cards) {
			if(c.getName().equals(person) || c.getName().equals(room) || c.getName().equals(weapon))
				potentialCards.add(c);
		}
		if(potentialCards.size() == 0)
			return null;
		else
			return potentialCards.get(new Random().nextInt(potentialCards.size()));
	}
	
	public void dealCard(Card card) {
		cards.add(card);
	}
	
	@Override
	public boolean equals(Object player) {
		if(player == this)
			return true;
		if(player == null || player.getClass() != this.getClass())
			return false;
		Player otherPlayer = (Player) player;
		return (this.name.equals(otherPlayer.name));
	}
}