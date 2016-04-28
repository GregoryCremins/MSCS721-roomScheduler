/**
 * Gregory Cremins Software License, 1.0
 * 
 * The user of this software has all rights to modify the source code of this file, provided they mention the original developer in each modified file.
 */
package test.java;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
		AddValidRoomTest.class, AddInvalidRoomTest.class,AddExistingRoomTest.class, 
		RemoveValidRoomTest.class, RemoveInvalidRoomTest.class,
		ScheduleValidRoomTest.class, ScheduleRoomInvalidInputTest.class, ScheduleConflictTest.class,
		ExportRoomTest.class, ExportToNonJsonTest.class
})
/**
 * Unit test suite to run all of the unit tests.
 * @author Greg Cremins
 * @version 2/23/2016
 *
 */
public class JunitTestSuite {   
}  