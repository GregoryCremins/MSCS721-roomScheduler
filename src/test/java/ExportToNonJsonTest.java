package test.java;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import main.java.com.marist.mscs721.RoomScheduler;

import org.junit.Test;

/**
 * Test to create a valid room, make sure that it is listed when the list command is given, then attempt to export the list to a non-json file and ensure that it was not created.
 * @author Gregory Cremins
 * @version 2-22-2016
 */
public class ExportToNonJsonTest {

	//create room scheduler
	RoomScheduler rs = new RoomScheduler();
	//input specification, space separated
	String[] arguments = new String[]{"1 TestRoom 12 Dyson MaristCollege 5 6 Testfile.jpg Testfile.json 0"};
	ByteArrayOutputStream outResults = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(outResults);
	PrintStream out = System.out;
	@Test
	public void test() 
	{
		System.out.println("TEST CASE 10: Export a roomlist to a non-json file.");
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
