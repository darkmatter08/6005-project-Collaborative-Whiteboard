package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class MasterServerStarterTest {
    
    private Socket socket = null;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    private BufferedReader in; 
    private PrintWriter out; 
    
    @Before
    public void setup() throws IOException {
        TestUtility.startServer(); // starts MasterServerStarter
        socket = TestUtility.connect();
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.objIn = new ObjectInputStream(socket.getInputStream());
    }
    
    @Test
    public void testServerStart() throws IOException {
        out.write("createNewWhiteboard");
        try {
            List<Integer> allWhiteboards = (List<Integer>) objIn.readObject();
        } catch (ClassNotFoundException e) {
            fail("unexpected object returned");
        }
    }
    
}
