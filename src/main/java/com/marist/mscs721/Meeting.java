/**
 * Gregory Cremins Software License, 1.0
 * 
 * The user of this software has all rights to modify the source code of this file, provided they mention the original developer in each modified file.
 */
package main.java.com.marist.mscs721;

import java.sql.Timestamp;

/**
 * Class to handle meetings and modifying their information
 * @author Greg Cremins/ Mike Gildein
 * @version 1-27-2015
 */
public class Meeting {

	private Timestamp startTime = null;
	private Timestamp stopTime = null;
	private String subject = null;

	/**
	 * Constructor for meeting
	 * @param newStartTime the start time of the meeting
	 * @param newEndTime the end time of the meeting
	 * @param newSubject the subject of the meeting
	 */
	public Meeting(Timestamp newStartTime, Timestamp newEndTime, String newSubject) {
		setStartTime(newStartTime);
		setStopTime(newEndTime);
		if (newSubject.isEmpty()) {
			setSubject("N/A");
		}
		else {
			setSubject(newSubject);
		}
	}

	/**
	 * Overloaded constructor for a meeting using json string data
	 * @param jsonString the json string data for the meeting
	 */
	public Meeting(String jsonString)
	{
		String jsonString2 = jsonString.substring(jsonString.indexOf("startTime"));
		String[] datapieces = jsonString2.split(",");
		setStartTime(parseTime(datapieces[0].substring(10)));
		setStopTime(parseTime(datapieces[1].substring(10)));
		setSubject(datapieces[2].substring(9));
	}

	
	/**
	 *
	 * Function to turn a meeting to a string
	 * @return the string version of the meeting
	 */
	@Override public String toString() {
		return this.getStartTime().toString() + " - " + this.getStopTime() + ": " + getSubject();
	}

	/**
	 * Function to get the start time of a meeting
	 * @return the starting time of the meeting
	 */
	public Timestamp getStartTime() {
		return startTime;
	}

	/**
	 * Function to handle setting the start time of a meeting
	 * @param startTime the meeting 
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * function to handle getting the stop time of the meeting
	 * @return the stop time of the meeting
	 */
	public Timestamp getStopTime() {
		return stopTime;
	}

	/**
	 * Function to handle set the stop time for the meeting
	 * @param stopTime the time to be set
	 */
	public void setStopTime(Timestamp stopTime) {
		this.stopTime = stopTime;
	}

	/**
	 * Function to get the subject of the meeting
	 * @return the meeting subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * Function to handle setting the subject of a meeting
	 * @param subject the subject of the meeting
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * Function to parse time from a string
	 * @param data the string to be parsed
	 * @return the timestamp from the string
	 */
	public Timestamp parseTime(String data)
	{
		return Timestamp.valueOf(data);
	}

}
