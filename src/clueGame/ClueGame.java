package clueGame;

import java.util.*;
import java.io.*;

import clueGame.Card.cardType;

public class ClueGame {
	
	private ArrayList<Player> players;
	private ArrayList<Card> deck;
	private Map<String,ArrayList<Card>> sortedDeck;
	private int currentPlayer;
	private Solution answer;
	
	public ClueGame() {
		this.players = new ArrayList<Player>();
		this.deck = new ArrayList<Card>();
		this.currentPlayer = 0;
		this.answer = null;
		this.sortedDeck = new HashMap<String,ArrayList<Card>>();
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Solution getAnswer() {
		return answer;
	}
	
	public void setAnswer(Solution answer) {
		this.answer = answer;
	}

	public void deal() {
		int j = new Random().nextInt(6);
		for(int i = 0; i < deck.size(); i++) {
			players.get(j).dealCard(deck.get(i));
			if(j == players.size() - 1)
				j = 0;
			else
				j++;
		}
	}
	
	public void loadConfigFiles() throws FileNotFoundException {
		// Gets room cards
		File infile = new File("ClueLegend.txt");
		Scanner scanner = new Scanner(infile);
		ArrayList<Card> tempCards = new ArrayList<Card>();
		while(scanner.hasNextLine()){
            String[] item = scanner.nextLine().split(",");
            item[1] = item[1].trim();
            if(!item[0].equals("W") && !item[0].equals("X")) {
            	deck.add(new Card(item[1],cardType.ROOM));
            	tempCards.add(new Card(item[1],cardType.ROOM));
            }
		}
		
		sortedDeck.put("Rooms",tempCards);
		tempCards = new ArrayList<Card>();
		
		// Loads players and player cards
		infile = new File("Players.txt");
		scanner = new Scanner(infile);
		int i = 0;
		while(scanner.hasNextLine()){
            String[] item = scanner.nextLine().split(",");
            if(i == 0)
            	players.add(new HumanPlayer(item[0],item[1],Integer.parseInt(item[2]),Integer.parseInt(item[3])));
            else
            	players.add(new ComputerPlayer(item[0],item[1],Integer.parseInt(item[2]),Integer.parseInt(item[3])));
            deck.add(new Card(item[0],cardType.PERSON));
            tempCards.add(new Card(item[0],cardType.PERSON));
            i++;
		}
		
		sortedDeck.put("Players",tempCards);
		tempCards = new ArrayList<Card>();
		
		// Loads weapons
		infile = new File("Weapons.txt");
		scanner = new Scanner(infile);
		while(scanner.hasNextLine()) {
			String weapon = scanner.nextLine();
			deck.add(new Card(weapon,cardType.WEAPON));
			tempCards.add(new Card(weapon,cardType.WEAPON));
		}
		
		Collections.shuffle(deck, new Random());
		deal();
	}
	
	public void selectAnswer() {
		int numRooms = sortedDeck.get("Rooms").size();
		int numWeapons = sortedDeck.get("Weapons").size();
		int numPlayers = sortedDeck.get("Players").size();
		Random random = new Random();
		
		String room = sortedDeck.get("Rooms").get(random.nextInt(numRooms)).getName();
		String weapon = sortedDeck.get("Weapons").get(random.nextInt(numWeapons)).getName();
		String player = sortedDeck.get("Players").get(random.nextInt(numPlayers)).getName();
		answer = new Solution(room,weapon,player);
	}
	
	public Card handleSuggestion(String person, String room, String weapon, Player accusingPerson) {
		int startingPlayer = new Random().nextInt(players.size());
		System.out.println(startingPlayer);
		Card card = null;
		for(int i = 0; i < players.size(); i++) {
			if(players.get(startingPlayer).equals(accusingPerson)) {
				startingPlayer++;
				continue;
			}
			card = players.get(startingPlayer).disproveSuggestion(person, room, weapon);
			if(card != null)
				return card;
			if(startingPlayer == players.size() - 1)
				startingPlayer = 0;
			else
				startingPlayer++;
		}
		return null;
	}
	
	public boolean checkAccusation(Solution solution) {
		return solution.equals(answer);
	}
}