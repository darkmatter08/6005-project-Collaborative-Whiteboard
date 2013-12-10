package server.slave;

import canvas.Whiteboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import server.MasterWhiteboard;

public class SlaveServer implements Runnable {
    // Must synchronize on whiteboard, as it is shared across multiple threads
    // Every whiteboard instance is unique.
    public final MasterWhiteboard whiteboard;
    public final Socket socket;
    public BufferedReader in;
    
    // IO
    //private ObjectOutputStream objOut = null;
    
    public int lastHistorySize;
    
    /**
     * Constructor for a new ConnectionHandler. 
     * @param whiteboard
     * @param socket
     * @throws IOException 
     */
    public SlaveServer(MasterWhiteboard whiteboard, Socket socket) throws IOException {
        this.whiteboard = whiteboard;
        this.socket = socket;
        lastHistorySize = 0;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("constructed SlaveServer");
    }
    
    public void run() {
        try {
            System.out.println("starting SlaveServer threads");
            new Sender(this).start();
            new Receiver(this, in).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
