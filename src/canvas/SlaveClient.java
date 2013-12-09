package canvas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import server.MasterWhiteboard;

public class SlaveClient implements Runnable {
    // Must synchronize on whiteboard, as it is shared across multiple threads
    // Every whiteboard instance is unique.
    public final Canvas canvas;
    public final Socket socket;
    
    // IO
    //private ObjectOutputStream objOut = null;
    
    public int lastHistorySize;
    
    /**
     * Constructor for a new ConnectionHandler. 
     * @param whiteboard
     * @param socket
     */
    public SlaveClient(Canvas canvas, Socket socket) {
        this.canvas = canvas;
        this.socket = socket;
    }
    
    public void run() {
        new Sender(this).start();
        new Receiver(this).start();
    }

    
    
    
}
