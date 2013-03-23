package clueGame;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import clueGame.RoomCell.DoorDirection;
//import experiment.Integer;


public class Board {
	
	private ArrayList<BoardCell> cells;
	private Map<Character, String> rooms;
	private ArrayList<Integer> adjSpots;
	private int numRows;
	private int numColumns;
	private boolean [] visited;
	private ArrayList<Integer> targetList;
	
	public Board() {
		numRows = 0;
		numColumns = 0;
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		adjSpots  = new ArrayList<Integer>();
		targetList = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getAdjSpots() {
		return adjSpots;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public void loadConfigFiles() throws FileNotFoundException {
		
		//System.out.println("Opening files.");
		
		String line = null;
		String[] legend;
		//Scanner layout = readFile("Out_Board.csv");
		//Scanner legend = readFile("ClueLegend.txt");
		Scanner scanner = null;
		scanner = readFile("Out_Board.csv");
		
		try{
		while(scanner.hasNext()){
			line = scanner.nextLine();
			legend = line.split(",");
			numColumns = legend.length;
			for (int i = 0; i<numColumns; i++){
				if(legend[i].charAt(0) == 'W') {
					WalkwayCell cell = new WalkwayCell(numRows, i);
					cells.add(cell);
				} else {
					RoomCell cell = new RoomCell(numRows,i, legend[i]);
					cells.add(cell);
				}
			}
			numRows++;
		
		}
		}catch(BadConfigFormatException e){
			System.out.println("Bad file format for ClueLayout.csv");
			
		}
		
		scanner = readFile("ClueLegend.txt");
		
		try{
			while(scanner.hasNext()){
				line = scanner.nextLine();
				legend = line.split(", ");
				rooms.put(legend[0].charAt(0),legend[1]);
			}
		}catch(BadConfigFormatException e){
			System.out.println("Bad file format for ClueLegend.txt");
		}
		visited = new boolean[numRows*numColumns];
	}
	
	public int calcIndex(int rowIn, int columnIn) {
		//assuming numColumns is starts at one. assuming rowIn and columnIn start at 0.
		int index = ((rowIn)*numColumns) + (columnIn);
		return index;
	}
	
	public RoomCell getRoomCellAt(int rowIn, int columnIn) {
		if (cells.get(calcIndex(rowIn, columnIn)).isWalkway()) {
			return new RoomCell(12,11, "X");
		}
		return (RoomCell) cells.get(calcIndex(rowIn,columnIn));
	}

	public ArrayList<BoardCell> getCells() {
		return cells;
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}
	
	private Scanner readFile(String file){
		Scanner in = null;
		try {
			FileReader reader = new FileReader(file);
			in = new Scanner(reader);
			
		} catch (FileNotFoundException e){
			System.out.println("Can't open file: " + file);
		}	
		return in;
		
	}
	
	
	
	
	public ArrayList<Integer> getAdjList(int index){
		ArrayList<Integer> adjList = new ArrayList<Integer>();

		//System.out.println(index);

		//System.out.println("testing");

			if(index%numColumns != 22) {
				if((this.getCells().get(index).isWalkway() && (this.getCells().get(index+1).isWalkway() || ( 
						(this.getRoomCellAt((index+1)/numColumns,
								(index+1)% numColumns).getDoorDirection() == DoorDirection.LEFT)))) || 
								this.getRoomCellAt((index)/numColumns,
										(index)% numColumns).getDoorDirection() == DoorDirection.RIGHT) {
					adjList.add(index+1);
				}
			}

			if(index%numColumns != 0) {
				if((this.getCells().get(index).isWalkway() && this.getCells().get(index-1).isWalkway() || ( 
						(this.getRoomCellAt((index-1)/numColumns,
								(index-1)% numColumns).getDoorDirection() == DoorDirection.RIGHT))) ||
								this.getRoomCellAt((index)/numColumns,
										(index)% numColumns).getDoorDirection() == DoorDirection.LEFT)
				{
					adjList.add(index-1);
				}
			}

			if(index < ((numRows-1) * numColumns)) {
				if((this.getCells().get(index).isWalkway() && this.getCells().get(index+numColumns).isWalkway() || ( 
						(this.getRoomCellAt((index+numColumns)/numColumns,
								(index+numColumns)% numColumns).getDoorDirection() == DoorDirection.UP))) ||
								this.getRoomCellAt((index)/numColumns,
										(index)% numColumns).getDoorDirection() == DoorDirection.DOWN)
				{
					adjList.add(index+numColumns);
				}
			}

			if(index >= numColumns) {
				if((this.getCells().get(index).isWalkway() && this.getCells().get(index-numColumns).isWalkway() || ( 
						(this.getRoomCellAt((index-numColumns)/numColumns,
								(index-numColumns)% numColumns).getDoorDirection() == DoorDirection.DOWN))) ||
								this.getRoomCellAt((index)/numColumns,
										(index)% numColumns).getDoorDirection() == DoorDirection.UP) {
					adjList.add(index-numColumns);
				}
			}

		
		
		return adjList;
		
	}
	
	
	public void startTargets(int indexIn, int stepsIn){
		visited[indexIn] = true;
		
		for(int i = 0; i < this.getAdjList(indexIn).size(); i++) {	
			if (this.getCells().get(this.getAdjList(indexIn).get(i)).isDoorway() && !targetList.contains(this.getAdjList(indexIn).get(i)) 
					&& !visited[(this.getAdjList(indexIn).get(i))]) {
				//System.out.println("Triggered" + this.getAdjList(indexIn).get(i));
				targetList.add(this.getAdjList(indexIn).get(i));
			}
		}

		if(stepsIn == 1) {
			for(int i = 0; i < this.getAdjList(indexIn).size(); i++) {
				if(!targetList.contains(this.getAdjList(indexIn).get(i)) && !visited[(this.getAdjList(indexIn).get(i))]){
					targetList.add(this.getAdjList(indexIn).get(i));
				}
			}
		} else {
			for(int i = 0; i < this.getAdjList(indexIn).size(); i++) {
				if(!visited[(this.getAdjList(indexIn).get(i))]) {
					startTargets(this.getAdjList(indexIn).get(i), stepsIn - 1);
				}
			}
		}
		//System.out.println("done with" + indexIn);
		visited[indexIn] = false;
	}

	
	public ArrayList<Integer> getTargets(){
		//System.out.println(targetList.get(0));
		return targetList;
	}	
		
	public BoardCell getCellAt(int index){
		return new BoardCell(0,0);
		
	}
	
	public void generateAdj(){
		int index;
		for(int n = 0; n < numColumns*numRows; n++) {
			index = n;
			
			if(index%numColumns != (this.numColumns - 1)) {
				if((this.getCells().get(index).isWalkway() && (this.getCells().get(index+1).isWalkway() || ( 
						(this.getRoomCellAt((index+1)/numColumns,
								(index+1)% numColumns).getDoorDirection() == DoorDirection.LEFT)))) || 
								this.getRoomCellAt((index)/numColumns,
										(index)% numColumns).getDoorDirection() == DoorDirection.RIGHT) {
					adjSpots.add(index+1);
				}
			}
			
			if(index%numColumns != 0) {
				if((this.getCells().get(index).isWalkway() && this.getCells().get(index-1).isWalkway() || ( 
						(this.getRoomCellAt((index-1)/numColumns,
								(index-1)% numColumns).getDoorDirection() == DoorDirection.RIGHT))) ||
								this.getRoomCellAt((index)/numColumns,
										(index)% numColumns).getDoorDirection() == DoorDirection.LEFT)
								{
					adjSpots.add(index-1);
				}
			}

			if(index < ((numRows-1) * numColumns)) {
				if((this.getCells().get(index).isWalkway() && this.getCells().get(index+numColumns).isWalkway() || ( 
						(this.getRoomCellAt((index+numColumns)/numColumns,
								(index+numColumns)% numColumns).getDoorDirection() == DoorDirection.UP))) ||
								this.getRoomCellAt((index)/numColumns,
										(index)% numColumns).getDoorDirection() == DoorDirection.DOWN)
				{
					adjSpots.add(index+numColumns);
				}
			}

			if(index >= numColumns) {
				if((this.getCells().get(index).isWalkway() && this.getCells().get(index-numColumns).isWalkway() || ( 
						(this.getRoomCellAt((index-numColumns)/numColumns,
								(index-numColumns)% numColumns).getDoorDirection() == DoorDirection.DOWN))) ||
								this.getRoomCellAt((index)/numColumns,
										(index)% numColumns).getDoorDirection() == DoorDirection.UP) {
					adjSpots.add(index-numColumns);
				}
			}
			
		}
	}
}
