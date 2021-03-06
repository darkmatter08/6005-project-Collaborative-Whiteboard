package shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import canvas.*;
import server.*;

/**
 * Executes all tests in the suite. 
 * @author jains
 *
 */

@RunWith(Suite.class)
//PickerServerTest.class must be at the end.
@SuiteClasses({
    ClientWhiteBoardGUITest.class,
	WhiteboardPickerClientTest.class,
	
	PickerServerTest.class,
    WhiteboardServerInfoTest.class,
	WhiteboardServerTest.class,
	
	WhiteboardActionTest.class,
})
public class AllTests {

}
