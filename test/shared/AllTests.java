package shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import canvas.*;
import server.*;

@RunWith(Suite.class)
//PickerServerTest.class must be at the end.
@SuiteClasses({
    ClientWhiteBoardGUITest.class,
	WhiteboardPickerClientTest.class,
	PickerServerTest.class,
	WhiteboardServerTest.class,
	WhiteboardActionTest.class
})
public class AllTests {

}
