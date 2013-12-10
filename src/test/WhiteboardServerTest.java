package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.*;

public class WhiteboardServerTest {
    
    private static Socket socket;
    private static Socket whiteboardSocket;
    private static BufferedReader in; 
    private static PrintWriter out; 
    
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
    }
    
    @Test
    public void fullTest() {
        fail("Not yet implemented");
    }
    
    public void askForWhiteboard() throws IOException {
        // create the board
        out.println(Messages.CREATE_NEW_WHITEBOARD);
        assertEquals(Integer.parseInt(in.readLine()), 1);
        
        // request connection to the WhiteboardServer
        whiteboardSocket = TestUtility.connectToWhiteboardServer();
    }

}
