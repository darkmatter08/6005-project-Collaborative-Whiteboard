package server;

import canvas.Whiteboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import shared.WhiteboardAction;

public class SlaveServer implements Runnable {
    // Must synchronize on whiteboard, as it is shared across multiple threads
    // Every whiteboard instance is unique.
    private final MasterWhiteboard whiteboard;
    private final Socket socket;
    
    // IO
    private ObjectOutputStream objOut = null;
    private ObjectInputStream objIn = null;
    
    /**
     * Constructor for a new ConnectionHandler. 
     * @param whiteboard
     * @param socket
     */
    public SlaveServer(MasterWhiteboard whiteboard, Socket socket) {
        this.whiteboard = whiteboard;
        this.socket = socket;
    }
    
    public void run() {
        // handle the client
        try {
            handleConnection();
        } catch (IOException e) {
            e.printStackTrace(); // but don't terminate serve()
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void handleConnection() throws IOException {
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.objIn = new ObjectInputStream(socket.getInputStream());
        
        try {
            for (List<WhiteboardAction> actions = (List<WhiteboardAction>)objIn.readObject(); actions != null;
                    actions = (List<WhiteboardAction>)objIn.readObject()) {
                handleRequest(actions);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            objOut.close();
            socket.close();
        }
    }
    
    private void handleRequest(List<WhiteboardAction> actions) {
        whiteboard.applyActions(actions);
    }
}
