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
    private int lastHistorySize;
    
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
        // handle the client
        
        // receive client draw actions
        new Thread() {
            @Override
            public void run() {
                try {
                    receiveClientActions();
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
        }.start();
        
        // send client draw actions
        new Thread() {
            @Override
            public void run() {
                try {
                    sendActions();
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
        }.start();
    }

    private void receiveClientActions() throws IOException {
        this.objIn = new ObjectInputStream(socket.getInputStream());
        
        try {
            for (List<WhiteboardAction> actions = (List<WhiteboardAction>)objIn.readObject(); actions != null;
                    actions = (List<WhiteboardAction>)objIn.readObject()) {
                whiteboard.applyActions(actions);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
    
    private void sendActions() throws IOException {
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        
        try {
            for (List<WhiteboardAction> actions = (List<WhiteboardAction>)objIn.readObject(); actions != null;
                    actions = (List<WhiteboardAction>)objIn.readObject()) {
                int newHistorySize;
                synchronized (whiteboard) {
                    newHistorySize = whiteboard.getHistory().size();
                }
                List<WhiteboardAction> newActions = ((List<WhiteboardAction>)whiteboard.getHistory()).subList(lastHistorySize, newHistorySize - 1);
                objOut.writeObject(newActions);
                this.lastHistorySize = newHistorySize;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
