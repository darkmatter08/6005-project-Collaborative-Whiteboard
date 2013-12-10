package test;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Tests both the MasterServerStarter (to establish the connection)
 *  and the MasterServer to send and receive messages. 
 * Sets up one continuous server instance and then closes the sockets 
 *  after the jUnit is completed. 
 * @author jains
 *
 */
public class PickerServerTest {
    
    private static Socket socket;
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
    public static void teardown() throws IOException, InterruptedException {
        in.close();
        out.close();
        socket.close();
    }
    
    @Test
    public void fullTest() throws IOException {
        testCreateNewWhiteboard();
        testGetWhiteboardIds();
        testManyNewWhiteboards();
    }
    
    public void testCreateNewWhiteboard() throws IOException {
        out.println(Messages.CREATE_NEW_WHITEBOARD);

        int allWhiteboards = Integer.parseInt(in.readLine());
        assertEquals(allWhiteboards, 1);
    }
    
    public void testGetWhiteboardIds() throws IOException {
        out.println(Messages.ASK_FOR_WHITEBOARDS);
        
        int allWhiteboards = Integer.parseInt(in.readLine());
        assertEquals(allWhiteboards, 1);
    }
     
    public void testManyNewWhiteboards() throws IOException {        
        //Board 2
        out.println(Messages.CREATE_NEW_WHITEBOARD);

        int allWhiteboards = Integer.parseInt(in.readLine());
        assertEquals(allWhiteboards, 2);
        
        out.println(Messages.CREATE_NEW_WHITEBOARD);
        
        allWhiteboards = Integer.parseInt(in.readLine());
        assertEquals(allWhiteboards, 3);
    }

    
}
