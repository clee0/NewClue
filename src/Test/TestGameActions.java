package Test;

import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

import org.junit.*;
import junit.framework.Assert;
import clueGame.*;

public class TestGameActions {
	private static Board board;
	private static ClueGame game;
	
	@BeforeClass
	public static void setup() {
		board = new Board();
		game = new ClueGame();
		try {
			board.loadConfigFiles();
			game.loadConfigFiles();
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	
	@Test
	public void TestAccusation() {
		Solution testSolution = new Solution("Colonel Mustard", "Rope", "Library");
		game.setAnswer(testSolution);
		Solution goodAnswer = new Solution("Colonel Mustard", "Rope", "Library");
		Solution badAnswer1 = new Solution ("Mrs. White", "Rope", "Library");
		Solution badAnswer2 = new Solution ("Colonel Mustard", "Dagger", "Library");
		Solution badAnswer3 = new Solution ("Colonel Mustard", "Rope", "Study");
		Assert.assertTrue(game.checkAccusation(goodAnswer));
		Assert.assertFalse(game.checkAccusation(badAnswer1));
		Assert.assertFalse(game.checkAccusation(badAnswer2));
		Assert.assertFalse(game.checkAccusation(badAnswer3));	
	}
	
	// Tests random location selection
	@Test
	public void TestPickLocation1() {
		ComputerPlayer comp = new ComputerPlayer("Mrs. White", "Red", 0, 0);
		board.startTargets(board.calcIndex(6, 14), 2);
		
		int loc6_12 = 0;
		int loc8_14 = 0;
		int loc5_15 = 0;
		
		for(int i = 0; i < 100; i++) {
			int selected = comp.pickLocation(board);
			if(selected == board.calcIndex(6,12))
				loc6_12++;
			else if(selected == board.calcIndex(8,14))
				loc8_14++;
			else if(selected == board.calcIndex(5,15))
				loc5_15++;
			else
				fail("Invalid target selected");
		}
		
		assertTrue(loc6_12 > 10);
		assertTrue(loc8_14 > 10);
		assertTrue(loc5_15 > 10);
	}
	
	// Ensure computer chooses room if possible, unless it has been visited immediately before
	@Test
	public void TestPickLocation2() {
		ComputerPlayer comp = new ComputerPlayer("Mrs. White", "Red", 0, 0);
		board.startTargets(board.calcIndex(4,1),3);
		
		for(int i = 0; i < 10; i++) {
			int selected = comp.pickLocation(board);
			assertTrue(selected == board.calcIndex(5, 1));
			comp.setLastRoomVisited('0');
		}
		
		for(int i = 0; i < 10; i++) {
			comp.setLastRoomVisited('C');
			int selected = comp.pickLocation(board);
			assertFalse(selected == board.calcIndex(5, 1));
		}
	}
	
	// Suggestions: one player, one correct match
	@Test
	public void TestSuggestions1() {
		ComputerPlayer comp = new ComputerPlayer("Miss Scarlett", "Red", 0, 0);
		comp.dealCard(new Card("Miss Scarlett", Card.cardType.PERSON));
		comp.dealCard(new Card("Colonel Mustard", Card.cardType.PERSON));
		comp.dealCard(new Card("Rope", Card.cardType.WEAPON));
		comp.dealCard(new Card("Dagger", Card.cardType.WEAPON));
		comp.dealCard(new Card("Library", Card.cardType.ROOM));
		comp.dealCard(new Card("Study", Card.cardType.ROOM));

		Assert.assertEquals(comp.disproveSuggestion("Miss Scarlett", "Candlestick", "Conservatory"), new Card("Miss Scarlett",Card.cardType.PERSON));
		Assert.assertEquals(comp.disproveSuggestion("Mrs. White", "Rope", "Conservatory"), new Card("Rope", Card.cardType.WEAPON));
		Assert.assertEquals(comp.disproveSuggestion("Mrs. White", "Candlestick", "Study"), new Card("Study", Card.cardType.ROOM));
		Assert.assertEquals(comp.disproveSuggestion("Mrs. White", "Candlestick", "Conservatory"), null);
	}
	
	// Suggestions: one player, multiple matches
	@Test
	public void TestSuggestions2() {
		ComputerPlayer comp = new ComputerPlayer("Miss Scarlett", "Red", 0, 0);
		comp.dealCard(new Card("Miss Scarlett", Card.cardType.PERSON));
		comp.dealCard(new Card("Colonel Mustard", Card.cardType.PERSON));
		comp.dealCard(new Card("Rope", Card.cardType.WEAPON));
		comp.dealCard(new Card("Dagger", Card.cardType.WEAPON));
		comp.dealCard(new Card("Library", Card.cardType.ROOM));
		comp.dealCard(new Card("Study", Card.cardType.ROOM));
		
		int PlayerA = 0;
		int Rope = 0;
		int Library = 0;
		for(int i = 0; i < 20; i++) {
			Card testCard = comp.disproveSuggestion("Miss Scarlett", "Rope", "Library");
			if(testCard.equals(new Card("Miss Scarlett", Card.cardType.PERSON)))
				PlayerA++;
			else if(testCard.equals(new Card("Rope", Card.cardType.WEAPON)))
				Rope++;
			else if(testCard.equals(new Card("Library", Card.cardType.ROOM)))
				Library++;
			else
				fail("Not a valid card");
		}
		
		assertTrue(PlayerA > 0);
		assertTrue(Rope > 0);
		assertTrue(Library > 0);
	}
	
	// Suggestions: test all players queried
	@Test
	public void TestSuggestions3() {
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new HumanPlayer("Miss Scarlett", "Red", 0, 0));
		testPlayers.add(new ComputerPlayer("Colonel Mustard", "Yellow", 0, 1));
		testPlayers.add(new ComputerPlayer("Mrs. White", "White", 0, 2));
		testPlayers.add(new ComputerPlayer("Mrs. Peacock", "Blue", 0, 3));
		testPlayers.add(new ComputerPlayer("Reverend Green", "Green", 0, 3));
		testPlayers.add(new ComputerPlayer("Professor Plum", "Purple", 0, 3));
		
		testPlayers.get(0).dealCard(new Card("Miss Scarlett", Card.cardType.PERSON));
		testPlayers.get(1).dealCard(new Card("Colonel Mustard", Card.cardType.PERSON));
		testPlayers.get(2).dealCard(new Card("Rope", Card.cardType.WEAPON));
		testPlayers.get(3).dealCard(new Card("Library", Card.cardType.ROOM));
		
		game.setPlayers(testPlayers);
		
		Assert.assertEquals(game.handleSuggestion("Miss Scarlett", "Study", "Lead Pipe", testPlayers.get(1)), new Card("Miss Scarlett", Card.cardType.PERSON));
		Assert.assertEquals(game.handleSuggestion("Mrs. White", "Study", "Lead Pipe", testPlayers.get(1)), null);
		Assert.assertEquals(game.handleSuggestion("Colonel Mustard", "Study", "Lead Pipe", testPlayers.get(1)), null);
		
		int comp1 = 0;
		int comp2 = 0;
		int comp3 = 0;
		
		for(int i = 0; i < 20; i++) {
			Card testCard = game.handleSuggestion("Miss Scarlett", "Library", "Rope", testPlayers.get(1));
			System.out.println(comp1 + " " + comp2 + " " + comp3);
			if (testCard.equals(new Card("Miss Scarlett", Card.cardType.PERSON)))
				comp1++;
			else if (testCard.equals(new Card("Rope", Card.cardType.WEAPON)))
				comp2++;
			else if (testCard.equals(new Card("Library",Card.cardType.ROOM)))
				comp3++;
			else
				fail("Invalid card returned");
		}
		
		assertTrue(comp1 > 0);
		assertTrue(comp2 > 0);
	}
	
	// Suggestions: test computer suggestions
	public void TestCompSuggestions() {
		ComputerPlayer comp = new ComputerPlayer("Miss Scarlett", "Red", 0, 0);
		comp.setRoom('C');
		
		comp.updateSeen(new Card("Miss Scarlett", Card.cardType.PERSON));
		comp.updateSeen(new Card("Colonel Mustard", Card.cardType.PERSON));
		comp.updateSeen(new Card("Rope", Card.cardType.WEAPON));
		comp.updateSeen(new Card("Dagger", Card.cardType.WEAPON));
		
		int white = 0;
		int green = 0;
		int wrench = 0;
		int revolver = 0;
		
		for(int i = 0; i < 100; i++) {
			comp.createSuggestion();
			Solution suggestion = comp.getSuggestion();

			Assert.assertEquals(suggestion.getRoom(), "Conservatory");
			if(comp.getSeen().contains(suggestion.getPerson())) {
				if(comp.getSeen().contains(suggestion.getWeapon()))
					fail("Invalid computer suggestion");
			}
			if(suggestion.getPerson().equals("Mrs. White"))
				white++;
			if(suggestion.getPerson().equals("Reverend Green"))
				green++;
			if(suggestion.getWeapon().equals("Wrench"))
				wrench++;
			if(suggestion.getPerson().equals("Revolver"))
				revolver++;
		}
		
		assertTrue(white > 0 && green > 0 && wrench > 0 && revolver > 0);
					
		comp.updateSeen(new Card("Mrs. White", Card.cardType.PERSON));
		comp.updateSeen(new Card("Reverend Green", Card.cardType.PERSON));
		comp.updateSeen(new Card("Mrs. Peacock", Card.cardType.PERSON));
		comp.updateSeen(new Card("Candlestick", Card.cardType.WEAPON));
		comp.updateSeen(new Card("Lead Pipe", Card.cardType.WEAPON));
		comp.updateSeen(new Card("Revolver", Card.cardType.WEAPON));
		
		comp.createSuggestion();
		Solution suggestion = comp.getSuggestion();
		
		Assert.assertEquals(suggestion.getRoom(), "Conservatory");
		Assert.assertEquals(suggestion.getPerson(), "Professor Plum");
		Assert.assertEquals(suggestion.getWeapon(), "Wrench");
	}
}
