package server.slave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import shared.WhiteboardAction;

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
        this.objOut = new ObjectOutputStream(server.socket.getOutputStream());
        
        try {
            while (true) {
                int newHistorySize;
                LinkedList<WhiteboardAction> actionList;
                synchronized (server.whiteboard.getHistory()) {
                    newHistorySize = server.whiteboard.getHistory().size();
                    actionList = new LinkedList<WhiteboardAction>(server.whiteboard.getHistory());
                }
                List<WhiteboardAction> newActions = actionList.subList(server.lastHistorySize, newHistorySize);
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