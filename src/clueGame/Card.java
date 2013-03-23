package clueGame;

public class Card {
	
	public enum cardType {PERSON, WEAPON, ROOM};
	
	private String name;
	private cardType type;
	
	public Card(String name, cardType type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public cardType getType() {
		return type;
	}
	public void setType(cardType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object card) {
		if(card == this)
			return true;
		if(card == null || card.getClass() != this.getClass())
			return false;
		Card otherCard = (Card) card;
		return (this.name.equals(otherCard.name) && this.type == otherCard.type);
	}
}