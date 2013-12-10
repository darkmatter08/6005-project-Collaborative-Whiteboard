package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
//PickerServerTest.class must be at the end.
@SuiteClasses({ ClientWhiteBoardGUITest.class,
		WhiteboardPickerClientTest.class, PickerServerTest.class })
public class AllTests {

}
