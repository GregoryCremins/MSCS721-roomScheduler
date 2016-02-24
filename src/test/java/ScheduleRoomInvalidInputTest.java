package test.java;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import main.java.com.marist.mscs721.RoomScheduler;

import org.junit.Test;

/**
 * Test to create a valid room, schedule a meeting for a non-existant room, then try to schedule invalid start and end times, and make sure that the meeting is listed when the list command is given
 * @author Gregory Cremins
 * @version 2-22-2016
 */
public class ScheduleRoomInvalidInputTest {

	//create room scheduler
	RoomScheduler rs = new RoomScheduler();
	//input specification, space separated
	String[] arguments = new String[]{"1 TestRoom 12 3 NONEXISTANTROOM TestRoom a b 2200-02-13 12:00 a b 2200-02-13 14:00 TestSubject 4 TestRoom 0"};
	ByteArrayOutputStream outResults = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(outResults);
	PrintStream out = System.out;
	@Test
	public void test() 
	{
		System.out.println("TEST CASE 7: Schedule an invalid meeting.");
		System.setOut(ps);
		//run arguements in test
		rs.main(arguments);	
		System.out.flush();
		System.setOut(out);
		//Uncomment to show test results
		//System.out.println("TEST RESULT: " + outResults.toString());
		boolean roomCreatedTest = outResults.toString().contains("ERROR: Invalid room. Please input a room that is on the room list.") && outResults.toString().contains("ERROR: Please make sure your inputs are in the correct format. Do not go over 24 hours.") && outResults.toString().contains("2200-02-13 12:00:00.0 - 2200-02-13 14:00:00.0: TestSubject");
		if(roomCreatedTest)
		{
			System.out.println("Test successful.");
		}
		else
		{
			System.out.println("Test unsuccessful.");
		}
		assertTrue(roomCreatedTest);
	}

}
