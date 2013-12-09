package server.slave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import shared.WhiteboardAction;

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
                BlockingQueue<WhiteboardAction> history = server.whiteboard.getHistory();
                synchronized (history) {
                    server.whiteboard.getHistory().addAll(actions);
                }
                System.out.println(history);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            server.socket.close();
        }
    }
}