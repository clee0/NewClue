package Test;

import java.io.*;
import java.util.*;
import org.junit.*;
import junit.framework.Assert;
import clueGame.*;
import clueGame.Card.cardType;

public class TestGameSetup {
	private static Board board;
	private static ClueGame game;
	
	@BeforeClass
	public static void setup() {
		board = new Board();
		game = new ClueGame();
		try {
			board.loadConfigFiles();
		}
		catch(FileNotFoundException e) {
			System.out.println("Board files not found");
		}
		
		try{
			game.loadConfigFiles();
		}
		catch(FileNotFoundException e) {
			System.out.println("Game files not found");
		}
	}
	
	// Checks loading players
	@Test
	public void TestPlayers() {
		ArrayList<Player> testPlayers = game.getPlayers();
		Assert.assertEquals(testPlayers.size(), 6);
		
		Assert.assertEquals(testPlayers.get(0).getName(), "Miss Scarlett");
		Assert.assertEquals(testPlayers.get(0).getColor(), "Red");
		Assert.assertEquals(testPlayers.get(0).getRow(), 25);
		Assert.assertEquals(testPlayers.get(0).getColumn(), 7);
		
		Assert.assertEquals(testPlayers.get(1).getName(), "Colonel Mustard");
		Assert.assertEquals(testPlayers.get(1).getColor(), "Yellow");
		Assert.assertEquals(testPlayers.get(1).getRow(), 25);
		Assert.assertEquals(testPlayers.get(1).getColumn(), 12);
		
		Assert.assertEquals(testPlayers.get(5).getName(), "Professor Plum");
		Assert.assertEquals(testPlayers.get(5).getColor(), "Purple");
		Assert.assertEquals(testPlayers.get(5).getRow(), 0);
		Assert.assertEquals(testPlayers.get(5).getColumn(), 15);
	}
	
	// Check cards
	@Test
	public void TestCards() {
		ArrayList<Card> testDeck = new ArrayList<Card>();
		testDeck = game.getDeck();
		int persons = 0;
		int weapons = 0;
		int rooms = 0;
		boolean correctRoom = false;
		boolean correctWeapon = false;
		boolean correctPerson = false;
		
		Assert.assertEquals(testDeck.size(), 21);
		for(Card c : testDeck) {
			if(c.getType() == cardType.PERSON) {
				persons++;
				if(c.getName().equals("Colonel Mustard"))
					correctPerson = true;
			}
			else if(c.getType() == cardType.WEAPON) {
				weapons++;
				if(c.getName().equals("Dagger"))
					correctWeapon = true;
			}
			else {
				rooms++;
				if(c.getName().equals("Study"))
					correctRoom = true;
			}
		}
		
		Assert.assertEquals(persons, 6);
		Assert.assertEquals(weapons, 6);
		Assert.assertEquals(rooms, 9);
		Assert.assertEquals(correctPerson, true);
		Assert.assertEquals(correctRoom, true);
		Assert.assertEquals(correctWeapon, true);
	}
	
	// Checks cards are dealt correctly
	@Test
	public void TestDeal() {
		ArrayList<Player> testPlayers = game.getPlayers();
		ArrayList<Card> allCards = new ArrayList<Card>();
		Set<Card> allCardsSet = new HashSet<Card>();
		int handSize = testPlayers.get(0).getCards().size();
		boolean correctSize = true;
		
		for(Player p : testPlayers) {
			allCards.addAll(p.getCards());
			if(Math.abs(p.getCards().size() - handSize) > 1)
				correctSize = false;
		}
		allCardsSet.addAll(allCards);
		
		Assert.assertEquals(correctSize, true);
		Assert.assertEquals(allCards.size(), 21);
		Assert.assertEquals(allCards.size(), allCardsSet.size());
	}

}
