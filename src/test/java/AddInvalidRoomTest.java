/**
 * Gregory Cremins Software License, 1.0
 * 
 * The user of this software has all rights to modify the source code of this file, provided they mention the original developer in each modified file.
 */
package test.java;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import main.java.com.marist.mscs721.RoomScheduler;

import org.junit.Test;

/**
 * Test to create an invalid room with a bad capacity, then add a valid one, and make sure that the valid one is listed when the list command is given
 * @author Gregory Cremins
 * @version 2-22-2016
 */
public class AddInvalidRoomTest {

	//create room scheduler
	RoomScheduler rs = new RoomScheduler();
	//input specification, space separated
	String[] arguments = new String[]{"1 TestRoom -1 12 Dyson MaristCollege 5 0"};
	ByteArrayOutputStream outResults = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(outResults);
	PrintStream out = System.out;
	@Test
	public void test() 
	{
		System.out.println("TEST CASE 2: Attempt to create a room with an invalid room capacity. (Expected failure)");
		System.setOut(ps);
		//run arguements in test
		rs.main(arguments);	
		System.out.flush();
		System.setOut(out);
		//Uncomment to show test results
		//System.out.println("TEST RESULT: " + outResults.toString());
		boolean roomCreatedTest = outResults.toString().contains("TestRoom - Dyson MaristCollege - 12");
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
