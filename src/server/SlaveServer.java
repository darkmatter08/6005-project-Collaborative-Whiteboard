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
    public final MasterWhiteboard whiteboard;
    public final Socket socket;
    
    // IO
    private ObjectOutputStream objOut = null;
    
    public int lastHistorySize;
    
    /**
     * Constructor for a new ConnectionHandler. 
     * @param whiteboard
     * @param socket
     */
    public SlaveServer(MasterWhiteboard whiteboard, Socket socket) {
        this.whiteboard = whiteboard;
        this.socket = socket;
        lastHistorySize = 0;
    }
    
    public void run() {
        new Sender(this).start();
        new Receiver(this).start();
    }

    
    
    
}

class Sender extends Thread {
    private SlaveServer server;
    private ObjectInputStream objIn = null;
    private ObjectOutputStream objOut = null;
    
    public Sender(SlaveServer server) {
        this.server = server;
    }
    
    public void run() {
        try {
            sendActions();
        } catch (IOException e) {
            e.printStackTrace(); // but don't terminate serve()
        } finally {
            try {
                server.socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private void sendActions() throws IOException {
        this.objIn = new ObjectInputStream(server.socket.getInputStream());
        this.objOut = new ObjectOutputStream(server.socket.getOutputStream());
        
        try {
            for (List<WhiteboardAction> actions = (List<WhiteboardAction>)objIn.readObject(); actions != null;
                    actions = (List<WhiteboardAction>)objIn.readObject()) {
                int newHistorySize;
                synchronized (server.whiteboard) {
                    newHistorySize = server.whiteboard.getHistory().size();
                }
                List<WhiteboardAction> newActions = ((List<WhiteboardAction>)server.whiteboard.getHistory()).subList(server.lastHistorySize, newHistorySize - 1);
                objOut.writeObject(newActions);
                server.lastHistorySize = newHistorySize;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            server.socket.close();
        }
    }
}

class Receiver extends Thread {
    private SlaveServer server;
    private ObjectInputStream objIn = null;
    
    public Receiver(SlaveServer server) {
        this.server = server;
    }
    
    public void run() {
        try {
            receiveClientActions();
        } catch (IOException e) {
            e.printStackTrace(); // but don't terminate serve()
        } finally {
            try {
                server.socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private void receiveClientActions() throws IOException {
        this.objIn = new ObjectInputStream(server.socket.getInputStream());
        
        try {
            for (List<WhiteboardAction> actions = (List<WhiteboardAction>)objIn.readObject(); actions != null;
                    actions = (List<WhiteboardAction>)objIn.readObject()) {
                server.whiteboard.applyActions(actions);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            server.socket.close();
        }
    }
}
