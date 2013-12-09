package canvas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import shared.WhiteboardAction;

class Sender extends Thread {
    private SlaveClient client;
    private ObjectInputStream objIn = null;
    private ObjectOutputStream objOut = null;
    
    public Sender(SlaveClient client) {
        this.client = client;
    }
    
    public void run() {
        try {
            sendActions();
        } catch (IOException e) {
            e.printStackTrace(); // but don't terminate serve()
        } finally {
            try {
                client.socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private void sendActions() throws IOException {
        this.objOut = new ObjectOutputStream(client.socket.getOutputStream());
        
        List<WhiteboardAction> copyCurrentActions;
        synchronized (client.canvas.getCurrentActions()) {
            copyCurrentActions = (List<WhiteboardAction>)client.canvas.getCurrentActions().clone();
            client.canvas.getCurrentActions().clear();
        }
        objOut.writeObject(copyCurrentActions);
        objOut.flush();
    }
}