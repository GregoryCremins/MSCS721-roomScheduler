package test.java;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import main.java.com.marist.mscs721.RoomScheduler;

import org.junit.Test;

/**
 * Test to create a valid room, and make sure that it is listed when the list command is given
 * @author Gregory Cremins
 * @version 2-22-2016
 */
public class AddValidRoomTest {

	//create room scheduler
	RoomScheduler rs = new RoomScheduler();
	//input specification, space separated
	String[] arguments = new String[]{"1 TestRoom 12 5 0"};
	ByteArrayOutputStream outResults = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(outResults);
	PrintStream out = System.out;
	@Test
	public void test() 
	{
		System.out.println("TEST CASE 1: Create a valid room");
		System.setOut(ps);
		//run arguements in test
		rs.main(arguments);	
		System.out.flush();
		System.setOut(out);
		//Uncomment to show test results
		//System.out.println("TEST RESULT: " + outResults.toString());
		boolean roomCreatedTest = outResults.toString().contains("TestRoom - 12");
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
