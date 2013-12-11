package shared;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class WhiteboardActionTest {

    @Test
    public void testParseToString() {
        WhiteboardAction action = new WhiteboardAction(1, 1, 5, 5, new Color(255, 0, 0).getRGB(), 10);
        assertEquals(WhiteboardAction.parse(action.toString()), action);
    }

}
