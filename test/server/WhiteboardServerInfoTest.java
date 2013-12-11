package server;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import shared.Messages;

public class WhiteboardServerInfoTest {
    
    /**
     * Testing Strategy:
     * - Add clients and make sure that getConnectedUsernamesMessage is correct.
     * - Check that the list of users is empty on start
     */
    
    @Test
    public void testGetConnectedUsernamesMessage() {
        WhiteboardServerInfo whiteboard = new WhiteboardServerInfo();
        whiteboard.getClients().add(new ClientConnection(null, "A"));
        whiteboard.getClients().add(new ClientConnection(null, "B"));
        whiteboard.getClients().add(new ClientConnection(null, "C"));
        assertEquals(Messages.CONNECTED_USERS + " A B C", whiteboard.getConnectedUsernamesMessage());
    }
    
    @Test 
    public void testUsersList() {
        WhiteboardServerInfo whiteboard = new WhiteboardServerInfo();
        List s = new ArrayList<ClientConnection>();
        assertEquals(whiteboard.getClients(), s);
    }

}
