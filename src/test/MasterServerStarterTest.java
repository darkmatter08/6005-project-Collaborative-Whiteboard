package test;

import static org.junit.Assert.*;
import org.junit.AfterClass;
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
    }
    
    @AfterClass
    public static void teardown() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    
    @Test
    public void testCreateNewWhiteboard() throws IOException {
        out.println("createNewWhiteboard");

        List<Integer> allWhiteboards = ProtocolUtility.convertListTypeToInt(
                ProtocolUtility.deserialize(in.readLine()));
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        assert(allWhiteboards.equals(expected));
    }
    
    @Test
    public void testGetWhiteboardIds() throws IOException {
        out.println("getWhiteboardIds");
        
        List<Integer> allWhiteboards = ProtocolUtility.convertListTypeToInt(
                ProtocolUtility.deserialize(in.readLine()));
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        assert(allWhiteboards.equals(expected));
    }

    
}
