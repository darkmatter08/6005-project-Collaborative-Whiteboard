package shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import canvas.ClientWhiteBoardGUITest;
import canvas.WhiteboardPickerClientTest;
import server.PickerServerTest;

@RunWith(Suite.class)
//PickerServerTest.class must be at the end.
@SuiteClasses({ ClientWhiteBoardGUITest.class,
		WhiteboardPickerClientTest.class, PickerServerTest.class })
public class AllTests {

}
