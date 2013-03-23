package Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.RoomCell;

public class TestBoardTest {

	//Initializing the number of columns, rows, rooms, and doorways for the board
	public static final int numbRooms = 11;
	public static final int numbCol = 23;
	public static final int numbRow = 26;
	public static final int numbDoorways = 24;
	public int actual = 0;
	public RoomCell room;
	public Board board;
	
	@Before
	public void setUp() throws FileNotFoundException {
		//Initializing the default 
		board = new Board();
		room = null;
		board.loadConfigFiles();
		
		
	}

	@Test
	public void roomTest(){
		//testing the legend mapping versus the actual values
		actual = board.getRooms().size();
		assertEquals(numbRooms, actual);
		//testing mapping of legend
		assertEquals(board.getRooms().get('C'),"Conservatory");
		assertEquals(board.getRooms().get('W'),"Walkway");
		assertEquals(board.getRooms().get('X'),"Closet");
	}
	
	@Test
	public void loadTest(){


		
		//testing actual versus values set above
		actual = board.getNumColumns();
		assertEquals(numbCol,actual);
		actual = board.getNumRows();
		assertEquals(numbRow,actual);
		
		room = board.getRoomCellAt(5,10);
		assertFalse(room.isDoorway());
		
		room = board.getRoomCellAt(25,11);
		assertTrue(room.isDoorway());
	}
	
	@Test
	public void testNumbDoor(){
		actual = 0;
		for(int i = 0; i<=(numbRow-1); i++){
			for(int c = 0; c<=(numbCol-1); c++){
				room = board.getRoomCellAt(i,c);
				if(room.isDoorway()){
					actual++;
				}
			}
		}
		assertEquals(actual,numbDoorways);
		
	}
		
	@Test
	public void testInitals(){
		room = board.getRoomCellAt(16,9);
		assertEquals(room.getInitial(),'D');
		
		room = board.getRoomCellAt(8,20);
		assertEquals(room.getInitial(),'S');
		
		room = board.getRoomCellAt(11,9);
		assertEquals(room.getInitial(),'X');
		
		assertEquals(0, board.calcIndex(0, 0));
		assertEquals(23, board.calcIndex(1,0));
		assertEquals(19, board.calcIndex(0,19));
		
	}
	


	@Test
	public void testBadRows() throws BadConfigFormatException, FileNotFoundException {
		Board testBoard = new Board();
		testBoard.loadConfigFiles();
	}
	@Test
	public void testBadLegend() throws BadConfigFormatException, FileNotFoundException {
		Board testBoard = new Board();
		testBoard.loadConfigFiles();
	}
	@Test	
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		Board testBoard = new Board();
		testBoard.loadConfigFiles();
	}
	

	@Test
	public void testWalkwaysOnly() {
		
		//Test a cell with only adjacent walkways
		ArrayList<Integer> testList = new ArrayList<Integer>();
		int size = board.getAdjList(board.calcIndex(14,19)).size();
		System.out.println("Size of: " + size);
		for(int i =0; i < size; i++) {
			testList.add(board.getAdjList(board.calcIndex(14,19)).get(i));
			System.out.println("Found:" + testList.get(i));
		}
		Assert.assertEquals(4, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(14,18)));
		Assert.assertTrue(testList.contains(board.calcIndex(14,20)));
		Assert.assertTrue(testList.contains(board.calcIndex(13,19)));
		Assert.assertTrue(testList.contains(board.calcIndex(15,19)));
		
		
	}
	
	@Test
	public void testWalls() {
		
		//Test a walkway with walls to the top and left
		ArrayList<Integer> testList = new ArrayList<Integer>();
		int size = board.getAdjList(board.calcIndex(13,2)).size();
		System.out.println("Size of: " + size);
		for(int i =0; i < size; i++) {
			testList.add(board.getAdjList(board.calcIndex(13,2)).get(i));
			System.out.println("Found:" + testList.get(i));
		}
		Assert.assertEquals(2, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(14,2)));
		Assert.assertTrue(testList.contains(board.calcIndex(13,3)));
		
		//Test a walkway with a wall below and a door to the right (that is in the wrong direction)
		testList = board.getAdjList(board.calcIndex(14,20));
		Assert.assertEquals(2, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(14,19)));
		Assert.assertTrue(testList.contains(board.calcIndex(13,20)));
		
		//Test a walkway with a wall above and 'bad' door to the left
		testList = board.getAdjList(board.calcIndex(10,18));
		Assert.assertEquals(2, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(10,19)));
		Assert.assertTrue(testList.contains(board.calcIndex(11,18)));
		
		//Test a completely isolated walkway (walls on all sides). Not realistic but tests all four wall directions.
		testList = board.getAdjList(board.calcIndex(18,8));
		Assert.assertEquals(0, testList.size());
		
	}
	
	@Test
	public void testAdjacentDoors() {

		
		//Test a walkway with an adjacent, properly oriented door below it, and walkways on other sides
		ArrayList<Integer> testList = board.getAdjList(board.calcIndex(4,1));
		Assert.assertEquals(4, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(4,0)));
		Assert.assertTrue(testList.contains(board.calcIndex(3,1)));
		Assert.assertTrue(testList.contains(board.calcIndex(4,2)));
		Assert.assertTrue(testList.contains(board.calcIndex(5,1)));
		
		//Test a walkway with an adjacent, properly oriented door to the left, and walkways on other sides
		testList = board.getAdjList(board.calcIndex(21,2));
		Assert.assertEquals(4, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(21,1)));
		Assert.assertTrue(testList.contains(board.calcIndex(21,3)));
		Assert.assertTrue(testList.contains(board.calcIndex(20,2)));
		Assert.assertTrue(testList.contains(board.calcIndex(22,2)));
		
		//Test a walkway with an adjacent, properly oriented door above it, and walkways on other sides
		testList = board.getAdjList(board.calcIndex(13,6));
		Assert.assertEquals(4, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(12,6)));
		Assert.assertTrue(testList.contains(board.calcIndex(13,5)));
		Assert.assertTrue(testList.contains(board.calcIndex(13,7)));
		Assert.assertTrue(testList.contains(board.calcIndex(14,6)));
		
		//Test a walkway with an adjacent, properly oriented door to the right, and a wall to the left
		testList = board.getAdjList(board.calcIndex(19,12));
		Assert.assertEquals(3, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(19,13)));
		Assert.assertTrue(testList.contains(board.calcIndex(18,12)));
		Assert.assertTrue(testList.contains(board.calcIndex(20,12)));
				
	}
	
	@Test
	public void testBoardEdge() {
		System.out.println("Testing edges");
		//Test a walkway on the left edge of the board, with a wall above it
		ArrayList<Integer> testList = board.getAdjList(board.calcIndex(3,0));
		Assert.assertEquals(2, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(4,0)));
		Assert.assertTrue(testList.contains(board.calcIndex(3,1)));
		
		//test a walkway on the bottom with walls on left and right
		testList = board.getAdjList(board.calcIndex(25,7));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(24,7)));
		
		//test a walkway on the right with no surrounding walls
		testList = board.getAdjList(board.calcIndex(11,22));
		Assert.assertEquals(3, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(11,21)));
		Assert.assertTrue(testList.contains(board.calcIndex(10,22)));
		Assert.assertTrue(testList.contains(board.calcIndex(12,22)));
				
		//test a walkway on the top with a door (oriented correctly) next to it.
		testList = board.getAdjList(board.calcIndex(0,12));
		Assert.assertEquals(3, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(0,11)));
		Assert.assertTrue(testList.contains(board.calcIndex(0,13)));
		Assert.assertTrue(testList.contains(board.calcIndex(1,12)));
	}
	
	@Test
	public void testExitingDoor() {
		
		//Test a door that exits down, with an additional walkway to the right
		ArrayList<Integer> testList = board.getAdjList(board.calcIndex(12,7));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(13,7)));
		
		//Test a door that exits left
		testList = board.getAdjList(board.calcIndex(19,13));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(19,12)));
				
		//Test a door that exits up, with an additional walkway to the left
		testList = board.getAdjList(board.calcIndex(14,21));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(13,21)));	
		
		//Test a door that exits left
		testList = board.getAdjList(board.calcIndex(21,17));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(21,18)));
		
	}
	
	@Test
	public void testTargetsWalkways() {
		
		//System.out.println("Walkways");
		
		//Test a non-branching walkway that terminates before steps run out, 5 Steps.
		board.startTargets(board.calcIndex(4,19),5);
		ArrayList<Integer> targets= board.getTargets();
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.calcIndex(9,19)));
		
		//System.out.println("Walkways2");
		
		targets.clear();
		
		//Test a branching walkway, 2 steps.
		board.startTargets(board.calcIndex(16,1),2);
		targets= board.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.calcIndex(17,0)));
		Assert.assertTrue(targets.contains(board.calcIndex(15,2)));
		Assert.assertTrue(targets.contains(board.calcIndex(16,3)));
		Assert.assertTrue(targets.contains(board.calcIndex(17,2)));
		
		targets.clear();
		
		//Test a hallway that branches multiple times, 4 steps.
		board.startTargets(board.calcIndex(14,14),4);
		targets= board.getTargets();
		Assert.assertEquals(5, targets.size());
		Assert.assertTrue(targets.contains(board.calcIndex(14,10)));
		Assert.assertTrue(targets.contains(board.calcIndex(16,12)));
		Assert.assertTrue(targets.contains(board.calcIndex(10,14)));
		Assert.assertTrue(targets.contains(board.calcIndex(11,15)));
		Assert.assertTrue(targets.contains(board.calcIndex(12,16)));
		
		targets.clear();
		
		//Test a mostly-open hallway, 3 steps
		board.startTargets(board.calcIndex(12,19),3);
		targets= board.getTargets();
		Assert.assertEquals(14, targets.size());
		Assert.assertTrue(targets.contains(board.calcIndex(12,16)));
		Assert.assertTrue(targets.contains(board.calcIndex(11,17)));
		Assert.assertTrue(targets.contains(board.calcIndex(10,18)));
		Assert.assertTrue(targets.contains(board.calcIndex(9,19)));
		Assert.assertTrue(targets.contains(board.calcIndex(11,21)));
		Assert.assertTrue(targets.contains(board.calcIndex(12,20)));
		Assert.assertTrue(targets.contains(board.calcIndex(13,21)));
		Assert.assertTrue(targets.contains(board.calcIndex(14,20)));
		Assert.assertTrue(targets.contains(board.calcIndex(15,19)));
		Assert.assertTrue(targets.contains(board.calcIndex(14,18)));
		Assert.assertTrue(targets.contains(board.calcIndex(11,19)));
		Assert.assertTrue(targets.contains(board.calcIndex(12,18)));
		Assert.assertTrue(targets.contains(board.calcIndex(13,19)));
		Assert.assertTrue(targets.contains(board.calcIndex(12,20)));

		
	
		
	}
	
	@Test 
	public void testTargetsEnterRoom() {
		
		//Test a location that can enter a single room cell
		board.startTargets(board.calcIndex(21,12),3);
		ArrayList<Integer> targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.calcIndex(24,12)));
		Assert.assertTrue(targets.contains(board.calcIndex(18,12)));
		Assert.assertTrue(targets.contains(board.calcIndex(19,13)));
		
		targets.clear();
		
		//System.out.println("Part2");
		
		//Test a location that can enter a number of room cells, in different rooms
		board.startTargets(board.calcIndex(14,6),2);
		targets= board.getTargets();
		
		Assert.assertTrue(targets.contains(board.calcIndex(15,6)));
		Assert.assertTrue(targets.contains(board.calcIndex(15,5)));
		Assert.assertTrue(targets.contains(board.calcIndex(15,7)));
		Assert.assertTrue(targets.contains(board.calcIndex(14,4)));
		Assert.assertTrue(targets.contains(board.calcIndex(14,8)));
		Assert.assertTrue(targets.contains(board.calcIndex(13,5)));
		Assert.assertTrue(targets.contains(board.calcIndex(13,7)));
		Assert.assertTrue(targets.contains(board.calcIndex(12,6)));
		Assert.assertEquals(8, targets.size());
	}
	
	@Test
	public void testTargetsExitRoom() {
		
		//Test a room that can exit to walkways only
		board.startTargets(board.calcIndex(9,21),3);
		ArrayList<Integer> targets= board.getTargets();
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.calcIndex(11,22)));
		
		targets.clear();

		//Test a room that can exit to different room cells of various rooms (including different cells of a same room)
		board.startTargets(board.calcIndex(15,5),3);
		targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.calcIndex(14,3)));
		Assert.assertTrue(targets.contains(board.calcIndex(13,4)));
		Assert.assertTrue(targets.contains(board.calcIndex(12,5)));
		Assert.assertTrue(targets.contains(board.calcIndex(13,6)));
		Assert.assertTrue(targets.contains(board.calcIndex(14,7)));
		Assert.assertTrue(targets.contains(board.calcIndex(15,6)));

	}
	
	
	
	
	
}
