/*
 * File: Adventure.java
 * --------------------
 * This program plays the Adventure game from Assignment #4.
 */

import java.io.*;
import java.util.*;

/* Class: Adventure */
/**
 * This class is the main program class for the Adventure game.
 */

public class Adventure {
	
	private static final String fileTinyRoom = "TinyRooms.txt"; 
	private static final String fileSmallRoom = "SmallRooms.txt"; 
	private static final String fileSmallObject = "SmallObjects.txt";
	private static final String fileSmallSynonym = "SmallSynonyms.txt"; 
	private static final String fileCrowtherRoom = "CrowtherRooms.txt";
	private static final String fileCrowtherObject = "CrowtherObjects.txt";
	private static final String fileCrowtherSynonym = "CrowtherSynonyms.txt";
	
	private List<AdvRoom> rooms = new ArrayList<AdvRoom>();
	private List<AdvObject> objects = new ArrayList<AdvObject>();
	private Map<String,AdvObject> objectMap = new HashMap<String,AdvObject>();
	private Map<String, AdvCommand> actionVerbMap = new HashMap<String, AdvCommand>(); 
	private Map<String,String> synonyms = new HashMap<String,String>();

	private int currentRoomNum;
	private AdvRoom currentRoom;
	private List<AdvObject> inventory = new ArrayList<AdvObject>();
	private boolean quit = false;
	
	// Use this scanner for any console input
	private static Scanner scan = new Scanner(System.in);

	/**
	 * This method is used only to test the program
	 */
	public static void setScanner(Scanner theScanner) {
		scan = theScanner;
		// Delete the following line when done
		AdventureStub.setScanner(theScanner);
	}

	/**
	 * Runs the adventure program
	 */
	public static void main(String[] args) {
		//AdventureStub.main(args); // Replace with your code
		
		try{
			// create an instance for Adventure game
			Adventure game = new Adventure();
			
			//-----------------------------------------------
			// Ask the user for the name of an adventure game
			//-----------------------------------------------
			System.out.print("What will be your adventure today? ");
			String input = scan.nextLine().toUpperCase();
			
			//---------------------------------------------------------------------
			// Read in the data files for the game into an internal data structure
			//---------------------------------------------------------------------
			//set file path
			File fileRoom = null, fileObject = null, fileSynonym = null;
			Scanner scanRoom = null, scanObject = null, scanSynonym = null;
			
			switch(input) {
			case "CROWTHWE":
				fileRoom = new File(fileCrowtherRoom);
				fileObject = new File(fileCrowtherObject);
				fileSynonym = new File(fileCrowtherSynonym);
				scanRoom = new Scanner(fileRoom);
				scanObject = new Scanner(fileObject);
				scanSynonym = new Scanner(fileSynonym);
				break;
			case "SMALL":
				fileRoom = new File(fileSmallRoom);
				fileObject = new File(fileSmallObject);
				fileSynonym = new File(fileSmallSynonym);
				scanRoom = new Scanner(fileRoom);
				scanObject = new Scanner(fileObject);
				scanSynonym = new Scanner(fileSynonym);
				break;
			case "TINY":
				fileRoom = new File(fileTinyRoom);
				scanRoom = new Scanner(fileRoom);
				break;
			default:
				throw new Exception("Invalid Command");
			}
			
			// read Room file
			if(scanRoom!=null) {
				while(scanRoom.hasNextLine()){
					game.rooms.add(AdvRoom.readFromFile(scanRoom));
				}
			}

			// read Object file
			if(scanRoom!=null) {
				while(scanObject.hasNextLine()){
					game.objects.add(AdvObject.readFromFile(scanObject));
				}
			}

			// read Synonym file
			if(scanSynonym!=null) {
				while(scanSynonym.hasNextLine()){
					// split string into key and value by "="
					String[] strs = (scanSynonym.nextLine()).split("=");
					game.synonyms.put(strs[0],strs[1]);
				}
			}
			
			//------------------------------------
			// Organize the data into useful form
			//------------------------------------
			// set up the action verb HashMap
			// set built in commands
			game.actionVerbMap.put("QUIT", AdvCommand.QUIT);
			game.actionVerbMap.put("LOOK", AdvCommand.LOOK);
			game.actionVerbMap.put("INVENTORY", AdvCommand.INVENTORY);
			game.actionVerbMap.put("TAKE", AdvCommand.TAKE);
			game.actionVerbMap.put("DROP", AdvCommand.DROP);
			game.actionVerbMap.put("HELP", AdvCommand.HELP);

			// set motion commands by going through the motion table for each room
			for(AdvRoom room : game.rooms) {
				AdvMotionTableEntry[] table = room.getMotionTable();
				for(AdvMotionTableEntry t : table) {
					game.actionVerbMap.put(t.getDirection(),new AdvMotionCommand(t.getDirection())); 
				}
			}
			
			// set up the object HashMap
			for(AdvObject obj : game.objects) {
				game.objectMap.put(obj.getName(), obj);
			}

			//---------------------------------------------------------------------
			// Play the game by reading and executing commands entered by the user
			//---------------------------------------------------------------------
			// start game from room 1
			game.moveRoom(1);
					
			// continue asking for inputs until game over
			while(game.currentRoomNum!=0) {
				
				// if the current room has the FORCED motion then directly move to the next room
				if(game.actionVerbMap.containsKey("FORCED")) {
					game.actionVerbMap.get("FORCED").execute(game, null);
					if(game.currentRoom.getRoomNumber() != game.currentRoomNum) {
						continue;	// move to next loop
					}
				}
				
				// ask for the input
				System.out.print("> ");
				String[] inputs = (scan.nextLine().toUpperCase()).split(" ");
				String command = inputs[0];
				AdvObject obj = null;
				if(inputs.length > 1) {
					// if an object is specified, look that up in the object HashMap
					if(game.objectMap.containsKey(inputs[1])){
						obj = game.objectMap.get(inputs[1]);
					}
				}
				
				// look up the action verb in the Action Verb HashMap
				if(game.actionVerbMap.containsKey(command)) {
					// call the execute method of the command
					game.actionVerbMap.get(command).execute(game, obj);
				}else {
					//invalid command, ask for another input
					System.out.println("Unavailable command.");
					System.out.print("> ");
					input = scan.nextLine().toUpperCase();
				}
			}
			
		}catch(FileNotFoundException e){
			  System.out.println(e);
		}catch(Exception e) {
			String exceptionMessage = e.getClass().getName() + ": " + e.getMessage();
			System.out.println(exceptionMessage);
		}
		
	}

	public void moveRoom(int destinationNum) {
		// set the current room number to the destination room number
		currentRoomNum = destinationNum;
		
		// set the currentRoom
		currentRoom = rooms.get(currentRoomNum - 1);
		
		// if the room has not been visited
		if(!currentRoom.hasBeenVisited()) {
			// print the description of the room 
			String[] description = currentRoom.getDescription();
			for(int i = 0; i < description.length; i++) {
				System.out.println(description[i]);
			}
			// set the flag to visited
			currentRoom.setVisited(true);
		// if the room has been visited
		}else {
			// print the name of the room
			System.out.println(currentRoom.getName());
		}			
	}
	
	/* Method: executeMotionCommand(direction) */
	/**
	 * Executes a motion command. This method is called from the
	 * AdvMotionCommand class to move to a new room.
	 * 
	 * @param direction
	 *            The string indicating the direction of motion
	 */
	public void executeMotionCommand(String direction) {
		//super.executeMotionCommand(direction); // Replace with your code
//		if(currentRoom != null) {
//			for(int i = 0; i < currentRoom.getMotionTable().length; i++) {
//				if(currentRoom.getMotionTable()[i].equals(direction)) {
//					
//				
//				}
//			}
//		}
		
		// get motion table of the room
		AdvMotionTableEntry[] table = currentRoom.getMotionTable();
		
		for(AdvMotionTableEntry entry : table) {
			// if matched direction is found
			if(entry.getDirection().equals(direction)) {
				// if there is a locked passage
				if(entry.getKeyName()!=null){
					// check if the player has the key item
					for(AdvObject obj : inventory) {
						if(obj.getName().equals(entry.getKeyName())) {
							// have the key item -> move to the next room
							moveRoom(entry.getDestinationRoom());
							break;
						}
					}
					// does not have key item -> check next entry
					continue;
				}
				// move to the next room
				moveRoom(entry.getDestinationRoom());
				break;
			}
		}
	}

	/* Method: executeQuitCommand() */
	/**
	 * Implements the QUIT command. This command should ask the user to confirm
	 * the quit request and, if so, should exit from the play method. If not,
	 * the program should continue as usual.
	 */
	public void executeQuitCommand() {
//		super.executeQuitCommand(); // Replace with your code
		System.out.println("Do you want to QUIT? (y / n)");
		String answer = scan.nextLine();
		if(answer.equals("y")) {
			currentRoomNum = 0;
		} else if (answer.equals("n")) {
			return;
		} else {
			System.out.println("Please ansewr with typing y or n");
			executeQuitCommand();
		}
	}

	/* Method: executeHelpCommand() */
	/**
	 * Implements the HELP command. Your code must include some help text for
	 * the user.
	 */
	public void executeHelpCommand() {
		// list up all possible commands
		AdvMotionTableEntry[] table = currentRoom.getMotionTable();
		for(AdvMotionTableEntry entry : table) {
			System.out.print("\"" + entry.getDirection() + "\" " );
		}
		System.out.println();
		System.out.println("To see the description of the room and items in the room, type \"LOOK\" ");
		System.out.println("To take an item in the room, type \"TAKE\" ");
		System.out.println("To drop an item in the inventory, type \"DROP\" ");
		System.out.println("To see what you have in the inventory, type \"INVENTORY\"");
		System.out.println("To quit, type \"QUIT\" ");
		System.out.println();
	}

	/* Method: executeLookCommand() */
	/**
	 * Implements the LOOK command. This method should give the full description
	 * of the room and its contents.
	 */
	public void executeLookCommand() {
//		super.executeLookCommand(); // Replace with your code
		if(currentRoom != null) {
			// description
			String[] strs = currentRoom.getDescription();
			for(int i = 0 ; i < strs.length; i++){
				System.out.println(strs[i]);				
			}
			// items
			for(int i = 0; i < currentRoom.getObjectCount(); i++) {
				System.out.println("There is(are)" + currentRoom.getObject(i) + "in the room.");
			}
		}
	}

	/* Method: executeInventoryCommand() */
	/**
	 * Implements the INVENTORY command. This method should display a list of
	 * what the user is carrying.
	 */
	public void executeInventoryCommand() {
//		super.executeInventoryCommand(); // Replace with your code
		if (!inventory.isEmpty()) {
			for(int i = 0; i < inventory.size(); i++) {
				System.out.println(inventory.get(i).getName());
			}
		} else {
			System.out.println("Empty");
		}
	}

	/* Method: executeTakeCommand(obj) */
	/**
	 * Implements the TAKE command. This method should check that the object is
	 * in the room and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to take
	 */
	public void executeTakeCommand(AdvObject obj) {
//		super.executeTakeCommand(obj); // Replace with your code
		if(currentRoom.containsObject(obj)) {
			System.out.println("Taken.");
			inventory.add(obj);
			currentRoom.removeObject(obj);
		} else {
			System.out.println("It is not here.");
		}
	}

	/* Method: executeDropCommand(obj) */
	/**
	 * Implements the DROP command. This method should check that the user is
	 * carrying the object and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to drop
	 */
	public void executeDropCommand(AdvObject obj) {
//		super.executeDropCommand(obj); // Replace with your code
		if(!inventory.isEmpty()) {
			for(int i = 0; i < inventory.size(); i++) {
				if(inventory.get(i).getName().equals(obj)) {
					System.out.println("Dropped.");
					inventory.remove(obj);
					currentRoom.addObject(obj);
				}
			}
		} else {
			System.out.println("You do not have" + obj.getName() + "to drop.");
		}
			
	}

	/* Private instance variables */
	// Add your own instance variables here
}
