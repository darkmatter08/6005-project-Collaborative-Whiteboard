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
            System.out.println("receive server actions");
            receiveServerActions();
        } catch (IOException e) {
            System.out.println("caught ioexception in receiver.run");
            e.printStackTrace(); // but don't terminate serve()
        } finally {
            try {
                System.out.println("socket close attempt in receiver.run()");
                client.socket.close();
            } catch (IOException e) {
                System.out.println("caught ioexception in receiver.run's finally-catch");
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private void receiveServerActions() throws IOException {
        System.out.println("entered receiver.receiveServerActions()");
        System.out.println("socket closed? " + client.socket.isClosed());
        this.objIn = new ObjectInputStream(client.socket.getInputStream());
        System.out.println("finished init of objIn");
        try {
            System.out.println("entered receiver.receiveServerActions() try block");
            for (List<WhiteboardAction> actions = (List<WhiteboardAction>)objIn.readObject(); actions != null;
                    actions = (List<WhiteboardAction>)objIn.readObject()) {
                client.canvas.applyActions(actions);
            }
        } catch (Exception e){
            System.out.println("caught exception in receiver.receiveserveractions()");
            e.printStackTrace();
        } finally {
            System.out.println("socket close attempt in receiver.receiveServerAction()");
            client.socket.close();
        }
    }
}