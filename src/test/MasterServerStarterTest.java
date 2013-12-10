package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.ProtocolUtility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests both the MasterServerStarter (to establish the connection)
 *  and the MasterServer to send and receive messages. 
 * Sets up one continuous server instance and then closes the sockets 
 *  after the jUnit is completed. 
 * @author jains
 *
 */
public class MasterServerStarterTest {
    
    private static Socket socket;
    private static BufferedReader in; 
    private static PrintWriter out; 
    
    @BeforeClass
    public static void setup() throws IOException {
        TestUtility.startServer(); // starts MasterServerStarter
        socket = TestUtility.connect();
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("done setup");
    }
    
    @AfterClass
    public static void teardown() throws IOException, InterruptedException {
        in.close();
        out.close();
        socket.close();
        System.out.println("done teardown");
        Thread.sleep(500);
    }
    
    @Test
    public void fullTest() throws IOException {
        testCreateNewWhiteboard();
        testGetWhiteboardIds();
        testManyNewWhiteboards();
    }
    
    public void testCreateNewWhiteboard() throws IOException {
        out.println("createNewWhiteboard");

        List<Integer> allWhiteboards = ProtocolUtility.convertListTypeToInt(
                ProtocolUtility.deserialize(in.readLine()));
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        assertTrue(allWhiteboards.equals(expected));
    }
    
    public void testGetWhiteboardIds() throws IOException {
        out.println("getWhiteboardIds");
        
        List<Integer> allWhiteboards = ProtocolUtility.convertListTypeToInt(
                ProtocolUtility.deserialize(in.readLine()));
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        assertTrue(allWhiteboards.equals(expected));
    }
     
    public void testManyNewWhiteboards() throws IOException {        
        //Board 2
        out.println("createNewWhiteboard");

        List<Integer> allWhiteboards = ProtocolUtility.convertListTypeToInt(
                ProtocolUtility.deserialize(in.readLine()));
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(2);
        assertTrue(allWhiteboards.equals(expected));
        
        out.println("createNewWhiteboard");
        
        String s = in.readLine();
        allWhiteboards = ProtocolUtility.convertListTypeToInt(
                ProtocolUtility.deserialize(s));
        expected.add(3);
        assertTrue(allWhiteboards.equals(expected));
    }

    
}
