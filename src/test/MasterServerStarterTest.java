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
import java.util.ArrayList;
import java.util.List;

public class MasterServerStarterTest {
    
    private Socket socket = null;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    private BufferedReader in; 
    private PrintWriter out; 
    
    @Before
    public void setup() throws IOException, InterruptedException {
        TestUtility.startServer(); // starts MasterServerStarter
        socket = TestUtility.connect();
        Thread.sleep(300);
        
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.objIn = new ObjectInputStream(socket.getInputStream());
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    
    @Test
    public void testServerStart() throws IOException, InterruptedException {
        Thread.sleep(300);
        System.out.println(this.out);
        out.write("createNewWhiteboard\n");
        System.out.println("sent createNewWhiteboard");
        try {
            while(objIn.available() == 0){
            }
            List<Integer> allWhiteboards = (List<Integer>) objIn.readObject();
            List<Integer> expected = new ArrayList<Integer>();
            expected.add(1);
            assert(allWhiteboards.equals(expected));
        } catch (ClassNotFoundException e) {
            fail("unexpected object returned");
        }
    }
    

    
}
