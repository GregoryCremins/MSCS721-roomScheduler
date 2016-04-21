package main.java.com.marist.mscs721;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


/**
 * Class to handle scheduling rooms and meetings
 * @author Greg Cremins/ Mike Gildein
 * @version 1-27-2016
 */ 
public class RoomScheduler {
	//keyboard reader
	protected static Scanner keyboard = new Scanner(System.in);
	
	/**
	 * Main function to run the scheduler
	 * @param args defaulted to null
	 */
	public static void main(String[] args) {

		final Logger logger  = Logger.getLogger(RoomScheduler.class.getName());
		if(args.length > 0)
		{
			keyboard = new Scanner(new ByteArrayInputStream(args[0].getBytes()));
		}
		//loop until end occurs
		Boolean end = false;
		ArrayList<Room> rooms = new ArrayList<Room>();
		while (!end) {
			switch (mainMenu(logger)) {
			case 1:
				addRoom(rooms, logger);
				break;
			case 2:
				removeRoom(rooms, logger);
				break;
			case 3:
				logger.info(scheduleRoom(rooms, logger));
				break;
			case 4:
				System.out.println(listSchedule(rooms, logger));
				break;
			case 5:
				System.out.println(listRooms(rooms));
				break;
			case 6:
				exportRooms(rooms, logger);
				break;
			case 7:
				rooms = importRooms(logger);
				break;
			case 0:
				end = true;
				break;
			default:
				logger.warning("Invalid selection: Please select a number 1-7. Or 0 to exit");
				break;
			}
		}
	}
	/**
	 * Function to handle importing room list from a json file
	 * @param logger the logger for the system
	 * @return the imported list of rooms and schedules
	 * Used GSON for importer and exporter, see https://github.com/google/gson
	 * Some help from: http://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
	 */
	protected static ArrayList<Room> importRooms(Logger logger)
	{
		ArrayList<Room> finalRooms = new ArrayList<Room>();
		//get filename
		boolean inputVerify = false;
		String filename = "";
		while(!inputVerify)
		{
			System.out.println("Please specify the full pathname and filename of the file you wish to read from:");
			 filename = keyboard.next();
			if(filename.contains(".json"))
			{
				inputVerify = true;
			}
			else
			{
				logger.warning("Please specify a .JSON file.");
			}
		}
		//read from file
		Gson gson = new Gson(); 
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			ArrayList<?> rooms = gson.fromJson(br, ArrayList.class);
			for(Object r: rooms)
			{
				finalRooms.add(new Room(r.toString()));
			}

		}
		//catch if unable to find file
		catch(IOException e)
		{
			logger.warning("Unable to open file. Please check file exists and path is correct.");
		}
		catch(JsonSyntaxException e)
		{
			logger.warning("Error: Json is malformed. Make sure that the JSON is formed correctly.");
		}
		return finalRooms;
	}
	/**
	 * Function to handle exporting the roomlist to a json file
	 * @param roomList the roomlist to be exported
	 * @param logger the logger for the system
	 * As a default, it will put it in the current running directory if the user does not specify a path, but users can specify a path.
	 */
	protected static void exportRooms(ArrayList<Room> roomList, Logger logger)
	{
		//get new gson object
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
		String json = gson.toJson(roomList);
		//ensure input is correct
		boolean inputCheck = false;
		System.out.println("Please specify full pathname and file to save to:");
		String filename = keyboard.next();
		while(!inputCheck)
		{
			if(!filename.contains(".json"))
			{
				logger.warning("Please make sure your filename ends with .json");
				System.out.println("Please specify full pathname and file to save to:");
				filename = keyboard.next();
			}
			else
			{
				inputCheck = true;
			}
		}
		//write to file
		try{
			FileWriter writer = new FileWriter(filename);
			writer.write(json);
			writer.close();
			logger.info("Files exported to " + filename + ".");
		}
		catch(IOException e)
		{
			logger.warning("Unable to create JSON file. Please try again.");
		}
	}
	/**
	 * Function to handle listing a schedule for a given room
	 * @param roomList the current list of rooms
	 * @param logger the logger for the system
	 * @return the list of all rooms in the building
	 */
	protected static String listSchedule(ArrayList<Room> roomList, Logger logger) {
		String roomName = getRoomName(roomList, logger);
		System.out.println(roomName + " Schedule");
		System.out.println("---------------------");
		if(roomList.size() > 0)
		{
			for (Meeting m : getRoomFromName(roomList, roomName).getMeetings()) {
				System.out.println(m.toString());
			}
		}
		return "";
	}
	/**
	 * Function to handle main menu text population
	 * @param logger the logger for the system
	 * @return the number selection from the main menu options
	 */
	protected static int mainMenu(Logger logger) {
		System.out.println("Main Menu:");
		System.out.println("  1 - Add a room");
		System.out.println("  2 - Remove a room");
		System.out.println("  3 - Schedule a room");
		System.out.println("  4 - List Schedule");
		System.out.println("  5 - List Rooms");
		System.out.println("  6 - Export Rooms to a JSON file");
		System.out.println("  7 - Import Rooms from a JSON file");
		System.out.println("  0 - Exit the program");
		System.out.println("Enter your selection: ");
		//ensure that target is an integer
		int target = -1;
		while(target == -1)
		{
			try{
				target = Integer.parseInt(keyboard.next());
				if(target < 0)
				{
					logger.warning("Invalid selection: Please select a number 1-7. Or 0 to exit");
				}
			}
			catch(NumberFormatException e)
			{
				logger.warning("ERROR: Please input an integer from 0-7");
			}
		}
		return target;
	}

	/**
	 * Function to handle adding a room
	 * @param roomList the current list of rooms
	 * @param logger the logger for the system
	 */
	protected static void addRoom(ArrayList<Room> roomList, Logger logger) {
		System.out.println("Add a room:");
		String name = "";
		boolean foundRoom = true;

		while(foundRoom)
		{
			foundRoom = false;
			System.out.println("Room Name?");

			String test = keyboard.next();
			if(findRoomIndex(roomList, test) == -1)
			{
				name = test;
			}
			else
			{
				logger.warning("Room already created. Please add a differently named room.");
				foundRoom = true;
			}
		}

		int capacity = 0;
		boolean inputCheck = false;
		//make sure input is an int
		while(!inputCheck)
		{
			inputCheck = true;
			try{
				System.out.println("Room capacity?");
				capacity = keyboard.nextInt();
				if(capacity <= 0)
				{
					logger.warning("Room capacity must be greater than 0.");
					inputCheck = false;
				}
			}
			catch(InputMismatchException e)
			{
				logger.warning("Invalid input. Please input a number. ");
				inputCheck = false;
				keyboard = new Scanner(System.in);
			}
		}
		
		System.out.println("Building?");
		String building = keyboard.next();
		building = building.replace(',', ' ');
		System.out.println("Location?");
		String location = keyboard.next();
		location = location.replace(',', ' ');
		name = name.replace(',', ' ');
		
		

		Room newRoom = new Room(name, capacity, building, location);
		roomList.add(newRoom);
		logger.info("Room '" + newRoom.getName() + "' added successfully!");
		return;
	}

	/**
	 * Function to handle removing a room.
	 * @param roomList the list of rooms 
	 * @param logger the logger for the system
	 */
	protected static void removeRoom(ArrayList<Room> roomList, Logger logger) {
		System.out.println("Remove a room:");
		if(roomList.size() == 0)
		{
			logger.info("The room list is empty.");
			return;
		}
		int index = findRoomIndex(roomList, getRoomName(roomList, logger));

		if(index != -1)
		{
			roomList.remove(index);
			logger.info("Room removed successfully!");
			return;
		}
		else
		{
			logger.warning("Room not found. Room not removed.");
			return;
		}
	}

	/**
	 * Function to list rooms and their capacities
	 * @param roomList The roomlist 
	 * @return the string to be printed by the menu loop
	 */
	protected static String listRooms(ArrayList<Room> roomList) {
		System.out.println("Room Name - Building Location - Capacity");
		System.out.println("---------------------");

		for (Room room : roomList) {
			System.out.println(room.getName() + " - " + room.getBuilding() + " " + room.getLocation() +  " - " + room.getCapacity());
		}

		System.out.println("---------------------");

		return roomList.size() + " Room(s)";
	}

	/**
	 * Function to handle schduling a room
	 * @param roomList the room list to be added to
	 * @param logger the logger for the system
	 * @return a string saying that it either succeeded or failed
	 */
	protected static String scheduleRoom(ArrayList<Room> roomList, Logger logger) {

		//get the room name
		System.out.println("Schedule a room:");
		

		boolean inputCheck = false;

		Timestamp startTimestamp = null;
		while(!inputCheck)
		{
			System.out.println("Start Date? (yyyy-mm-dd):");
			String startDate = keyboard.next();
			System.out.println("Start Time? (24 hour)");
			String startTime = keyboard.next();
			startTime = startTime + ":00.0";
			String testTime = startDate + " " + startTime;
			if(validateTimestamp(testTime))
			{
				startTimestamp = Timestamp.valueOf(testTime);
				if(currentTimeCheck(startTimestamp))
				{
					inputCheck = true;
				}
				else
				{
					logger.warning("Time format was correct but time specified was earlier than current time. Please specify a time which is past the current date and time.");
				}
			}
			else
			{
				logger.warning("Please make sure your inputs are in the correct format. Do not go over 24 hours.");
			}
		}

		inputCheck = false;
		Timestamp endTimestamp = null;
		while(!inputCheck)
		{
			System.out.println("End Date? (yyyy-mm-dd):");
			String endDate = keyboard.next();
			System.out.println("End Time? (24 hour)");
			String endTime = keyboard.next();
			endTime = endTime + ":00.0";
			String testTime = endDate + " " + endTime;
			if(validateTimestamp(testTime))
			{
				endTimestamp = Timestamp.valueOf(testTime);
				if(currentTimeCheck(endTimestamp))
				{
					if(endTimestamp.after(startTimestamp))
					{
						inputCheck = true;
					}
					else
					{
						logger.warning("End time was earlier than start time. Please ensure that the meeting end happens after the meeting start.");
					}
				}
				else
				{
					logger.warning("Time format was corret but time specified was earlier than current time. Please specify a time which is past the current date and time.");
				}
			}
			else
			{
				logger.warning("Please make sure your inputs are in the correct format. Do not go over 24 hours.");
			}
		}
		
		
		System.out.println("Subject?");
		String subject = keyboard.next();
		Meeting meeting = new Meeting(startTimestamp, endTimestamp, subject);
		
		ArrayList<Room> cleanRooms = getAvailableRooms(roomList, meeting);

		System.out.println("Do you want a list of available rooms for your meeting? (Y or N)");
		String answer = keyboard.next();
			if(answer.toLowerCase().trim().compareTo("y") == 0)
			{
				System.out.println("Rooms which are available:");
				if(cleanRooms.size() > 0)
				{
					for(Room testRoom: cleanRooms)
					{
						System.out.println(testRoom.getName());
					}
				}				
			}
			
			if(cleanRooms.size() <= 0)
			{
				return "There are no rooms available for your meeting time. Please try again with a different time.";
			}
			else
			{
				String name = getRoomName(roomList, logger);
				Room curRoom = getRoomFromName(roomList, name);
				return curRoom.addMeeting(meeting);
			}
	}

	/**
	 * Function to handle getting a room from the list of rooms by name
	 * @param roomList the list of rooms to be parsed
	 * @param name the name of the room to find
	 * @return the room with the given name
	 */
	protected static Room getRoomFromName(ArrayList<Room> roomList, String name) {
		return roomList.get(findRoomIndex(roomList, name));
	}

	/**
	 * Function to get the index of a room with a given name
	 * @param roomList the list of rooms to be parsed
	 * @param roomName the room to find
	 * @return the index of the room 
	 */
	protected static int findRoomIndex(ArrayList<Room> roomList, String roomName) {
		int roomIndex = -1;
		int finalRoomIndex = -1;
		boolean found = false;
		for (Room room : roomList) {
			if (room.getName().compareTo(roomName) == 0 && found == false) 
			{
				found = true;
				roomIndex++;
			}
			if(!found)
			{
				roomIndex++;
			}
		}
		if(found)
		{
			finalRoomIndex = roomIndex;
		}

		return finalRoomIndex;
	}

	/**
	 * Function to figure out which rooms are available for a given time period
	 * @param rooms the current room list
	 * @param m the meeting to check
	 * @return an arraylist of rooms which have availabilities for the given meeting
	 */
	protected static ArrayList<Room> getAvailableRooms(ArrayList<Room> rooms, Meeting m)
	{
		ArrayList<Room> availableRooms = new ArrayList<Room>();
		for(Room testRoom: rooms)
		{
			if(testRoom.verifySchedule(m))
			{
				availableRooms.add(testRoom);
			}
		}
		return availableRooms;
	}
	/**
	 * Function to get a room name from keyboard input
	 * @param rooms the room list to be parsed
	 * @param logger the logger for the system
	 * @return the room string received from input
	 */
	protected static String getRoomName(ArrayList<Room> rooms, Logger logger) 
	{
		boolean inputCheck = false;
		String test = "";
		while(!inputCheck)
		{
			System.out.println("Room Name?");

			test = keyboard.next();
		
			if(findRoomIndex(rooms, test) == -1)
			{
				logger.warning("Invalid room. Please input a room that is on the room list.");

			}
			else
			{
				inputCheck = true;
			}
		}
		return test;
	}

	/**
	 * function to handle validating creation of timestamps without throwing exceptions
	 * @param t the string to be tested
	 * @return true if it is in valid timestamp format, false otherwise
	 */
	protected static Boolean validateTimestamp(String t)
	{
		boolean returnVal = true;
		try
		{
			Timestamp.valueOf(t);
		}
		catch(IllegalArgumentException e)
		{
			returnVal = false;
		}
		return returnVal;
	}
	
	/**
	 * Function to check if the current time stamp is in the future
	 * @param timeCheck the current time to check
	 * @return a boolean true if it is past the current time, or false if it is not
	 */
	protected static boolean currentTimeCheck(Timestamp timeCheck)
	{
		Date now = new Date();
		Timestamp nowTimeStamp = new Timestamp(now.getTime());
		return timeCheck.after(nowTimeStamp);
	}
	
}
