package server;

import static org.junit.Assert.*;

import java.io.PrintWriter;

import org.junit.Test;

import shared.Messages;

public class WhiteboardServerInfoTest {

    @Test
    public void testGetConnectedUsernamesMessage() {
        WhiteboardServerInfo whiteboard = new WhiteboardServerInfo();
        whiteboard.getClients().add(new ClientConnection(null, "A"));
        whiteboard.getClients().add(new ClientConnection(null, "B"));
        whiteboard.getClients().add(new ClientConnection(null, "C"));
        assertEquals(Messages.CONNECTED_USERS + " A B C", whiteboard.getConnectedUsernamesMessage());
    }

}
