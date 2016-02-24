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