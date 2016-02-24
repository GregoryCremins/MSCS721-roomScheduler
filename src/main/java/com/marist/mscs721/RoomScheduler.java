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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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

		if(args.length > 0)
		{
			keyboard = new Scanner(new ByteArrayInputStream(args[0].getBytes()));
		}
		//loop until end occurs
		Boolean end = false;
		ArrayList<Room> rooms = new ArrayList<Room>();
		while (!end) {
			switch (mainMenu()) {
			case 1:
				System.out.println(addRoom(rooms));
				break;
			case 2:
				System.out.println(removeRoom(rooms));
				break;
			case 3:
				System.out.print(scheduleRoom(rooms));
				break;
			case 4:
				System.out.println(listSchedule(rooms));
				break;
			case 5:
				System.out.println(listRooms(rooms));
				break;
			case 6:
				exportRooms(rooms);
				break;
			case 7:
				rooms = importRooms();
				break;
			case 0:
				end = true;
				break;
			default:
				System.out.println("Invalid selection: Please select a number 1-7. Or 0 to exit");
				break;
			}
		}
	}
	/**
	 * Function to handle importing room list from a json file
	 * @return the imported list of rooms and schedules
	 * Used GSON for importer and exporter, see https://github.com/google/gson
	 * Some help from: http://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
	 */
	protected static ArrayList<Room> importRooms()
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
				System.out.println("ERROR: Please specify a .JSON file.");
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
			System.out.println("FAILURE: ERROR READING FROM DISK. Unable to open file. Please check file exists and path is correct.");
		}
		return finalRooms;
	}
	/**
	 * Function to handle exporting the roomlist to a json file
	 * @param roomList the roomlist to be exported
	 * As a default, it will put it in the current running directory if the user does not specify a path, but users can specify a path.
	 */
	protected static void exportRooms(ArrayList<Room> roomList)
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
				System.out.println("ERROR: Please make sure your filename ends with .json");
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
			System.out.println("Files exported to " + filename + ".");
		}
		catch(IOException e)
		{
			System.out.println("ERROR WRITING TO DISK: Unable to create JSON file. Please try again.");
		}
	}
	/**
	 * Function to handle listing a schedule for a given room
	 * @param roomList the current list of rooms
	 * @return the list of all rooms in the building
	 */
	protected static String listSchedule(ArrayList<Room> roomList) {
		String roomName = getRoomName(roomList);
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
	 * @return the number selection from the main menu options
	 */
	protected static int mainMenu() {
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
					System.out.println("Invalid selection: Please select a number 1-7. Or 0 to exit");
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("ERROR: Please input an integer from 0-7");
			}
		}
		return target;
	}

	/**
	 * Function to handle adding a room
	 * @param roomList the current list of rooms
	 * @return a string printed by the above switch statement to alert the user of the created room.
	 */
	protected static String addRoom(ArrayList<Room> roomList) {
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
				System.out.println("ERROR: Room already created. Please add a differently named room.");
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
					System.out.println("ERROR: Room capacity must be greater than 0.");
					inputCheck = false;
				}
			}
			catch(InputMismatchException e)
			{
				System.out.println("ERROR: INVALID INPUT. Please input a number. ");
				inputCheck = false;
				keyboard = new Scanner(System.in);
			}
		}

		Room newRoom = new Room(name, capacity);
		roomList.add(newRoom);
		return "Room '" + newRoom.getName() + "' added successfully!";
	}

	/**
	 * Function to handle removing a room.
	 * @param roomList the list of rooms 
	 * @return the string to be printed by the input loop
	 */
	protected static String removeRoom(ArrayList<Room> roomList) {
		System.out.println("Remove a room:");
		int index = findRoomIndex(roomList, getRoomName(roomList));

		if(index != -1)
		{
			roomList.remove(index);
			return "Room removed successfully!";
		}
		else
		{
			return "ERROR: Room not found. Room not removed.";
		}
	}

	/**
	 * Function to list rooms and their capacities
	 * @param roomList The roomlist 
	 * @return the string to be printed by the menu loop
	 */
	protected static String listRooms(ArrayList<Room> roomList) {
		System.out.println("Room Name - Capacity");
		System.out.println("---------------------");

		for (Room room : roomList) {
			System.out.println(room.getName() + " - " + room.getCapacity());
		}

		System.out.println("---------------------");

		return roomList.size() + " Room(s)";
	}

	/**
	 * Function to handle schduling a room
	 * @param roomList the room list to be added to
	 * @return a string saying that it either succeeded or failed
	 */
	protected static String scheduleRoom(ArrayList<Room> roomList) {

		//get the room name
		System.out.println("Schedule a room:");
		String name = getRoomName(roomList);

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
					System.out.println("ERROR: Time format was corret but time specified was earlier than current time. Please specify a time which is past the current date and time.");
				}
			}
			else
			{
				System.out.println("ERROR: Please make sure your inputs are in the correct format. Do not go over 24 hours.");
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
						System.out.println("ERROR: End time was earlier than start time. Please ensure that the meeting end happens after the meeting start.");
					}
				}
				else
				{
					System.out.println("ERROR: Time format was corret but time specified was earlier than current time. Please specify a time which is past the current date and time.");
				}
			}
			else
			{
				System.out.println("ERROR: Please make sure your inputs are in the correct format. Do not go over 24 hours.");
			}
		}

		System.out.println("Subject?");
		String subject = keyboard.next();

		Room curRoom = getRoomFromName(roomList, name);

		Meeting meeting = new Meeting(startTimestamp, endTimestamp, subject);

		curRoom.addMeeting(meeting);
		return "";
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
	 * Function to get a room name from keyboard input
	 * @param rooms the room list to be parsed
	 * @return the room string received from input
	 */
	protected static String getRoomName(ArrayList<Room> rooms) 
	{
		boolean inputCheck = false;
		String test = "";
		while(!inputCheck)
		{
			System.out.println("Room Name?");

			test = keyboard.next();
		
			if(findRoomIndex(rooms, test) == -1)
			{
				System.out.println("ERROR: Invalid room. Please input a room that is on the room list.");

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
