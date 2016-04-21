package main.java.com.marist.mscs721;

import java.sql.Timestamp;
import java.util.ArrayList;
/**
 * Class to handle room creation and modifcation
 * @author Greg Cremins / Mike Gildein
 * @version 1-27-2016
 *
 */
public class Room {	

	private String name;
	private String building; 
	private String location;
	private int capacity;
	private ArrayList<Meeting> meetings;
	

	/**
	 * Constructor for room
	 * @param newName the name of the room
	 * @param newCapacity its capacity
	 * @param building the building name
	 * @param location the location of the new building
	 * meetings defaulted to null
	 */
	public Room(String newName, int newCapacity, String building, String location) {
		setName(newName);
		setCapacity(newCapacity);
		setMeetings(new ArrayList<Meeting>());
		setBuilding(building);
		setLocation(location);
	}
	/**
	 * Overloaded constructor for room given json file data
	 * @param data the data from a json file
	 */
	public Room(String data)
	{
		String jsonString = data;
		//name of room
		int namePosn = jsonString.indexOf("name") + 4;
		setName(jsonString.substring(namePosn + 1, jsonString.indexOf(",")));
		jsonString = jsonString.substring(jsonString.indexOf(",")+ 1, jsonString.length());
		//building
		int buildPosn = jsonString.indexOf("Building") + 10;
		setBuilding(jsonString.substring(buildPosn + 1, jsonString.indexOf(",")));	
		jsonString = jsonString.substring(jsonString.indexOf(",")+ 1, jsonString.length());
		//location
		int locPosn = jsonString.indexOf("Location") + 10;
		setLocation(jsonString.substring(locPosn + 1, jsonString.indexOf(",")));	
		jsonString = jsonString.substring(jsonString.indexOf(",")+ 1, jsonString.length());
		//capacity
		int capPosn = jsonString.indexOf("capacity") + 8;
		setCapacity((int)Double.parseDouble(jsonString.substring(capPosn + 1, jsonString.indexOf(","))));
		jsonString = jsonString.substring(jsonString.indexOf(",")+ 1, jsonString.length());
		//meetings
		int meetPosn = jsonString.indexOf("meetings") + 8;
		jsonString = jsonString.substring(0, jsonString.length() - 1);
		//add handling for building and location
		if(jsonString.length() > 14)
		{
			String[] meetStrings = jsonString.substring(meetPosn + 3, jsonString.length() - 1).split("}");
			ArrayList<Meeting> meetingsA = new ArrayList<Meeting>();
			for(int i = 0; i < meetStrings.length; i++)
			{
				meetingsA.add(new Meeting(meetStrings[i]));
			}
			setMeetings(meetingsA);
		}
		else 
		{
			setMeetings(new ArrayList<Meeting>());
		}
	}

	/**
	 * Function to add a meeting to a given room
	 * @param newMeeting the meeting to be added
	 * @return the string statking if the meeting was added successfully
	 */
	public String addMeeting(Meeting newMeeting) 
	{ 
		//add verification
		if(this.verifySchedule(newMeeting))
		{
			this.getMeetings().add(newMeeting);
			return("Successfully scheduled meeting!");
		}
		else
		{
			return("There is a meeting or meetings that overlap with this time, please change the meeting time. Check the schedule for more information");
		}
	}

	/**
	 * Function to get the name of the room
	 * @return the name of the room as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Function to handle setting the name of the room to a given name
	 * @param name the name which you want to set it to
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Function to get the capacity for the room
	 * @return the capacity for a given room
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Function to handle setting the cpacity for a room
	 * @param capacity the capacity you wish to set it at
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Function to handle returning the meetings
	 * @return the list of meetings
	 */
	public ArrayList<Meeting> getMeetings() {
		return meetings;
	}

	/**
	 * Function to handle setting the meetings for a given room
	 * @param meetings the meetings to be set
	 */
	public void setMeetings(ArrayList<Meeting> meetings) {
		this.meetings = meetings;
	}

	/**
	 * Function to check if a meeting which will be added will not interfere with the given schedule
	 * @param meeting the meeting to be checked
	 * @return true if it will not conflict, false if it will
	 */
	public boolean verifySchedule(Meeting meeting)
	{
		ArrayList<Meeting> meetingsL = this.meetings;
		for(Meeting m: meetingsL)
		{
			//overlap check
			if(!(checkMeeting(meeting.getStartTime(), meeting.getStopTime(), m.getStartTime(), m.getStopTime())))
			{
				return false;
			}
			
		}
		return true;
	}

	/**
	 * Function to handle checking two meetings to see if they overlap
	 * @param startA the start time of a
	 * @param endA the end time of a
	 * @param startB the start time of b
	 * @param endB the end time of b
	 * @return a boolean stating weather the meeting overlaps is true or false
	 */
	public boolean checkMeeting(Timestamp startA, Timestamp endA, Timestamp startB, Timestamp endB)
	{
		return startA.after(endB) && endA.before(startB);
		
	}
	
	/**
	 * Function to set a room's building
	 * @param building the building the room is located in
	 */
	public void setBuilding(String building)
	{
		this.building = building;
	}
	
	/**
	 * Function to set a room's location
	 * @param location the location where a room is located
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	/**
	 * Function to get the room's building
	 * @return the building the room is located in
	 */
	public String getBuilding()
	{
		return this.building;
	}
	
	/**
	 * Function to get the rooms location geographically
	 * @return the room's location geographically
	 */
	public String getLocation()
	{
		return this.location;
	}

}
