package canvas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import shared.WhiteboardAction;

class Receiver extends Thread {
    private SlaveClient client;
    private ObjectInputStream objIn = null;
    
    public Receiver(SlaveClient client) {
        this.client = client;
    }
    
    public void run() {
        try {
            receiveServerActions();
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
    
    private void receiveServerActions() throws IOException {
        this.objIn = new ObjectInputStream(client.socket.getInputStream());
        
        try {
            for (List<WhiteboardAction> actions = (List<WhiteboardAction>)objIn.readObject(); actions != null;
                    actions = (List<WhiteboardAction>)objIn.readObject()) {
                client.canvas.applyActions(actions);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            client.socket.close();
        }
    }
}