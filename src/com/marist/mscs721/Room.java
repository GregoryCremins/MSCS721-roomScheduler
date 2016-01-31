package com.marist.mscs721;

import java.sql.Timestamp;
import java.util.ArrayList;
/**
 * Class to handle room creation and modifcation
 * @author Greg Cremins/ Mike Gildein
 * @version 1-27-2016
 *
 */
public class Room {	

	private String name;
	private int capacity;
	private ArrayList<Meeting> meetings;

	/**
	 * Constructor for room
	 * @param newName the name of the room
	 * @param newCapacity its capacity
	 * meetings defaulted to null
	 */
	public Room(String newName, int newCapacity) {
		setName(newName);
		setCapacity(newCapacity);
		setMeetings(new ArrayList<Meeting>());
	}
	/**
	 * Overloaded constructor for room given json file data
	 * @param data
	 */
	public Room(String data)
	{
		int namePosn = data.indexOf("name") + 4;

		setName(data.substring(namePosn + 1, data.indexOf(",")));
		data = data.substring(data.indexOf(",")+ 1, data.length());
		int capPosn = data.indexOf("capacity") + 8;
		setCapacity((int)Double.parseDouble(data.substring(capPosn + 1, data.indexOf(","))));
		data = data.substring(data.indexOf(",")+ 1, data.length());
		int meetPosn = data.indexOf("meetings") + 8;
		data = data.substring(0, data.length() - 1);
		if(data.length() > 14)
		{
			String[] meetStrings = data.substring(meetPosn + 3, data.length() - 1).split("}");
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
	 */
	public void addMeeting(Meeting newMeeting) 
	{ 
		//add verification
		if(this.verifySchedule(newMeeting) == true)
		{
			this.getMeetings().add(newMeeting);
			System.out.println("Successfully scheduled meeting!");
		}
		else
		{
			System.out.println("ERROR: There is a meeting or meetings that overlap with this time, please change the meeting time. Check the schedule for more information");
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
	 * @return
	 */
	public boolean checkMeeting(Timestamp startA, Timestamp endA, Timestamp startB, Timestamp endB)
	{
		if(startA.after(endB) && endA.before(startB))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
