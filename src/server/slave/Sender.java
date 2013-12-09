package server.slave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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