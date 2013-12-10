package server;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import shared.*;

public class WhiteboardServerTest {
    
    private static Socket socket;
    private static Socket whiteboardSocket;
    private static BufferedReader in, inBoard; 
    private static PrintWriter out, outBoard; 
    
    private final String sp = " ";
    private final String name = "ShawnJ";
    
    @BeforeClass
    public static void setup() throws IOException {
        TestUtility.startServer();  
        socket = TestUtility.connect();
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }
    
    @AfterClass
    public static void tearDown() throws IOException {
        in.close();
        out.close();
        socket.close();
        inBoard.close();
        outBoard.close();
        whiteboardSocket.close();
    }
    
    @Test
    public void fullTest() throws IOException {
        askForWhiteboard();
        writeOnceToWhiteboard();
    }
    
    public void askForWhiteboard() throws IOException {
        // create the board
        out.println(Messages.CREATE_NEW_WHITEBOARD);
        assertEquals(Integer.parseInt(in.readLine()), 1);
        
        // request connection to the WhiteboardServer
        whiteboardSocket = TestUtility.connectToWhiteboardServer();
        
        inBoard = new BufferedReader(new InputStreamReader(whiteboardSocket.getInputStream()));
        outBoard = new PrintWriter(whiteboardSocket.getOutputStream(), true);
        
        /*
         * Protocol token ordering:
         * 0 - Request type 
         * 1 - Whiteboard ID
         * 2 - username (String) (only on shared.Messages.NEW_WHITEBOARD_CONNECTION)
         * 2-7 WhiteboardAction (only on shared.Messages.ADD_ACTION)
         *   2 - x1
         *   3 - y1
         *   4 - x2 
         *   5 - y2
         *   6 - colorRGB (int)
         *   7 - strokeWidth (int)
         */
        
        outBoard.println(Messages.NEW_WHITEBOARD_CONNECTION + sp + "0" + sp + name);
        // read lines of history
        List<String> answers = new ArrayList<String>();
        try {
            for (String action = inBoard.readLine(); action != null; action = inBoard
                    .readLine()) {
                answers.add(action);
            }
        } catch (SocketTimeoutException e) {
            assertTrue(answers.size() == 0);
        }
    }
    
    public void writeOnceToWhiteboard() throws IOException {
        final int x1 = 10, y1 = 10, x2 = 50, y2 = 50, 
                colorRGB = new Color(10,20,30).getRGB(), width = 20;
        WhiteboardAction wba = new WhiteboardAction(x1, y1, x2, y2, colorRGB, width);
        outBoard.println(Messages.ADD_ACTION + sp + "0" + sp + wba.toString());
        
        // Check response
        List<String> answers = new ArrayList<String>();
        try {
            for (String action = inBoard.readLine(); action != null; action = inBoard
                    .readLine()) {
                answers.add(action);
            }
        } catch (SocketTimeoutException e) {
            assertEquals(WhiteboardAction.parse(answers.get(0)), wba);
        }
    }

}
